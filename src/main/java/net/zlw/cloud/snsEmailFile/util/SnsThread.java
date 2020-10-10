package net.zlw.cloud.snsEmailFile.util;

import net.zlw.cloud.snsEmailFile.model.SnsInfo;
import net.zlw.cloud.snsEmailFile.service.SnsInfoService;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

public class SnsThread implements Runnable {
	Logger log = Logger.getLogger(this.getClass());

	private String phoneNum;
	private String content;
	
	public SnsThread(String phoneNum, String content){
		this.phoneNum = phoneNum;
		this.content = content;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean flag = SnsUtil.checkMobieNumber(phoneNum);
		//flag = false;//暂时关闭
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		SnsInfoService snsService = context.getBean(SnsInfoService.class);
		SnsInfo snsInfo = new SnsInfo();
		snsInfo.setId(UUID.randomUUID().toString());
		snsInfo.setPhone(phoneNum);
		snsInfo.setContent(content);
		snsInfo.setType("1");
		if (flag) {
			snsInfo.setCode("1");
		} else {
			snsInfo.setCode("0");
		}
		snsService.saveSnsInfo(snsInfo);
		
		log.info("判断是手机号码校验手机格式是否正确："+flag);
		if( flag ){
			log.info("SnsThread:调用短信发送功能");
			SnsUtil.sndMessage(phoneNum, content);
		}		
	}

}
