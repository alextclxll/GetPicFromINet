package com.get;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
 
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
public class ImageFile implements Runnable {
	private String url;
	private String name;
	private static final String PATH = "D:\\img\\";
	static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier(){
 
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	};
	
	public ImageFile(String url,String name){
		this.url = url;
		this.name = name;
	}
 
	@Override
	public void run() {
		OutputStream os = null;
		InputStream in = null;
		SSLSocketFactory ssf = null;
    	
		File dir = new File(PATH);  
        if (!dir.exists()) {  
            dir.mkdirs();  
            System.out.println("图片存放于"+PATH+"目录下");  
        } 
        File file = new File(PATH+name);
        try {
			os = new FileOutputStream(file);
			URL u = new URL(this.url);  
            if (u.getProtocol().toLowerCase().equals("https")) {
            	HttpsURLConnection https = (HttpsURLConnection)u.openConnection();
            	https.setSSLSocketFactory(createSSL());
                https.setConnectTimeout(5000);
                https.setReadTimeout(5000);
                https.setDoOutput(true);
                https.setRequestMethod("GET");
                https.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0");
                https.connect();
                System.out.println(https.getResponseCode() + " " + https.getResponseMessage());
                in = https.getInputStream();
            } else {  
            	HttpURLConnection conn = (HttpURLConnection)u.openConnection();  
                conn.connect(); 
                System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                in = conn.getInputStream();
            }  
             
            byte[] buff = new byte[1024];  
            while(true){  
                int readed = in.read(buff);//读取内容长度  
                if(readed == -1){  
                    break;  
                }  
                byte[] temp = new byte[readed];  
                System.arraycopy(buff, 0, temp, 0, readed);//内容复制  
                os.write(temp);  
            }
            
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				os.close();
				if(in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
	}
	public static SSLSocketFactory createSSL() throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException{
		TrustManager[] tm =new TrustManager[]{
				myTrustManager
		};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		
		sslContext.init(null, tm, null);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		return ssf;
	}
	public static TrustManager myTrustManager = new X509TrustManager()
	{
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
 
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1){}
 
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};
}
