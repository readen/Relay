package cn.readen.relay;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 捕获所有api action异常
 *
 * @author zhuanghl
 */
public class ApiLogInterceptor implements Interceptor {
    private static int maxOutputLengthOfParaValue = 256;
    private static final Log logger = Log.getLog(ApiLogInterceptor.class);

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        StringBuilder sb = new StringBuilder();
        long startTime = System.currentTimeMillis();
        sb.append(controller.getRequest().getMethod()).append("  ").
                append(inv.getViewPath() + inv.getMethodName()).append("\n");
        String urlParas = controller.getPara();
        if (urlParas != null) {
            sb.append("UrlPara     : ").append(urlParas).append("\n");
        }
        HttpServletRequest request = controller.getRequest();
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter   : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=");
                    if (values[0] != null && values[0].length() > maxOutputLengthOfParaValue) {
                        sb.append(values[0].substring(0, maxOutputLengthOfParaValue)).append("...");
                    } else {
                        sb.append(values[0]);
                    }
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
            }
            logger.info(sb.toString());
            inv.invoke();
            long costTime = System.currentTimeMillis() - startTime;
            logger.info(inv.getMethodName() + " Response :  " + controller.getResponse().getStatus() + "   " + costTime + "ms");
        }
    }

}
