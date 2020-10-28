package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.snsEmailFile.model.SnsInfo;
import net.zlw.cloud.snsEmailFile.service.SnsInfoService;
import net.zlw.cloud.snsEmailFile.util.SnsThread;
import net.zlw.cloud.snsEmailFile.util.SnsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * @Author Armyman
 * @Description 短信接口
 * @Date 2020/10/9 16:48
 **/
@RestController
public class SnsInfoController extends BaseController {


    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SnsInfoService snsInfoService;
    /**
     * @Author Armyman
     * @Description //发送短信
     * @Date 22:46 2020/10/9
     **/
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public String sendCode(String phone ,String content,String business){
        try {
            boolean flag = SnsUtil.checkMobieNumber(phone);
            log.info("判断是手机号码校验手机格式是否正确："+flag);
            if( flag ) {
                log.info("SnsThread:调用短信发送功能");
                boolean b = SnsUtil.sndMessage(phone, content);
//                WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//                SnsInfoService snsInfoService = context.getBean(SnsInfoService.class);
                SnsInfo snsInfo = new SnsInfo();
                snsInfo.setId("Sms"+UUID.randomUUID().toString().replace("-","").substring(0,15));
                snsInfo.setPhone(phone);
                snsInfo.setContent(content);
                snsInfo.setType("1");
                if (b) {
                    snsInfo.setCode("1");
                } else {
                    snsInfo.setCode("0");
                }
                snsInfoService.saveSnsInfo(snsInfo);
//            SnsThread sns = new SnsThread(phone, content);
//            Thread t = new Thread(sns);
//            t.start();
            }

            return "T";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
