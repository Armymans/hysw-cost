package net.zlw.cloud.snsEmailFile.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 文件操作相关工具
 *
 *
 */
public class FileOperationUtil {
	
	private static Logger log = Logger.getLogger(FileOperationUtil.class);
	
	//判断文件上传的是否需要依据吴江和芜湖进行区分
	public static final String WuHu_File = Common.getValueByProperty("WuHu_File","/platform.properties");
	public static final String WuJiang_File = Common.getValueByProperty("WuJiang_File","/platform.properties");


	//将测试环境和生产环境 的url和systemcode写入配置文件
	private static final String INTERNAL_IP = Common.getValueByProperty("INTERNAL_IP","/platform.properties");
	private static final String SYSTEM_CODE	= Common.getValueByProperty("SYSTEM_CODE","/platform.properties");
//	private static final String UPLOAD_URL  = INTERNAL_IP + "/api/Document/Upload";
	private static final String DOWNLOAD_URL= INTERNAL_IP + "/api/Document/DownLoad";
//	private static final String DELETE_URL  = INTERNAL_IP + "/api/Document/Delete";

	//芜湖
	private static final String WH_INTERNAL_IP 	= Common.getValueByProperty("WH_INTERNAL_IP","/platform.properties");
	private static final String WH_SYSTEM_CODE	= Common.getValueByProperty("WH_SYSTEM_CODE","/platform.properties");
//	private static final String WH_UPLOADURL 	= WH_INTERNAL_IP+"/api/Document/Upload";
	private static final String WH_DOWNLOADURL 	= WH_INTERNAL_IP+"/api/Document/DownLoad";
//	private static final String WH_DELETEURL 	= WH_INTERNAL_IP+"/api/Document/Delete";

			
	//吴江
	private static final String WJ_INTERNAL_IP 	= Common.getValueByProperty("WJ_INTERNAL_IP","/platform.properties");
	private static final String WJ_SYSTEM_CODE	= Common.getValueByProperty("WJ_SYSTEM_CODE","/platform.properties");
//	private static final String WJ_UPLOADURL 	= WJ_INTERNAL_IP+"/api/Document/Upload";
	private static final String WJ_DOWNLOADURL 	= WJ_INTERNAL_IP+"/api/Document/DownLoad";
//	private static final String WJ_DELETEURL 	= WJ_INTERNAL_IP+"/api/Document/Delete";

	


	/**
	 * 下载文件，返回byte已转码直接用即可
	 * 
	 * @param userName 当前操作人账号
	 * @param fileId   文件id
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> download(String userName, String fileId) throws Exception {
		log.info("=============================文件下载开始----------原方法====================================");
		String url = DOWNLOAD_URL + "?systemCode=" + SYSTEM_CODE + "&userMsg=" + userName + "&fileGuid=" + fileId;
		log.info("文件下载的url："+url);
		HttpGet httpGet = new HttpGet(url);
		String result = httpClient(httpGet);
		//log.info("文件上传返回信息result："+result);
		Map<String, Object> parse = JSON.parseObject(result, Map.class);
		if ((boolean) parse.get("result")) {
			String data = (String) parse.get("data");
			Base64 base64 = new Base64();
			byte[] decode = base64.decode(data);
			parse.put("data", decode);
		}
		log.info("=============================文件下载结束----------原方法====================================");
		return parse;
	}
	
	/**
	 * 下载文件，返回byte已转码直接用即可
	 * 
	 * @param userName 当前操作人账号
	 * @param fileId   文件id
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> downloadBPM(String userName, String fileId) throws Exception {
		String url_ = "http://doc.huayanwater.com:8000/api/Document/DownLoad";
		String systemCode = "bpm";
		String url = url_ + "?systemCode=" + systemCode + "&userMsg=" + userName + "&fileGuid=" + fileId;
		System.out.println("文件下载信息："+url);
		HttpGet httpGet = new HttpGet(url);
		String result = httpClient(httpGet);
		
		Map<String, Object> parse = JSON.parseObject(result, Map.class);
		if ((boolean) parse.get("result")) {
			String data = (String) parse.get("data");
			Base64 base64 = new Base64();
			byte[] decode = base64.decode(data);
			parse.put("data", decode);
		}
		System.out.println("文件下载返回信息："+parse);
		return parse;
	}

	/**
	 * http请求调用
	 * 
	 * @param httpMethod
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String httpClient(HttpRequestBase httpMethod) throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = httpClient.execute(httpMethod);
		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		return result;
	}

	/*****
	 *  文件下载
	 * @param userName 用户域账号
	 * @param fileId 文件id
	 * @param fileSource 文件源
	 * 				2  吴江
	 * 				3  芜湖
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> fileDownload(String userName, String fileId,String fileSource) {
		try{
			log.info("===========================文件下载区分文档库,下载开始===============================");
			log.info("文件源fileSource:"+fileSource);
			
			String downUrl = "";
			String systemCode="";
			if(fileSource!=null){
				if("2".equals(fileSource)){
					//如果是bpm的文件，从吴江下载
					downUrl = WJ_DOWNLOADURL;
					systemCode = WJ_SYSTEM_CODE;
				}else if("3".equals(fileSource)){
					//芜湖的文件，从芜湖下载
					downUrl = WH_DOWNLOADURL;
					systemCode = WH_SYSTEM_CODE;
				}else{
					//兼容老数据
					downUrl = DOWNLOAD_URL;
					systemCode = SYSTEM_CODE;
				}
			}else{
				//兼容老数据
				downUrl = DOWNLOAD_URL;
				systemCode = SYSTEM_CODE;
			}
			String url = downUrl + "?systemCode=" + systemCode + "&userMsg=" + userName + "&fileGuid=" + fileId;
			
			log.info("文件下载信息："+url);
			
			HttpGet httpGet = new HttpGet(url);
			log.info("httpGet");
			String result = httpClient(httpGet);
			log.info("result");
			Map<String, Object> parse = JSON.parseObject(result, Map.class);
			log.info("文件下载返回信息parse:"+parse);
			if (parse!=null && (boolean) parse.get("result")) {
				log.info("1");
				String data = (String) parse.get("data");
				log.info("返回的data="+data);
				Base64 base64 = new Base64();
				byte[] decode = base64.decode(data);
				log.info("data="+decode);
				parse.put("data", decode);
				log.info("===========================文件下载区分文档库,下载结束 ===============================");
				return parse;
			}
		}catch(Exception ex){
			log.info("文件下载错误  FileOperationUtil - fileDownload -error ");
			ex.printStackTrace();
		}
		return null;
	}

}
