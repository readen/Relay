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
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redis.clients.jedis.Jedis;

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
        try {
            AliRequest aliRequest = new AliRequest(apiConfig.getHost(), apiConfig.getBasePath() + "/" + apiMethod.getName(),
                    apiConfig.getAppKey(), apiConfig.getAppSecret(), querys, headers);
            Headers header = Headers.of(headers);
            Request request = new Request.Builder().url(aliRequest.getUrl()).
                    headers(header).build();
            String cacheKey = aliRequest.getUrl();
            Cache redis = Redis.use();
            Jedis jedis = redis.getJedis();
            redis.setThreadLocalJedis(jedis);
            DatumResponse result = redis.get(cacheKey);
            if (result != null) {
                render(new JsonCacheRender(result));
                return;
            }
            OkHttpClient client = RetrofitManager.me().getClient();
            Response response = client.newCall(request).execute();
            System.out.println(cacheKey);
            if (response.isSuccessful()) {
                String s = response.body().string();
                System.out.println(s.length());
                result = new DatumResponse(JsonParser.getObjectMapper().readValue(s, HashMap.class));
                redis.set(cacheKey, result);
                if (s.length() > 200) {
                    redis.expire(cacheKey, apiMethod.getCacheExpireTime() * 60);
                    render(new JsonCacheRender(result));
                } else {
                    redis.expire(cacheKey, 60);
                    renderJson(result);
                }
            } else {
                DatumResponse baseResponse = new DatumResponse(response.code(), response.header(SystemHeader.X_Ca_Error_Message));
                renderJson(baseResponse);
                redis.setex(cacheKey, 10, baseResponse);//错误缓存60s。60s后重试
            }
            redis.removeThreadLocalJedis();
            redis.close(jedis);
        } catch (Exception e) {
            renderJson(new BaseResponse(Code.CODE_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }

    protected boolean proceedQueries(Map<String, String> queries, ApiParam[] urlParams) {
        for (ApiParam urlParam : urlParams) {
            String value = getPara(urlParam.getName());
            if (value != null) {
                queries.put(urlParam.getName(), value);
            } else {
                if (urlParam.isRequire()) {
                    renderJson(new BaseResponse("Parameter " + urlParam + " can't be null"));
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
