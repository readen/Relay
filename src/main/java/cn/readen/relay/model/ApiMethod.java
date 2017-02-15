package cn.readen.relay.model;

/**
 * Created by readen on 2017/2/15.
 */
public class ApiMethod {
    private String name;
    private String httpMethod;
    private ApiParam[] urlParams;
    private ApiParam[] headerParams;
    private int cacheExpireTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public ApiParam[] getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(ApiParam[] urlParams) {
        this.urlParams = urlParams;
    }

    public ApiParam[] getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(ApiParam[] headerParams) {
        this.headerParams = headerParams;
    }

    public int getCacheExpireTime() {
        return cacheExpireTime;
    }

    public void setCacheExpireTime(int cacheExpireTime) {
        this.cacheExpireTime = cacheExpireTime;
    }
}
