package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.snsEmailFile.util.SnsThread;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;


/**
 * @Author Armyman
 * @Description 短信接口
 * @Date 2020/10/9 16:48
 **/
@RestController
public class SnsInfoController extends BaseController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @Author Armyman
     * @Description //发送短信
     * @Date 22:46 2020/10/9
     **/
    public String sendCode(String phone ,String content,String business){
        try {
            SnsThread sns = new SnsThread(phone, content);
            Thread t = new Thread(sns);
            t.start();

            return "T";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
