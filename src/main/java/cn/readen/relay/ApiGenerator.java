package cn.readen.relay;

import cn.readen.relay.common.JsonParser;
import cn.readen.relay.generator.*;
import cn.readen.relay.model.ApiConfig;
import cn.readen.relay.model.ApiMethod;
import com.jfinal.config.Routes;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readen on 2017/2/15.
 */
public class ApiGenerator implements SourceGenerator {
    private static String[] apiNames = {"weather", "amapWeather"};
    //生成代码所在packageName
    private static final String packageName = "cn.readen.relay.api";
    //代码根目录
    private static final String baseDir = "/Users/readen/Documents/web/Relay/src/main/java";
    private static final String srcDir = baseDir + File.separator + packageName.replace(".", "/");
    private Map<String, ApiConfig> apiConfigs;
    private Map<String, String> classNames;

    private static ApiGenerator instance;

    public static ApiGenerator me() {
        if (instance == null) {
            synchronized (ApiGenerator.class) {
                instance = new ApiGenerator();
            }
        }
        return instance;
    }

    private ApiGenerator() {
        apiConfigs = new HashMap<>();
        classNames = new HashMap<>();
    }

    @Override
    public void generateSource(PrintStream out, String apiName) {
        ApiConfig config = getConfig(apiName);
        JClass jClass = new JClass(getClassName(apiName), packageName);
        jClass.setExtends(BaseController.class);
        jClass.add(new JImport(ApiGenerator.class));
        jClass.add(new JImport(ApiConfig.class));
        jClass.add(new JImport(ApiMethod.class));
        ApiMethod[] apiMethods = config.getApiMethods();
        for (int j = 0; j < apiMethods.length; j++) {
            JMethodBody methodBody = new JMethodBody();
            methodBody.append("ApiConfig apiConfig= ApiGenerator.me().getConfig(\"" + apiName + "\");");
            methodBody.append("ApiMethod method=apiConfig.getApiMethods()[" + j + "];");
            methodBody.append("handle(apiConfig,method);");
            JMethod method = new JMethod(Modifier.PUBLIC, false,
                    apiMethods[j].getName(), "void", methodBody);
            jClass.add(method);
        }
        jClass.print(out, 0);
    }


    @Override
    public ApiConfig getConfig(String apiName) {
        if (apiConfigs.get(apiName) == null) {
            String fileName = apiName + ".json";
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            String jsonConfig = CommentRemover.removeComments(inputStreamReader);
//        System.out.println(jsonConfig);
            try {
                ApiConfig config = JsonParser.getObjectReader().forType(ApiConfig.class).readValue(jsonConfig);
                apiConfigs.put(apiName, config);
                return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return apiConfigs.get(apiName);
    }

    @Override
    public String getClassName(String apiName) {
        if (classNames.get(apiName) == null) {
            classNames.put(apiName, captureName(apiName) + "Controller");
        }
        return classNames.get(apiName);
    }

    public void generateRoutes(String className, boolean replace) {
        File file = new File(srcDir, className + ".java");
        if (replace || !file.exists()) {
            JClass jClass = new JClass(className, packageName);
            jClass.setExtends(Routes.class);
            JMethodBody methodBody = new JMethodBody();
            for (int i = 0; i < apiNames.length; i++) {
                ApiConfig apiConfig = getConfig(apiNames[i]);
                methodBody.append("add(\"" + apiConfig.getLocalPath() + "\"," + getClassName(apiNames[i]) + ".class);");
            }
            JMethod jMethod = new JMethod(Modifier.PUBLIC, false, "config", "void", methodBody);
            jClass.add(jMethod);
            try {
                jClass.print(new PrintStream(file), 0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ApiGenerator apiGenerator = ApiGenerator.me();
        boolean saveToFile = true;
        System.out.println(srcDir);
        File file = new File(srcDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        apiGenerator.generateRoutes("ApiRouter", saveToFile);
        for (int i = 0; i < apiNames.length; i++) {
            String apiName = apiNames[i];
            File srcFile = new File(srcDir, apiGenerator.getClassName(apiName) + ".java");
            try {
                if (saveToFile) {
                    PrintStream printStream = new PrintStream(srcFile);
                    apiGenerator.generateSource(printStream, apiName);
                } else {
                    apiGenerator.generateSource(System.out, apiName);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
