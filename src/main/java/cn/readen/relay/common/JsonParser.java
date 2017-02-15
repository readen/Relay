package cn.readen.relay.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Created by readen on 2017/2/15.
 */
public class JsonParser {
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectReader objectReader=objectMapper.reader();
    private  static JsonParser instance;

    private JsonParser(){}

    public static  JsonParser me(){
        if(instance==null){
            synchronized (JsonParser.class){
                instance=new JsonParser();
            }
        }
        return instance;
    }

    public  static ObjectReader getObjectReader(){
        return me().objectReader;
    }

}
