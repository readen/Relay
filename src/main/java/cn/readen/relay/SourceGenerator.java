package cn.readen.relay;

import cn.readen.relay.model.ApiConfig;

import java.io.PrintStream;

/**
 * Created by readen on 2017/2/15.
 */
public interface SourceGenerator {

    void generateSource(PrintStream out,String apiName);

    ApiConfig getConfig(String apiName);

    String getClassName(String apiName);

}
