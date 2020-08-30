package com.get;
 
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
 
public class Main_getImage {
 
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String html = null;
		List<String> list = new ArrayList<String>();//存放ImageURL
		try {
			html = Utils.getHtml("http://so.5tu.cn/tupian/chuntiantupian.html" );//返回的是字符串Html
		} catch (IOException e) {
			e.printStackTrace();
		}
		//利用工具类，获取每张图片的URL
		list = Utils.getImageUrl(html, Utils.HTTP_IMG);	
		for(String string :list){
			if(string.indexOf(".gif") != (string.length()-4) ||string.indexOf(".jpg") != (string.length()-4)
					|| string.indexOf(".png") != (string.length()-4)){
				//需改进
				String s[] = string.split("\" original=\"");
				System.out.println("s的长度:"+s.length);
				for(String ss:s){
					System.out.println("name:"+ss+"\n");
					new Thread(new ImageFile(ss,Utils.getImageName(ss))).start();
				}
			}else{
				System.out.println(string+"\n");
				new Thread(new ImageFile(string,Utils.getImageName(string))).start();
				System.out.println(Utils.getImageName(string));
			}
		}
	}
}
