package net.zlw.cloud.snsEmailFile.util;

import com.alibaba.fastjson.JSONObject;
import net.tec.cloud.common.util.ConfigHelper;
import net.tec.cloud.common.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class SnsUtilAbstract {

	private static Log log = LogFactory.getLog(SnsUtilAbstract.class.getClass());

	public static boolean sendMsg(String phone, String content){
		String sendFlag = ConfigHelper.getProperty("send_phone");
		if(StrUtil.isNotEmpty(sendFlag) && sendFlag.equals("0")){
			log.info("SnsUtilAbstract关闭短信功能-不发送短信");
			return false;
		} else {
			log.info("SnsThread:调用短信发送功能");
			String response = null;
			try {
				response = SmsRtnJsonUtils.sendSms(phone, content);
				// json数据转换
				JSONObject jsonObject = JSONObject.parseObject(response);
				// 获取返回码和返回信息
				String errcode = jsonObject.getString("errcode");
				String errmsg = jsonObject.getString("errmsg");

				log.info("短信api返回信息 errcode:" + errcode + " errmsg:" + errmsg);
				// 如果errcode和errmsg为空，发送成功
				if (org.apache.commons.lang3.StringUtils.isEmpty(errcode) && StringUtils.isEmpty(errmsg)){
					return true;
				}

			} catch (ApiException | IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean sndMessage(String phoneNum, String content) {
		// 首先就保存短信内容，到数据库；存在发送不成功的情况时，我们也可以得到验证码。
		//saveSns(phoneNum, content);
		
		//当配置文件设置发送时再发送短信
        String sendFlag = ConfigHelper.getProperty("send_phone");
        if(StrUtil.isNotEmpty(sendFlag) && sendFlag.equals("0")){
        	log.info("SnsUtilAbstract关闭短信功能-不发送短信");
        	return false;
        }
        log.info("SnsUtilAbstract开始发送短信"+phoneNum);
		String sender = SmsUtils.sender(phoneNum, content);
		log.info("短信发送完毕，获取短信发送结果sender="+sender);
		if("error".equals(sender)){
			return false;
		}
		return true;
	}

	public static boolean checkMobieNumber(String phoneNum) {
		boolean flag = false;
		if (phoneNum != null) {
			Pattern regex = Pattern.compile("^(0|86|17951)?(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])[0-9]{8}$");
			Matcher matcher = regex.matcher(phoneNum);
			flag = matcher.matches();
		}
		return flag;
	}
 
	/**
	 * 生成6位随机验证码
	 * 
	 * @return 验证码
	 */
	public static String createRandomCode() {
		String code = "";
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			code += r.nextInt(10);
		}
		return code;
	}

	
}
