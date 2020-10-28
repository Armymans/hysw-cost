package net.zlw.cloud.snsEmailFile.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author Armyman
 * @Description // aes解密
 * @Date 17:34 2020/10/27
 **/
public class AesEncryptUtil {
	
	//使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
	private static String KEY = "0000000671595991";
	
	private static String IV = "tdrdadq59tbss5n7";

	/**
	 * 解密方法
	 * @param data 要解密的数据
	 * @param key  解密key
	 * @param iv 解密iv
	 * @return 解密的结果
	 * @throws Exception
	 */
	public static String desEncrypt(String data, String key, String iv) throws Exception {
		try {
			byte[] encrypted1 = new Base64().decode(data);

			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用默认的key和iv解密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String desEncrypt(String data) throws Exception {
		return desEncrypt(data, KEY, IV);
	}

	/**
	* 测试
	*/
	public static void main(String args[]) throws Exception {
		String key = "0000000671595991";
		String iv = "tdrdadq59tbss5n7";
//		pUHWcQHq3slljOns6gUVaw== 解密后 = 111111
		String tdrdadq59tbss5n7 = desEncrypt("pUHWcQHq3slljOns6gUVaw==", "0000000671595991", "tdrdadq59tbss5n7");
		System.out.println("解密后"+tdrdadq59tbss5n7);
	}

}
