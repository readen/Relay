package cn.readen.relay;

import cn.readen.relay.aliyun.AliRequest;
import cn.readen.relay.aliyun.SystemHeader;
import cn.readen.relay.common.BaseResponse;
import cn.readen.relay.http.HttpResponse;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
import cn.readen.relay.model.ApiParam;
import com.jfinal.core.Controller;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readen on 2017/2/15.
 */
public class BaseController extends Controller{


    protected void handle(ApiConfig apiConfig, ApiMethod apiMethod){
        Map<String,String> querys=new HashMap<>();
        if(proceedQueries(querys,apiMethod.getUrlParams())){
            return;
        }
        Map<String,String> headers=new HashMap<>();
        if(proceedHeaders(headers,apiMethod.getHeaderParams())){
            return;
        }
        AliRequest aliRequest=new AliRequest(apiConfig.getHost(),apiConfig.getBasePath()+"/"+apiMethod.getName(),
                apiConfig.getAppKey(),apiConfig.getAppSecret(),querys,headers);
        String cacheKey=aliRequest.getUrl();
        System.out.println(cacheKey);
        Cache redis= Redis.use();
        String result=redis.get(cacheKey);
        if(result!=null){
            renderJson(new String(result));
            return;
        }

        try {
            HttpResponse response=HttpResponse.getResponse(aliRequest);
            if(response.getStatus()== 200){
                result=new String(response.getContent());
                redis.setThreadLocalJedis(redis.getJedis());
                redis.set(cacheKey,result);
                redis.expire(cacheKey,apiMethod.getCacheExpireTime()*60);
                redis.removeThreadLocalJedis();
                renderJson(result);
            }else {
                renderJson(new BaseResponse(response.getStatus(),response.getHeaderValue(SystemHeader.X_Ca_Error_Message)));
            }

        } catch (IOException e) {
            renderJson(new BaseResponse(e.getMessage()));
            e.printStackTrace();
        }
    }

   protected boolean  proceedQueries(Map<String,String> queries,ApiParam[] urlParams){
       for (ApiParam urlParam:urlParams) {
           String value=getPara(urlParam.getName());
           if(value!=null){
               queries.put(urlParam.getName(),value);
           }else {
               if(urlParam.isRequire()){
                   renderJson(new BaseResponse("Parameter "+urlParam+" can't be null"));
                   return true;
               }else if(urlParam.getDefaultValue()!=null){
                   queries.put(urlParam.getName(),urlParam.getDefaultValue());
               }
           }
       }
       return false;
   }

    protected boolean  proceedHeaders(Map<String,String> headers,ApiParam[] headerParams){
        for(ApiParam headParam:headerParams){
            String value=getHeader(headParam.getName());
            if(value!=null){
                headers.put(headParam.getName(),value);
            }else {
                if(headParam.isRequire()){
                    renderJson(new BaseResponse("Header param "+headParam+" can't be null"));
                    return true;
                }else if(headParam.getDefaultValue()!=null){
                    headers.put(headParam.getName(),headParam.getDefaultValue());
                }
            }
        }
        return false;
    }


}
