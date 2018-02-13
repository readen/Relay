/**
 * Copyright (c) 2011-2017, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.readen.relay.common;

import com.jfinal.kit.JsonKit;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * JsonRender.
 * <p>
 * IE 不支持content type 为 application/json, 在 ajax 上传文件完成后返回 json时 IE 提示下载文件,<br>
 * 解决办法是使用： render(new JsonRender(params).forIE());
 */
public class JsonCacheRender extends Render {

    /**
     * http://zh.wikipedia.org/zh/MIME
     * 在wiki中查到: 尚未被接受为正式数据类型的subtype，可以使用x-开始的独立名称（例如application/x-gzip）
     * 所以以下可能要改成 application/x-json
     * <p>
     * 通过使用firefox测试,struts2-json-plugin返回的是 application/json, 所以暂不改为 application/x-json
     * 1: 官方的 MIME type为application/json, 见 http://en.wikipedia.org/wiki/MIME_type
     * 2: IE 不支持 application/json, 在 ajax 上传文件完成后返回 json时 IE 提示下载文件
     */
    private static final String contentType = "application/json; charset=" + getEncoding();
    private static final String contentTypeForIE = "text/html; charset=" + getEncoding();

    private String jsonText;
    private int maxAge = 300; // 默认缓存时间为5分钟
    private String eTag;


    public JsonCacheRender(String jsonText) {
        if (jsonText == null) {
            // throw new IllegalArgumentException("The parameter jsonString can not be null.");
            this.jsonText = "null";
        } else {
            this.jsonText = jsonText;
            this.eTag = Integer.toString(jsonText.hashCode());
        }
    }


    public JsonCacheRender(Object object) {
        this.jsonText = JsonKit.toJson(object);
        this.eTag = Integer.toString(jsonText.hashCode());
    }

    public JsonCacheRender(Object object, int maxAge) {
        this.jsonText = JsonKit.toJson(object);
        this.eTag = Integer.toString(jsonText.hashCode());
        this.maxAge = maxAge;
    }

    public void render() {
        if (eTag.equals(request.getHeader("If-None-Match"))) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        PrintWriter writer = null;
        try {
            response.setHeader("Cache-Control", "max-age=" + maxAge + ", max-stale=3600");//最大缓存时间
            response.setHeader("ETag", eTag);
            response.setDateHeader("Expires", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(maxAge));
            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(jsonText);
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    public String getJsonText() {
        return jsonText;
    }

}






