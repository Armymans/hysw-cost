package net.zlw.cloud.snsEmailFile.util;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;

public class SmsUtils {

    public static String sender(String phone, String content) {

//        String url = "http://220.180.134.38:10002/SendSMSService.asmx";// 提供接口的地址外网
        String url = "http://10.61.98.5:600/SendSMSService.asmx";// 提供接口的地址内网
        String soapAction = "http://10.61.98.5/webservice/sms/"; // 域名，这是在server定义的--不知道的可以问接口提供方，他们一并提供这个

        Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(url);
            call.setOperationName(new QName(soapAction, "SendSMS")); // 设置要调用哪个方法
            call.addParameter(new QName(soapAction, "mobile"), // 设置要传递的参数--要和接口方提供的参数名一致
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "message"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "subcategory"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "preSendTime"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "expiredTime"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "sendLevel"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(soapAction, "DND"), // 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.setReturnType(new QName(soapAction, "SendSMS"), String.class); // 要返回的数据类型（自定义类型，我这边接口提供方给我返回的是json字符串，所以我用string类型接收。这个地方一定要设置好，不然各种报错很崩溃）

            call.setUseSOAPAction(true);
            call.setSOAPActionURI(soapAction + "SendSMS");

            String resulet = (String) call.invoke(new Object[]{phone, content, "招采平台", null, null, "0", "0"});// 调用方法并传递参数-传递的参数和设置的参数要对应，顺序不能搞错了

            return resulet;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }
    
    public static void main(String[] args) {
		String content="（华衍水务电子招标平台）尊敬，马鞍山华衍水务有限公司邀请您参与2019-MXJ093-仓库水表保温套请购的询价，请登录招采平台，在业务通知处查看详情。";
		sender("18255747151", content);
	}
}
