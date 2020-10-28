package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.ConfigHelper;
import net.tec.cloud.common.util.StrUtil;
import net.zlw.cloud.snsEmailFile.model.EmailInfo;
import net.zlw.cloud.snsEmailFile.service.EmailInfoService;
import net.zlw.cloud.snsEmailFile.util.MailInfo;
import net.zlw.cloud.snsEmailFile.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description 邮件接口
 * @Date 2020/10/9 16:48
 **/
@RestController
public class EmailInfoController extends BaseController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private EmailInfoService emailInfoService;

    /**
     * @Author Armyman
     * @Description //发送邮件接口
     * @Date 17:48 2020/10/11
     **/
    @RequestMapping(value = "/sendEmailInterface", method = RequestMethod.POST)
    public void sendEmailInterface(String toAddress, String emailSubject, String emailContent) {
        boolean flag = sendEmail(toAddress, emailSubject, emailContent);
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setId("Email"+ UUID.randomUUID().toString().replace("-","").substring(0,15));
        emailInfo.setSender(ConfigHelper.getProperty("email_userName_zlw"));
        emailInfo.setReceiver(toAddress);
        emailInfo.setContent(emailContent);
        emailInfo.setSubject(emailSubject);
        emailInfo.setCreateTime(sdf.format(new Date()));
        if(flag){
            emailInfo.setSendStatus("1");
        }else{
            emailInfo.setSendStatus("0");
        }
        emailInfoService.save(emailInfo);
    }

    public static void main(String[] args) {
        sendEmail("2827778977@qq.com", "邮件", "邮件内容");
    }

    public static boolean sendEmail(String toAddress, String emailSubject, String emailContent) {
        boolean flag = true;
        try {
            MailInfo mailInfo = new MailInfo();
            mailInfo.setMailServerHost("zao.jia@huayanwater.com");
            mailInfo.setMailServerPort("465");
            mailInfo.setValidate(true);
            // mailInfo.setUserName("zlwbid");
            // mailInfo.setPassword("bid123456");//您的邮箱密码
            // mailInfo.setFromAddress("zlwbid@126.com");
            mailInfo.setUserName(ConfigHelper.getProperty("email_userName_zlw"));
            mailInfo.setPassword(ConfigHelper.getProperty("email_passWord_zlw")); // 您的邮箱密码
            mailInfo.setFromAddress(ConfigHelper.getProperty("email_fromAddress_zlw"));

            mailInfo.setToAddress(toAddress);
            mailInfo.setSubject(emailSubject);
            mailInfo.setContent(emailContent);

            // 插入数据库记录
            // CommonService commonService = (CommonService)
            // Struts2Utils.getSpringBean("commonServiceImpl");
            // commonService.addEmailInfo(mailInfo);

            // 当配置文件设置发送时再发送邮件
            String sendFlag = ConfigHelper.getProperty("send_email");
            if (StrUtil.isNotEmpty(sendFlag) && sendFlag.equals("1")) {
                flag = MailSender.sendHtmlMail(mailInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

}
