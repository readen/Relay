package cn.readen.relay;


import cn.readen.relay.api.ApiRouter;
import com.jfinal.config.*;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.IKeyNamingPolicy;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;


/**
 * Created by hnair20160706 on 2016/9/13.
 */
public class AppConfig extends JFinalConfig{

    @Override
    public void configConstant(Constants me) {
        PropKit.use("config.txt");
        me.setViewType(ViewType.JSP);
        //根据gt可以添加扩展函数，格式化函数，共享变量等，
        me.setDevMode(true);
        me.setJsonFactory(new JacksonFactory());
    }

    @Override
    public void configRoute(Routes me) {
        me.add(new ApiRouter());
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        loadPropertyFile("config.txt");
        RedisPlugin redisPlugin=new RedisPlugin("default",PropKit.get("redisHost"));
        redisPlugin.setKeyNamingPolicy(IKeyNamingPolicy.defaultKeyNamingPolicy);
        me.add(redisPlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }


    @Override
    public void afterJFinalStart() {
//        Cache cache= Redis.use();
//        cache.setThreadLocalJedis(cache.getJedis());
    }

    @Override
    public void configHandler(Handlers me) {

    }

}
