package cn.readen.relay.api;

import cn.readen.relay.BaseController;
import cn.readen.relay.ApiGenerator;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
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
