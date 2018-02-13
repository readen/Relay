package cn.readen.relay;

import cn.readen.relay.aliyun.AliRequest;
import cn.readen.relay.aliyun.SystemHeader;
import cn.readen.relay.common.*;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
import cn.readen.relay.model.ApiParam;
import cn.readen.relay.utils.RetrofitManager;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.sun.org.apache.regexp.internal.RE;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readen on 2017/2/15.
 */
public class BaseController extends Controller {


    protected void handle(ApiConfig apiConfig, ApiMethod apiMethod) {
        Map<String, String> querys = new HashMap<>();
        if (proceedQueries(querys, apiMethod.getUrlParams())) {
            return;
        }
        Map<String, String> headers = new HashMap<>();
        if (proceedHeaders(headers, apiMethod.getHeaderParams())) {
            return;
        }
        switch (apiConfig.getApiName()) {
            case "weather":
                AliRequest aliRequest = new AliRequest(apiConfig.getHost(), apiConfig.getBasePath() + "/" + apiMethod.getName(),
                        apiConfig.getAppKey(), apiConfig.getAppSecret(), querys, headers);
                Headers header = Headers.of(headers);
                String cacheKey = aliRequest.getUrl();
                fetchResult(cacheKey, header, apiMethod.getCacheExpireTime() * 60);
                break;
            case "amapWeather":
                String url = apiConfig.getHost() + apiConfig.getBasePath() + "/" + apiMethod.getName();
                url += "?key=" + apiConfig.getAppKey() + "&" + concatQueries(querys);
                fetchResult(url, null, apiMethod.getCacheExpireTime() * 60);
                break;
        }
    }


    protected void fetchResult(String url, Headers header, int cacheSeconds) {
        Cache redis = Redis.use();
        Jedis jedis = redis.getJedis();
        try {
            redis.setThreadLocalJedis(jedis);
            DatumResponse result = redis.get(url);
            if (result != null) {
                render(new JsonCacheRender(result));
                return;
            }
            Request.Builder builder = new Request.Builder().url(url);
            if (header != null) {
                builder.headers(header);
            }
            Request request = builder.build();
            OkHttpClient client = RetrofitManager.me().getClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String s = response.body().string();
                System.out.println(s.length());
                result = new DatumResponse(JsonParser.getObjectMapper().readValue(s, HashMap.class));
                redis.set(url, result);
                if (s.length() > 200) {
                    redis.expire(url, cacheSeconds);
                    render(new JsonCacheRender(result));
                } else {
                    redis.expire(url, 60);
                    renderJson(result);
                }
            } else {
                DatumResponse baseResponse = new DatumResponse(response.code(), response.toString());
                renderJson(baseResponse);
                redis.setex(url, 10, baseResponse);//错误缓存60s。60s后重试
            }
        } catch (IOException e) {
            renderJson(new BaseResponse(Code.CODE_ERROR, e.getMessage()));
            e.printStackTrace();
        } finally {
            redis.removeThreadLocalJedis();
            redis.close(jedis);
        }
    }


    protected String concatQueries(Map<String, String> queries) {
        if (!queries.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            return sb.toString();
        }
        return "";
    }

    protected boolean proceedQueries(Map<String, String> queries, ApiParam[] urlParams) {
        for (ApiParam urlParam : urlParams) {
            String value = getPara(urlParam.getName());
            if (value != null) {
                queries.put(urlParam.getName(), value);
            } else {
                if (urlParam.isRequire()) {
                    renderJson(new BaseResponse("Parameter " + urlParam.getName() + " can't be null"));
                    return true;
                } else if (urlParam.getDefaultValue() != null) {
                    queries.put(urlParam.getName(), urlParam.getDefaultValue());
                }
            }
        }
        return false;
    }

    protected boolean proceedHeaders(Map<String, String> headers, ApiParam[] headerParams) {
        for (ApiParam headParam : headerParams) {
            String value = getHeader(headParam.getName());
            if (value != null) {
                headers.put(headParam.getName(), value);
            } else {
                if (headParam.isRequire()) {
                    renderJson(new BaseResponse("Header param " + headParam + " can't be null"));
                    return true;
                } else if (headParam.getDefaultValue() != null) {
                    headers.put(headParam.getName(), headParam.getDefaultValue());
                }
            }
        }
        return false;
    }


}
