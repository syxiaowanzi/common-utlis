package com.common.utils.transfer.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;


/**
 * httpClient请求接口
 * @author wangming
 *
 */
public class HttpClientUtil {
	
	
	/**
	 * 超时时间设置
	 */
	private static int TIMEOUT = 10000000;
	
	/**
	 * POST请求
	 * @param url
	 * @param post
	 * @return
	 * @throws PAIException
	 * @throws IOException
	 */
	public static String post(String url,String post) throws IOException{
		OutputStream outputS = null;
		PrintWriter out = null;
		InputStream inputStream = null;
		try {
			URLConnection conn = initPostConn(url);
			conn.connect();
			//连接发送报文
			outputS = conn.getOutputStream();
			out = new PrintWriter(outputS);
			out.write(post);
			out.flush();
			/*
			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
				System.out.println("网络错误异常！请求码："+conn.getResponseCode());
			}
			*/
			//开始获取数据
			inputStream = conn.getInputStream();
			String backMsg = readIs(inputStream);
			return backMsg;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
			if(outputS != null)
				outputS.close();
			if(inputStream != null)
				inputStream.close();
		}
		return "";
	}
	
	/**
	 * 	根据URL，初始化连接
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static URLConnection initPostConn(String url) throws IOException{
		URL u = new URL(url);
		//HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		URLConnection conn = u.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		//conn.setRequestMethod("POST");
		conn.setUseCaches(false);
        //conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(TIMEOUT);
        
//        conn.setRequestProperty("Accept", "application/xml"); // 设置接收数据的格式
//        conn.setRequestProperty("Content-Type", "application/xml"); // 设置发送数据的格式
//        conn.setRequestProperty("Accept-Charset", "utf-8");
//        conn.setRequestProperty("contentType", "utf-8");
        
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        
		return conn;
	}
	
	
    /**
     *	 发送HTTP(POST)请求
     *
     * @param url
     * @param propsMap 发送的参数
     */

    public String httpSend(String promoteUrl) {
        //返回报文http://192.168.180.162:21000/bcm-rec/nci/unfiedOrder
    	//localhost--http://192.168.1.151:8080/bcm-rec/a/nci/unfiedOrder
        String responseContent = null;
        try {
            URL url_new = new URL(promoteUrl);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url_new.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Accept", "application/xml"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/xml"); // 设置发送数据的格式
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream()); // utf-8编码 ;
            //获取报文的字符串
            InputStream instream = HttpClientUtil.class.getResourceAsStream("/com/newstrength/pay/PlaceOrder.xml");
            /**
             * <xml>
				   <appid>wx2421b1c4370ec43b</appid>
				   <mch_id>10000100</mch_id>
				   <nonce_str>ec2316275641faa3aacf3cc599e8730f</nonce_str>
				   <transaction_id>1008450740201411110005820873</transaction_id>
				   <sign>FDD167FAA73459FD921B144BAF4F4CA2</sign>
			  </xml>
            */
            /**
        	XStream xs = new XStream(new DomDriver());
        	PlaceOrder placeOrder = new PlaceOrder();   
        	placeOrder.setDevice_info("WEB");
    		String sendXml = xs.toXML(placeOrder);
    		*/
            //拿到字符数组
            byte[] sendXml = HttpClientUtil.read(instream);
            //System.out.println(new String(sendXml));
            String sendMsg = new String(sendXml,"UTF-8");
            out.writeBytes(sendMsg);
            out.flush();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                   System.out.println("网络错误异常！请求码："+connection.getResponseCode());
            }
            InputStream in = connection.getInputStream();
            BufferedReader rds = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String message = "";
            String line = "";
            while((line = rds.readLine()) != null) {  
            	line+='\n';
            	message+= line;  
            }
            
            responseContent = message;
            out.close();
            rds.close();
            connection.disconnect();
            return responseContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent; // 自定义错误信息
    }
    
    /**
     * 	流中转化字节
     * @param instream
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream instream) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len=instream.read(buffer)) != -1)
		{
			bos.write(buffer, 0, len);
		}
		return bos.toByteArray();
	}
    
    /**
     * 	流转为字符串
     * @param instream
     * @return
     * @throws IOException
     */
    public static String readIs(InputStream instream) throws IOException{
    	StringBuilder content = new StringBuilder();
    	byte[] b_ = new byte[1024];
    	int length = 0;
    	while((length = instream.read(b_)) > -1){
    		content.append(new String(b_,0,length,"UTF-8"));
    	}
    	return content.toString();
    }
    
    /**
     * 	读取request中的流
     * @param request
     * @return
     * @throws IOException
     */
    public static String readrRequest(HttpServletRequest request) throws IOException{
    	int totalbytes = request.getContentLength();
    	if(totalbytes < 0){
    		return "";
    	}
    	DataInputStream dis = new DataInputStream(request.getInputStream());
    	byte[] b_ = new byte[totalbytes];
    	dis.readFully(b_);
    	dis.close();
    	return new String(b_,0,totalbytes,"UTF-8");
    }
    
    
    String readFile() throws IOException{
    	InputStream instream = HttpClientUtil.class.getResourceAsStream("/com/newstrength/pay/PlaceOrder.xml");
    	return readIs(instream);
    }

}
