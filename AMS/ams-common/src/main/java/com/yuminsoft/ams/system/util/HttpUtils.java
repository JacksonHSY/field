package com.yuminsoft.ams.system.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuminsoft.ams.system.vo.apply.AntifraudReqNewVo;

/**
 * 基于 httpclient http工具类
 * @author fuhongxing
 *
 */
public class HttpUtils {

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doGet(String url,Map<String,String> params,String charset){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	try {
    		if(params != null && !params.isEmpty()){
    			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
    		}
    		HttpGet httpGet = new HttpGet(url);
    		CloseableHttpResponse response = httpClient.execute(httpGet);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpGet.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doPost(String url, Map<String,String> params, String charset){
    	if(StringUtils.isBlank(url)){
    		return null;
    	}
    	try {
    		List<NameValuePair> pairs = null;
    		if(params != null && !params.isEmpty()){
    			pairs = new ArrayList<NameValuePair>(params.size());
    			for(Map.Entry<String,String> entry : params.entrySet()){
    				String value = entry.getValue();
    				if(value != null){
    					pairs.add(new BasicNameValuePair(entry.getKey(),value));
    				}
    			}
    		}
    		HttpPost httpPost = new HttpPost(url);
    		if(pairs != null && pairs.size() > 0){
    			httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
    		}
    		CloseableHttpResponse response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, "utf-8");
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    
    
    /**
     * HTTP Post 发送json请求(针对spring使用@RequestBody时使用)
     * @param url  请求的url地址 ?之前的地址
     * @param json	 请求的json参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static String doPost(String url, String json, String charset){
    	if(StringUtils.isEmpty(url)){
    		return null;
    	}
    	try {
    		HttpPost httpPost = new HttpPost(url);
    		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");//设置数据格式
    		//给httpPost设置JSON格式的参数  
            StringEntity stringEntity = new StringEntity(json, CHARSET); // 解决中文乱码问题
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding(CHARSET);
            httpPost.setEntity(stringEntity);
            //发送请求
    		CloseableHttpResponse response = httpClient.execute(httpPost);
    		int statusCode = response.getStatusLine().getStatusCode();
    		if (statusCode != HttpStatus.SC_OK) {
    			httpPost.abort();
    			throw new RuntimeException("HttpClient,error status code :" + statusCode);
    		}
    		//获取返回结果
    		HttpEntity entity = response.getEntity();
    		String result = null;
    		if (entity != null){
    			result = EntityUtils.toString(entity, CHARSET);
    		}
    		EntityUtils.consume(entity);
    		response.close();
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");

	public static MediaType mediaTypeForm = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	
    /**
     * Spring RestTemplate 发送post请求,传递json参数
     * @param requestUrl
     * @param json
     * @param type
     * @return
     */
    public static <T> T postForJson(String requestUrl, String json, Class<T> type) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		org.springframework.http.HttpEntity<String> formEntity = new org.springframework.http.HttpEntity<String>(json, headers);
		restTemplate.postForObject(requestUrl, formEntity, type);
		return restTemplate.postForObject(requestUrl, formEntity, type);
	}
    
    /**
	 * 请求第三方接口
	 * spring RestTemplate
	 * @param url 请求目标地址
	 * @param mediaType 头文件信息
	 * @param param 请求参数
	 * @param type 响应数据类型
	 * @return
	 */
	public static <T> T postForEntity(String requestUrl, Map<String, Object> param, Class<T> type) {
		if (StringUtils.isEmpty(requestUrl)) {
			return null;
		}
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<T> responseEntity = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaTypeForm);
			org.springframework.http.HttpEntity<Map<String, Object>> request = new org.springframework.http.HttpEntity<Map<String, Object>>(param, headers);
			responseEntity = restTemplate.postForEntity(requestUrl, request, type);
			org.springframework.http.HttpStatus httpStatus = responseEntity.getStatusCode();
			switch (httpStatus) {
				case OK:
					LOGGER.info("[status:200]" + requestUrl + "参数：" + param);
					/** 正常响应 **/
					T value = responseEntity.getBody();
					if (value instanceof String) {
						//return (T) new String(value.toString().getBytes("ISO-8859-1"), "UTF-8");
					//} else {
						return value;
					}
				default:
					LOGGER.error("[status:" + httpStatus + "]" + requestUrl + "参数：" + param);
					throw new RuntimeException("调用第三方接口异常URL：" + requestUrl + " ; 响应码：" + httpStatus);
			}
		} catch (Exception e) {
			LOGGER.error("http请求接口调用异常URL[{}],参数[{}]!!!", requestUrl, param, e);
		}
		return null;
	}
    
	
	/**
	 * https post请求
	 * @param url 请求地址
     * @param param 参数
     * @param charset 字符编码
     * @param headers 数据格式
	 */
    public static String doPostHttps(String url, String param, String charset, Map<String, String> headers) {
        OutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = getConnection(realUrl);

            Map<String, String> defaultHeaders = new LinkedHashMap<>();
            defaultHeaders.put("accept", "*/*");
            defaultHeaders.put("connection", "Keep-Alive");
            defaultHeaders.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if(null != headers) {
                defaultHeaders.putAll(headers);
            }
            //set header
            //defaultHeaders.forEach((k, v) -> conn.setRequestProperty(k, v));
            Iterator<String> ite = defaultHeaders.keySet().iterator();
            String key = null;
            while (ite.hasNext()) {
                key = ite.next();
                conn.setRequestProperty(key, defaultHeaders.get(key));
            }
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = conn.getOutputStream();
            if(param != null) {
                // 发送请求参数
                out.write(param.getBytes(charset));
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	LOGGER.error("https post 请求发送异常!!", e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
	
    /**
     * https post请求
     * @param url 请求地址
     * @param param 参数
     * @param charset 字符编码
     * @param headers 数据格式
     * @return
     */
    public static String doPostHttps(String url, Map<String, String> param, String charset, Map<String, String> headers) {
        return doPostHttps(url, buildParams(param, charset), charset, headers);
    }
    
    /**
     * 构建请求参数
     * @param param
     * @param charset
     * @return
     */
    public static String buildParams(Map<String, String> param, String charset) {
        if (param != null && !param.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                try {
                    buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset)).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return buffer.deleteCharAt(buffer.length() - 1).toString();
        } else {
            return null;
        }
    }

    
    /**
     * 获取http是请求连接对象
     * @param url
     * @return
     */
    public static HttpURLConnection getConnection(URL url) {
        HttpURLConnection connection = null;
        try{
            if(url.getProtocol().toUpperCase().startsWith("HTTPS")) {
                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(new KeyManager[0], new TrustManager[] {new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }}, new SecureRandom());


                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                conn.setSSLSocketFactory(ctx.getSocketFactory());
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(1000*60*30);

                conn.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                connection = conn;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }


        } catch(Exception e){
            e.printStackTrace();
        }

        return connection;
    }
    
    public static void main(String []args){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("customerIdCard", "555422451158");
    	map.put("customerName", "jack");
    	map.put("queryDate", "2016-11-30");
    	map.put("reportId", "100742");
    	map.put("sources", "AMS");
    	map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    	JSONObject json = new JSONObject();
    	Map<String, String> test = new HashMap<String, String>();
    	
//    	String getData = doGet("http://10.8.30.29:8080/",null);
//    	System.out.println(getData);
    	
    	map.put("appNo", "2017042815022488571");
    	map.put("idCard", "15010219850602022X");
    	map.put("name", "张三");
    	map.put("cellphone", "13876769999");
    	map.put("address", "20170428150224969232");
    	map.put("homePhone", "20170428150224969232");
    	map.put("companyName", "20170428150224969232");
    	map.put("companyAddress", "20170428150224969232");
    	map.put("companyPhone", "20170428150224969232");
    	map.put("contactPhone1", "20170428150224969232");
    	
    	AntifraudReqNewVo antifraudvo = new AntifraudReqNewVo();
		antifraudvo.setAppNo("20170428150224969232");
		antifraudvo.setName("张三");
		antifraudvo.setIdNo("15010219850602039X");
		antifraudvo.setAddress("鞍山人民路营业中心");
		antifraudvo.setContactPhone1("13878787878");
		System.out.println(JSON.toJSONString(antifraudvo));
//		System.out.println(JSON.toJSONString(map));
		test.put("param", JSON.toJSONString(map));
    	String postData = doPost("http://10.8.30.29:8080/creditReport/getAntifraudInfo", test, null);
    	System.out.println("返回结果："+postData);
//    	String result = doPost("http://10.8.30.29:8080/creditReport/getSuanHuaAntifraudNew", JSON.toJSONString(antifraudvo), null);
//    	System.out.println(postData);
    }
    
	/**
	 * HTTP-POST方法 params传递方式
	 * @author Shipf
	 * @param url http服务地址
	 * @param json 参数
	 * @return JSONObject
	 */
	@SuppressWarnings("finally")
	public JSONObject postBaseMethod(String url, Map<String, String> map) {
		JSONObject resp = new JSONObject();
		try {
			if (StringUtils.isBlank(url)) {
				resp.put("code", "000002");
				resp.put("message", "调用接口url地址不能为空.");
			}
			// 构造HttpClient的实例
			HttpClient client = new HttpClient();
			// 创建PostMethod的实例
			PostMethod postMethod = new PostMethod(url);
			postMethod.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			for (String item : map.keySet()) {
				postMethod.addParameter(item, map.get(item));
			}
			// 执行getMethod,调用http接口
			client.executeMethod(postMethod);
			// 服务调用失败：404
			if (404 == postMethod.getStatusCode()) { 
				resp.put("code", "000001");
				resp.put("message", "调用接口失败,请联系系统管理员.");
			} else {
				resp.put("response", postMethod.getResponseBodyAsString());
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			return resp;
		}
	}
    
}