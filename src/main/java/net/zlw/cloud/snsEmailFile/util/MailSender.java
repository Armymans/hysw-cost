package net.zlw.cloud.snsEmailFile.util;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class MailSender {
	
	/**
	 * 发送简单邮件
	 * @param mailInfo
	 * @return
	 */
	public static boolean sendTextMail(MailInfo mailInfo) {
		ExchangeMailUtil mailUtil = new ExchangeMailUtil("https://mail.huayanwater.com/EWS/exchange.asmx",
                "CaiGou", "Hy123456");
		String toStr = mailInfo.getToAddress();
		String[] to = new String[]{toStr};
		try {
			mailUtil.send(mailInfo.getSubject(), to, null, mailInfo.getContent(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
//	public static boolean sendTextMail(MailInfo mailInfo) {
//		// 判断是否需要身份认证
//		BidAuthenticator authenticator = null;
//		Properties pro = mailInfo.getProperties();
//		if (mailInfo.isValidate()) {
//			// 如果需要身份认证，则创建一个密码验证器
//			authenticator = new BidAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
//		}
//		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
//		try {
//			// 根据session创建一个邮件消息
//			Message mailMessage = new MimeMessage(sendMailSession);
//			// 创建邮件发送者地址
//			Address from = new InternetAddress(mailInfo.getFromAddress());
//			// 设置邮件消息的发送者
//			mailMessage.setFrom(from);
//			// 创建邮件的接收者地址，并设置到邮件消息中
//			Address to = new InternetAddress(mailInfo.getToAddress());
//			mailMessage.setRecipient(Message.RecipientType.TO, to);
//			// 设置邮件消息的主题
//			mailMessage.setSubject(mailInfo.getSubject());
//			// 设置邮件消息发送的时间
//			mailMessage.setSentDate(new Date());
//			// 设置邮件消息的主要内容
//			String mailContent = mailInfo.getContent();
//			mailMessage.setText(mailContent);
//			// 发送邮件
//			Transport.send(mailMessage);
//			return true;
//		} catch (MessagingException ex) {
//			ex.printStackTrace();
//		}
//		return false;
//	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
//	public static boolean sendHtmlMail(MailInfo mailInfo) {
//		/*// 判断是否需要身份认证
//		BidAuthenticator authenticator = null;
//		Properties pro = mailInfo.getProperties();
//		// 如果需要身份认证，则创建一个密码验证器
//		if (mailInfo.isValidate()) {
//			authenticator = new BidAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
//		}
//		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);*/
//	try {
//			// 根据session创建一个邮件消息
//			/*Message mailMessage = new MimeMessage(sendMailSession);
//			// 创建邮件发送者地址
//			Address from = new InternetAddress(mailInfo.getFromAddress());
//			// 设置邮件消息的发送者
//			mailMessage.setFrom(from);
//			// 创建邮件的接收者地址，并设置到邮件消息中
//			//Address to = new InternetAddress(mailInfo.getToAddress());
//			// Message.RecipientType.TO属性表示接收者的类型为TO
//			InternetAddress[] to = InternetAddress.parse(mailInfo.getToAddress());
//			mailMessage.setRecipients(Message.RecipientType.TO, to);
//			// 设置邮件消息的主题
//			mailMessage.setSubject(mailInfo.getSubject());
//			// 设置邮件消息发送的时间
//			mailMessage.setSentDate(new Date());
//			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
//			Multipart mainPart = new MimeMultipart();
//			// 创建一个包含HTML内容的MimeBodyPart
//			BodyPart html = new MimeBodyPart();
//			// 设置HTML内容
//			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
//			mainPart.addBodyPart(html);
//			// 将MiniMultipart对象设置为邮件内容
//			mailMessage.setContent(mainPart);
//			// 发送邮件
//			Transport.send(mailMessage);*/
//			
//			
//		 Properties properties = new Properties();
//         properties.put("mail.transport.protocol", "smtp");// 连接协议
//         properties.put("mail.smtp.host", "smtp.exmail.qq.com");// 主机名
//         properties.put("mail.smtp.port", 465);// 端口号
//         properties.put("mail.smtp.auth", "true");
//         properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
//         properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
//        // 得到回话对象
//         Session session = Session.getInstance(properties);
//         // 获取邮件对象
//         Message message = new MimeMessage(session);
//         // 设置发件人邮箱地址
//         message.setFrom(new InternetAddress(mailInfo.getFromAddress()));
//         // 设置收件人邮箱地址 
//         message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));//一个收件人
//         // 设置邮件标题
//         message.setSubject(mailInfo.getSubject());
//         // 设置邮件内容
//         Multipart mainPart = new MimeMultipart();
//		 // 创建一个包含HTML内容的MimeBodyPart
//		 BodyPart html = new MimeBodyPart();
//		 // 设置HTML内容
//		 html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
//		 mainPart.addBodyPart(html);
//		 message.setContent(mainPart);
//         // 得到邮差对象
//         Transport transport = session.getTransport();
//         // 连接自己的邮箱账户
//         transport.connect(mailInfo.getFromAddress(), mailInfo.getPassword());// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
//         // 发送邮件
//         transport.sendMessage(message, message.getAllRecipients());
//         transport.close();
//         
//		 return true;
//		} catch (MessagingException ex) {
//			ex.printStackTrace();
//		}
//		return false;
//	}
	
	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailInfo mailInfo) {
		ExchangeMailUtil mailUtil = new ExchangeMailUtil("https://mail.huayanwater.com/EWS/exchange.asmx","CaiGou", "Hy123456");
		String toStr = mailInfo.getToAddress();
		String[] to = new String[]{toStr};
		try {
			mailUtil.send(mailInfo.getSubject(), to, null, mailInfo.getContent(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
