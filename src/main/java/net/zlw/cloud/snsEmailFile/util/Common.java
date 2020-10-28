package net.zlw.cloud.snsEmailFile.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Common {

	/**
	 * 获取property文件的属性值
	 * @param key
	 * @return
	 */
	public static String getValueByProperty(String key){
		//ResourceBundle bundle=PropertyResourceBundle.getBundle("config/config",Locale.getDefault());
		Properties p = new Properties();
		try {
			InputStream in = Common.class.getResourceAsStream("/config/uct_auth.properties");
			p.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String path = bundle.getString(key);
		String path = p.getProperty(key);
		return path;
	}
	
	/**
	 * 通过文件路径获取property文件的属性值
	 * @param key
	 * @param filePath
	 * @return
	 */
	public static String getValueByProperty(String key,String filePath){
		//ResourceBundle bundle=PropertyResourceBundle.getBundle("config/config",Locale.getDefault());
		Properties p = new Properties();
		String defaultPath = "/config/uct_auth.properties";
		String path ="";
		if(filePath == null){
			filePath = defaultPath;
		}
		try {
			InputStream in = Common.class.getResourceAsStream(filePath);
			p.load(in);
			path = p.getProperty(key);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String path = bundle.getString(key);
		//String path = p.getProperty(key);
		
		return path;
	}
	
}
