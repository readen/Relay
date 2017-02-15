/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.readen.relay.http;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequest {
	private String url = null;
	private MethodType method = null;
	protected FormatType contentType = null;
	protected byte[] content = null;
	protected String encoding = null;
	protected Map<String, String> headers = null;
	
	public HttpRequest(String strUrl) {
		this.url = strUrl;
		this.headers = new HashMap<String, String>();
	}
	
	public HttpRequest(String strUrl, Map<String, String> tmpHeaders) {
		this.url = strUrl;
		if (null != tmpHeaders)
			this.headers = tmpHeaders;
	}
	
	public HttpRequest() {
	}
	
	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public FormatType getContentType() {
		return contentType;
	}
	
	public void setContentType(FormatType contentType) {
		this.contentType = contentType;
		if (null != this.content || null != contentType) {
			this.headers.put("Content-Type", getContentTypeValue(this.contentType, this.encoding));
		}
		else { 
			this.headers.remove("Content-Type");
		}
	}
	
	public MethodType getMethod() {
		return method;
	}
	
	public void setMethod(MethodType method) {
		this.method = method;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public String getHeaderValue(String name) {
		return this.headers.get(name);
	}
	
	public void putHeaderParameter(String name, String value) {
		if (null != name && null != value)
			this.headers.put(name, value);
	}
	
	public void setContent(byte[] content, String encoding, FormatType format)
			throws NoSuchAlgorithmException {
		
		if (null == content) {
			this.headers.remove("Content-MD5");
			this.headers.remove("Content-Length");
			this.headers.remove("Content-Type");
			this.contentType = null;
			this.content = null;
			this.encoding = null;
			return;
		}
		this.content = content;
		this.encoding = encoding;
		String contentLen =String.valueOf(content.length);
		String strMd5 = md5Sum(content);
		if (null != format) {
			this.contentType = format;
		}
		else {
			this.contentType = FormatType.RAW;
		}
		this.headers.put("Content-MD5", strMd5);
		this.headers.put("Content-Length", contentLen);
		this.headers.put("Content-Type", getContentTypeValue(contentType, encoding));
	}
	
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}
	
	public HttpURLConnection getHttpConnection() throws IOException {
		Map<String, String> mappedHeaders = this.headers;
		String strUrl = url;
		if (null == strUrl || null == this.method){
			return null;
		}
		URL url = null;
		String[] urlArray = null;
		if(MethodType.POST.equals(this.method)){
			urlArray =	strUrl.split("\\?");
			url = new URL(urlArray[0]); 
		}
		else {
			url = new URL(strUrl);
		}
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod(this.method.toString());
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
	
		for(Entry<String, String> entry: mappedHeaders.entrySet()) {
			httpConn.setRequestProperty(entry.getKey(), entry.getValue());
		}

		if(null != getHeaderValue("Content-Type")){
			httpConn.setRequestProperty("Content-Type", getHeaderValue("Content-Type"));
		}
		else {
			String contentTypeValue = getContentTypeValue(contentType, encoding);
			if(null != contentTypeValue){
				httpConn.setRequestProperty("Content-Type", contentTypeValue);
			}
		}
		
		if(MethodType.POST.equals(this.method) && urlArray.length == 2){
			httpConn.getOutputStream().write(urlArray[1].getBytes());
		}
		return httpConn;
	}
	
	private String getContentTypeValue(FormatType contentType, String encoding) {
		if(null != contentType && null != encoding){
			return FormatType.mapFormatToAccept(contentType) + 
					";charset=" + encoding.toLowerCase();
		}
		else if (null != contentType) {
			return FormatType.mapFormatToAccept(contentType);
		}
		return null;
	}

	public static String md5Sum(byte[] buff) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md.digest(buff);
		return Base64Helper.encode(messageDigest);
	}
}
