package net.zlw.cloud.snsEmailFile.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * 对请求参数签名工具类
 * @author zhaidawei@ruibetter.com
 */
public class SignRequest {
	/**
     * 给API请求签名。
     * @param params 所有字符型的API请求参数
     * @param secret 签名密钥
     * @param signMethod 签名方法，目前支持：md5, hmac_md5两种
     * @return 签名
     */
    public static String signRequest(Map<String, String> params,String secret, String signMethod)
            throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (Constants.SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (Constants.SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第五步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
    
    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
    
    /**
     * 对字符串采用UTF-8编码后，用MD5进行摘要。
     */
    public static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(Constants.CHARSET_UTF8));
    }

    /**
     * 对字节流进行MD5摘要。
     */
    public static byte[] encryptMD5(byte[] data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data);
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
    
    /**
     * 把字节流转换为十六进制表示方式。
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
    
    /**
	  * 获取加密后的字符串
	  * @param input
	  * @return
	  */
	 public static String stringMD5(String pw) {
		 try {  
			 // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
			 MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
			 // 输入的字符串转换成字节数组  
			 byte[] inputByteArray = pw.getBytes(Constants.CHARSET_UTF8);
			 // inputByteArray是输入字符串转换得到的字节数组  
			 messageDigest.update(inputByteArray);  
			 // 转换并返回结果，也是字节数组，包含16个元素  
			 byte[] resultByteArray = messageDigest.digest();  
			 // 字符数组转换成字符串返回  
			 return byteArrayToHex(resultByteArray);  
	     } catch (Exception e) {  
			 return null;  
	     }  
	 }
	 public static String byteArrayToHex(byte[] byteArray) {
		// 首先初始化一个字符数组，用来存放每个16进制字符  
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
		char[] resultCharArray =new char[byteArray.length * 2];  
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
		int index = 0; 
		for (byte b : byteArray) {  
			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
			resultCharArray[index++] = hexDigits[b& 0xf];  
		}
		// 字符数组组合成字符串返回  
		return new String(resultCharArray);  
	}
}
