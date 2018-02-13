package cn.readen.relay.api;

import cn.readen.relay.BaseController;
import cn.readen.relay.ApiGenerator;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
public class AmapWeatherController extends BaseController {


  public void weatherInfo() {
    ApiConfig apiConfig= ApiGenerator.me().getConfig("amapWeather");
    ApiMethod method=apiConfig.getApiMethods()[0];
    handle(apiConfig,method);
  }

}
