package com.common.utils.transfer.http; 
  
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

/**
 *	 带证书的https请求工具类
 * @author wangming
 */
  
public class PostHttp {  
	

    public static String requestHTTPS(String targetUrl,String params,HttpServletRequest request) {  
	    PrintWriter out = null;
	    BufferedReader in = null;
	    //证书密码
	    String passwd = "";
	    //证书名称
	    String keyName = "";	
	    String result = "";
		try {  
	        URL url = new URL(targetUrl);  
	        HttpsURLConnection connection = (javax.net.ssl.HttpsURLConnection) url  
	                .openConnection();  
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
	       
	        InputStream instream =  request.getServletContext().getResourceAsStream("/WEB-INF/key/"+keyName);
	        //FileInputStream instream = new FileInputStream(new File("C://Users//leo//Desktop//app-keystore")); 
	        try {  
	            // 加载keyStore     
	            trustStore.load(instream, passwd.toCharArray());  
	            } catch (CertificateException e) {  
	                e.printStackTrace();  
	            } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	            } finally {  
	                try {  
	                    instream.close();  
	                } catch (Exception ignore) {  
	                }  
	            }  
	            TrustManagerFactory tmf =   
	                      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
	            tmf.init(trustStore);  
	              
	            X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];  
	  
	            SSLContext ctx = SSLContext.getInstance("TLS");  
		        ctx.init(null, new TrustManager[] {defaultTrustManager}, null);  
		          
		        SSLSocketFactory sslFactory = ctx.getSocketFactory();  
		        connection.setSSLSocketFactory(sslFactory);  
		        connection.setHostnameVerifier(new PostHttp().new TrustAnyHostnameVerifier());
		        connection.setDoOutput(true);  
		        connection.setDoInput(true);  
		        //connection.setRequestMethod("post");  
		        connection.setUseCaches(false);  
		        connection.setInstanceFollowRedirects(true);  
		        connection.setRequestProperty("Content-Type", "application/json");  
		        connection.setRequestProperty("Accept", "application/json");  
		        connection.connect();
		        out = new PrintWriter(connection.getOutputStream());
		        // 发送请求参数
		        out.print(params);
		        // flush输出流的缓冲
		        out.flush();
		        // 定义BufferedReader输入流来读取URL的响应
		        in = new BufferedReader(
		                new InputStreamReader(connection.getInputStream()));
		        String line;
		        while ((line = in.readLine()) != null) {
		        	line = new String(line.getBytes(), "utf-8");
		            result += line;
		        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    //使用finally块来关闭输出流、输入流
	    finally{
	            if(out!=null){
	                out.close();
	            }
	            if(in!=null){
	                try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	    } 
            
        return result;  
    } 
    
    public class TrustAnyHostnameVerifier implements HostnameVerifier {
    	  public boolean verify(String hostname, SSLSession session) {
    	   // 直接返回true
    	   return true;
    	  }
    }
    /**
     * 	初始化统一下单接口
     * @return
     */
    public static String initURL(){
    	String ip_URL = "http://192.168.180.162:21000";
		
		String path_URL = "/bcm-rec/a/nci/unfiedOrder";
		if(!path_URL.startsWith("/")){
			path_URL = "/" + path_URL;
		}
		return ip_URL + path_URL;
    }
    
	public static void main(String[] args) {
    	//发送 POST 请求
    	
    	/*String xmlStr = "<xml><device_info>1234567890</device_info>";//设备号
		xmlStr = xmlStr + "<body>JSAPI支付测试</body>";//商品描述
		xmlStr = xmlStr + "<attach>10000100</attach>";//附加数据 原样返回
		xmlStr = xmlStr + "<out_trade_no>WD_"+Long.toString(System.currentTimeMillis() / 1000)+"</out_trade_no>";//商户订单号
		xmlStr = xmlStr + "<fee_type>CNY</fee_type>";//货币类型
		xmlStr = xmlStr + "<total_fee>1</total_fee>";//总金额
		xmlStr = xmlStr + "<spbill_create_ip>127.0.0.1</spbill_create_ip>";//终端IP
		xmlStr = xmlStr + "<trade_type>JSAPI</trade_type>";//交易类型
		xmlStr = xmlStr + "<distribution_channel>WD</distribution_channel>";//销售渠道
		xmlStr = xmlStr + "<openid>o5lHPw7-PQiZ6RxqVwoXRV_ZI9xY</openid>";//用户标识
		xmlStr = xmlStr + "<policy_no>0001</policy_no>";//投保单号
		xmlStr = xmlStr + "<bus_type>续期缴费</bus_type></xml>";//业务类型
		System.out.println(xmlStr);
		
		String xmlStr = "<xml>"
					  +"<device_info>WEB</device_info>"
					  +"<body>员福平台测试（小微团单交通工具A款）</body>"
					  +"<attach>暂留</attach>"
					  +"<out_trade_no>01605001121675</out_trade_no>"
					  +"<time_start>20171225101654</time_start>"
					  +"<time_expire></time_expire>"
					  +"<fee_type>CNY</fee_type>"
					  +"<total_fee>15</total_fee>"
					  +"<spbill_create_ip>192.168.180.87</spbill_create_ip>"
					  +"<trade_type>NATIVE</trade_type>"
					  +"<product_id>86001598</product_id>"
					  +"<distribution_channel>YF</distribution_channel>"
					  +"<openid></openid>"
					  +"<sub_openid></sub_openid>"
					  +"<policy_no></policy_no>"
					  +"<bus_type></bus_type>"
					  +"</xml>";
		
		String sr;
		try {
			sr = PostHttp.requestHTTPS("https://192.168.180.162:21002/bcm-rec/a/nci/unfiedOrder",xmlStr.replaceAll("\r|\n", "").replaceAll(" ", ""));
			System.out.println(sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
}