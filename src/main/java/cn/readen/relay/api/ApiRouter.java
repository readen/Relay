package cn.readen.relay.api;

import com.jfinal.config.Routes;
public class ApiRouter extends Routes {


  public void config() {
    add("/weather",WeatherController.class);
    add("/amap/weather",AmapWeatherController.class);
  }

}
