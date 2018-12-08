package com.common.utils.transfer.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClient {
	private static final CloseableHttpClient httpclient;  
	public static final String CHARSET = "UTF-8";  
    
    static{  
        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(6000).build();  
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();  
    } 
    /** 
     * HTTP Post 获取内容 
     * @param url请求的url地址 ?之前的地址 
     * @param params请求的参数 
     * @param charset编码格式 
     * @return 页面内容 
     * @throws IOException  
     * @throws ClientProtocolException  
     */  
      
    public static String sendPost(String url, Map<String, Object> params){  
    	List<NameValuePair> pairs = null;
    	CloseableHttpResponse response = null;
    	HttpEntity entity = null;
    	String result = null;
        try {
        	if (params != null && !params.isEmpty()) {  
        		pairs = new ArrayList<NameValuePair>(params.size());  
        		for (String key : params.keySet()) {  
        			pairs.add(new BasicNameValuePair(key, params.get(key).toString()));  
        		}  
        	}  
        	HttpPost httpPost = new HttpPost(url);
        	if (pairs != null && pairs.size() > 0) {  
        		httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));  
        	}  
        	response = httpclient.execute(httpPost);  
        	int statusCode = response.getStatusLine().getStatusCode();  
        	if (statusCode != 200) {
        		Header header = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD 中的
                String newuri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
                System.out.println(newuri);
        		httpPost.abort();  
        		throw new RuntimeException("HttpClient,error status code :" + statusCode);  
        	}  
        	entity = response.getEntity();  
        	if (entity != null) {  
        		result = EntityUtils.toString(entity, "utf-8");  
        		EntityUtils.consume(entity);  
        		response.close();  
        		return result;  
        	}else{  
        		return result;
        	}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
    }
    /** 
     * HTTP Get 获取内容 
     * @param url请求的url地址 ?之前的地址 
     * @param params请求的参数 
     * @param charset编码格式 
     * @return 页面内容 
     */  
    public static String sendGet(String url, Map<String, Object> params){
    	CloseableHttpResponse response = null;
    	HttpEntity entity = null;
    	String result = null;
        try {
        	if(params !=null && !params.isEmpty()){  
        		
        		List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());  
        		
        		for (String key :params.keySet()){  
        			pairs.add(new BasicNameValuePair(key, params.get(key).toString()));  
        		}  
        		url +="?"+EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);  
        	}  
        	
        	HttpGet httpGet = new HttpGet(url);  
        	response = httpclient.execute(httpGet);  
        	int statusCode = response.getStatusLine().getStatusCode();  
        	if(statusCode !=200){  
        		httpGet.abort();  
        		throw new RuntimeException("HttpClient,error status code :" + statusCode);  
        	}  
        	entity = response.getEntity();  
        	if (entity != null) {  
        		result = EntityUtils.toString(entity, "utf-8");  
        		EntityUtils.consume(entity);  
        		response.close();  
        		return result;  
        	}else{  
        		return result;
        	}
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
    }  
    public static void main(String[] args) {
    	String url = "http://localhost:10006/rbac";
		String loginUrl = url+"/eb/person/aginInsure.gsp";
		Map<String, Object> params = new HashMap<String, Object>();
		HttpClient.sendPost(loginUrl,params);
	}
}
