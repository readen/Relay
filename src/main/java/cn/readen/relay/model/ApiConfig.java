package cn.readen.relay.model;

/**
 * Created by readen on 2017/2/15.
 */
public class ApiConfig {
    private String apiName;
    private String host;
    private String basePath;
    private String localPath;
    private String appKey;
    private String appSecret;
    private ApiMethod[] apiMethods;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public ApiMethod[] getApiMethods() {
        return apiMethods;
    }

    public void setApiMethods(ApiMethod[] apiMethods) {
        this.apiMethods = apiMethods;
    }
}
