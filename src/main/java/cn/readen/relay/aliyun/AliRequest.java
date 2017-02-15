package cn.readen.relay.aliyun;

import cn.readen.relay.http.FormatType;
import cn.readen.relay.http.HttpRequest;
import cn.readen.relay.http.MethodType;
import cn.readen.relay.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by readen on 2017/2/15.
 */
public class AliRequest extends HttpRequest {


    public AliRequest(String host, String path, String appKey, String appSecret,Map<String,String> querys){
        this(host,path,appKey,appSecret,querys,null);
    }

    public AliRequest(String host, String path, String appKey, String appSecret,Map<String,String> querys,Map<String,String> headers){
        if(headers==null){
            headers= new HashMap<>();
        }
        this.headers =headers;
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT,"application/json");
        setMethod(MethodType.GET);
        setContentType(FormatType.JSON);
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT,"application/json");
        try {
            setUrl(initUrl(host,path,querys));
            initialBasicHeader(MethodType.GET.name(),path,headers,
                    querys,null,null,appKey,appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String initUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append(Constants.SPE3);
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append(Constants.SPE4);
                        sbQuery.append(URLEncoder.encode(query.getValue(), Constants.ENCODING));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append(Constants.SPE5).append(sbQuery);
            }
        }

        return sbUrl.toString();
    }


    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers,
                                                          Map<String, String> querys,
                                                          Map<String, String> bodys,
                                                          List<String> signHeaderPrefixList,
                                                          String appKey, String appSecret)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        //headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_SIGNATURE,
                SignUtil.sign(appSecret, method, path, headers, querys, bodys, signHeaderPrefixList));

        return headers;
    }

}
