package com.get;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.net.ssl.SSLContext;
 
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;  
  /**
   * 
   * @author 90604
   * 工具类
   */
public class Utils {  
	
	// 获取img标签正则
    public static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    public static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";
	//获取标签background
    public static final String BACKGROUND_REG = 
    		"url/((/S+?)/)";
    //获取图片链接（这一个使用，其他没使用）
    public static final String HTTP_IMG = "(http|https)://.+\\.(jpg|gif|png)";
    //获取网页源码
    public static String getHtml(String urlString) throws IOException{
		 URL url = new URL(urlString);
		 HttpURLConnection hrc = (HttpURLConnection) url.openConnection();
		 InputStream in = hrc.getInputStream();
		 String html = Utils.convertStreamToString(in);
		 return html;
	 }
    //获取网页源码，利用HttpClient
    public static String setImageConnectTool(String url){
    	String html = null;
    	RequestConfig globalConfig =   
	             RequestConfig.custom()
	             .setCookieSpec(CookieSpecs.STANDARD)
	             .setConnectionRequestTimeout(5000)//设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
	             .setConnectTimeout(6000)//设置连接超时时间，单位毫秒
	             .build(); 
		 //创建httpClient实例
		 CloseableHttpClient httpClient = HttpClients.custom()
				 .setDefaultRequestConfig(globalConfig)
				 .build();
		 //url代表每张图片下载地址
		 HttpGet httpGet = new HttpGet(url);
		 //创建httpget请求
		 httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0");  
		 //执行get请求
		 try {
			 //获取get请求
			CloseableHttpResponse response = httpClient.execute(httpGet);
			//获取响应实体
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());
		 
			InputStream in = entity.getContent();  //得到请求回来的数据
			//得到请求到的页面
			Utils.convertStreamToString(in);
				
		 } catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		 } catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		 }  
         return html;
    }
    
    public static String convertStreamToString(InputStream in) throws UnsupportedEncodingException {        
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        StringBuilder sb = new StringBuilder();    
        String line = null;  
        String str = System.getProperty("line.separator");
        try {    
            while ((line = reader.readLine()) != null) {    
                sb.append(line + str);    
            }    
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                in.close();    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }    
        return sb.toString();    
    }    
  //获取ImageUrl地址
    public static List<String> getImageUrl(String html,String reg){
        Matcher matcher=Pattern.compile(reg).matcher(html);
        List<String>listimgurl=new ArrayList<String>();
        while (matcher.find()){
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }
 
    //获取ImageSrc地址
    public List<String> getImageSrc(List<String> listimageurl){
        List<String> listImageSrc=new ArrayList<String>();
        for (String image:listimageurl){
            Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()){
                listImageSrc.add(matcher.group().substring(0, matcher.group().length()-1));
            }
        }
        return listImageSrc;
    }
    public static String getImageName(String urlName){
    	String str = null;
    	int start = urlName.lastIndexOf("/");
    	int end = urlName.length();
    	str = urlName.substring(start+1, end);
    	return str;
    }
   
}  
