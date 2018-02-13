package cn.readen.relay.api;

import cn.readen.relay.ApiLogInterceptor;
import cn.readen.relay.BaseController;
import cn.readen.relay.ApiGenerator;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
import com.jfinal.aop.Before;

@Before(ApiLogInterceptor.class)
public class WeatherController extends BaseController {


  public void city() {
    ApiConfig apiConfig= ApiGenerator.me().getConfig("weather");
    ApiMethod method=apiConfig.getApiMethods()[0];
    handle(apiConfig,method);
  }

  public void query() {
    ApiConfig apiConfig= ApiGenerator.me().getConfig("weather");
    ApiMethod method=apiConfig.getApiMethods()[1];
    handle(apiConfig,method);
  }

}
