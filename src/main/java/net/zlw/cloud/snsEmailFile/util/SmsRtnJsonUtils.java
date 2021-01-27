package net.zlw.cloud.snsEmailFile.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 华衍水务短信平台接口
 * 返回数据格式为json
 */

public class SmsRtnJsonUtils {

    private static String apiUrl = "http://10.61.98.67/api/sendsm.json";

    private static String appKey = "21011";

    private static String appSecret = "0b2a6dcb75f1c75c018b23c9861e7fcf";

    private static String signMethod = "md5";//或者为md5

    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");


    public static String sendSms(String phone ,String content) throws IOException, ApiException {

        String yyyyMMddHHmmss=sdf.format(Calendar.getInstance().getTime());
        //要发送的消息报文
        String request="<request><batchid>B20161031125959001</batchid><data><id>1000</id><phone>"+ phone +"</phone><content>"+ content +"</content><sendtime></sendtime></data></request>";
        //发送参数
        Map<String, String> param=new HashMap<String, String>();
        param.put(net.zlw.cloud.snsEmailFile.util.Constants.APP_KEY, appKey);
        param.put(net.zlw.cloud.snsEmailFile.util.Constants.TIMESTAMP, yyyyMMddHHmmss);
        param.put(net.zlw.cloud.snsEmailFile.util.Constants.SIGN_METHOD, signMethod);
        param.put(net.zlw.cloud.snsEmailFile.util.Constants.REQUEST_MD5, net.zlw.cloud.snsEmailFile.util.SignRequest.stringMD5(request));//对发送消息进行md5
        String url=null;
        try{
            //对请求参数hmac或md5签名
            if ("md5".equals(signMethod)){
                param.put(net.zlw.cloud.snsEmailFile.util.Constants.SIGN, SignRequest.signRequest(param,appSecret, Constants.SIGN_METHOD_MD5));
            } else {
                param.put(net.zlw.cloud.snsEmailFile.util.Constants.SIGN, SignRequest.signRequest(param,appSecret, Constants.SIGN_METHOD_HMAC));
            }

            //请求路径及参数构建
            String query= net.zlw.cloud.snsEmailFile.util.WebUtils.buildQuery(param);
            System.out.println("url" + apiUrl);
            url= net.zlw.cloud.snsEmailFile.util.WebUtils.buildRequestUrl(apiUrl,query);

            String response= WebUtils.doPost(url, param, request);
            return response;
        }catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(e);
        }
    }




}
