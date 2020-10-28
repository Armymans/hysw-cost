package net.zlw.cloud.snsEmailFile.util;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * @Author Armyman
 * @Description //校验域账户
 * @Date 17:34 2020/10/27
 **/
public class AdUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AdUtils.connect("shen.jinhua","Rr20110316!");
	}
	
	public static String connect(String username,String password) {
		 LdapContext ctx=null;
		 Hashtable<String,String> HashEnv = new Hashtable<String,String>();
		 HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		 HashEnv.put(Context.SECURITY_PRINCIPAL, "hywater\\"+username);
		 HashEnv.put(Context.SECURITY_CREDENTIALS, password);
		 HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		 HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");
		 String ldap = "ldap://whdc01.hywater.com:389";
		 HashEnv.put(Context.PROVIDER_URL, ldap);
		 try {
			ctx =  new InitialLdapContext(HashEnv, null);
			System.out.println("身份验证成功!");
			return "OK";
		 } catch (AuthenticationException e) {
           System.out.println("身份验证失败!");
			 System.out.println(e);
           return "NONO";
           // e.printStackTrace();  
		 } catch (javax.naming.CommunicationException e) {  
           System.out.println("AD域连接失败!");  
           return "NO";
		 } catch (Exception e) {  
           System.out.println("身份验证未知异常!");
			 System.out.println(e);
           return "NO";
		 }finally{
           if(null!=ctx){
               try {  
                   ctx.close();  
                   ctx=null;  
               } catch (Exception e) {  
                   e.printStackTrace();  
               }  
           }
       }
	 }

}
