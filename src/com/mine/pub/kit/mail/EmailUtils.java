package com.mine.pub.kit.mail;


import java.util.List;

import com.mine.pub.kit.StrKit;


/**
 * 邮件发送工具类
 * @author woody
 * @date 2015-6-12
 */
public class EmailUtils {
	

	/**
	 * 发送邮件
	 * @param smtp        邮件协议
	 * @param fromAddress 发送人地址
	 * @param fromPass    发送人密码
	 * @param toaddress   收件人地址
	 * @param subject     发送主题
	 * @param content     发送内容
	 * @throws Exception
	 */
	public static void send(String smtp,String fromAddress,String fromPass,String toAddress, String subject, String content) throws Exception{
		System.out.println("开始向" + toAddress + "发送邮件");
		EmailHandle emailHandle = new EmailHandle(smtp);
		emailHandle.setFrom(fromAddress);
		emailHandle.setNeedAuth(true);
		emailHandle.setSubject(subject);
		emailHandle.setBody(content);
		emailHandle.setTo(toAddress);
		emailHandle.setFrom(fromAddress);
		emailHandle.setNamePass(fromAddress, fromPass);
		emailHandle.sendEmail();
		System.out.println("邮件发送结束!");
	}
	
	/**
	 * 发送邮件
	 * @param smtp        邮件协议
	 * @param fromAddress 发送人地址
	 * @param fromPass    发送人密码
	 * @param toAddress   收件人地址
	 * @param ccAdress    抄送人地址
	 * @param subject     发送主题
	 * @param content     发送内容
	 * @throws Exception
	 */
	public static void send(String smtp,String fromAddress,String fromPass,String toAddress,String ccAdress, String subject, String content){
		try{
			System.out.println("开始向" + toAddress + "发送邮件");
			EmailHandle emailHandle = new EmailHandle(smtp);
			emailHandle.setFrom(fromAddress);
			emailHandle.setNeedAuth(true);
			emailHandle.setSubject(subject);
			emailHandle.setBody(content);
			emailHandle.setTo(toAddress);
			/**添加抄送**/
			if(StrKit.notBlank(ccAdress)){
				emailHandle.setCopyTo(ccAdress);
			}
			emailHandle.setFrom(fromAddress);
			emailHandle.setNamePass(fromAddress, fromPass);
			emailHandle.sendEmail();
			System.out.println("邮件发送结束!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送邮件
	 * @param smtp        邮件协议
	 * @param fromAddress 发送人地址
	 * @param fromPass    发送人密码
	 * @param toaddress   收件人地址
	 * @param subject     发送主题
	 * @param content     发送内容
	 * @throws Exception
	 */
	public static void send(String smtp,String fromAddress,String fromPass,String toAddress,String subject, String content,List<String> fileList){
		try{
			System.out.println("开始向" + toAddress + "发送邮件");
			EmailHandle emailHandle = new EmailHandle(smtp);
			emailHandle.setFrom(fromAddress);
			emailHandle.setNeedAuth(true);
			emailHandle.setSubject(subject);
			emailHandle.setBody(content);
			emailHandle.setTo(toAddress);
			emailHandle.setFrom(fromAddress);
			emailHandle.setNamePass(fromAddress, fromPass);
			/** 附件文件路径 **/
			for(String file : fileList){
				emailHandle.addFileAffix(file);
			}
			emailHandle.sendEmail();
			System.out.println("邮件发送结束!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送邮件
	 * @param smtp        邮件协议
	 * @param fromAddress 发送人地址
	 * @param fromPass    发送人密码
	 * @param toAddress   收件人地址
	 * @param ccAdress    抄送人地址
	 * @param subject     发送主题
	 * @param content     发送内容
	 * @throws Exception
	 */
	public static void send(String smtp,String fromAddress,String fromPass,String toAddress,String ccAdress,String subject, String content,List<String> fileList){
		try{
			System.out.println("开始向" + toAddress + "发送邮件");
			EmailHandle emailHandle = new EmailHandle(smtp);
			emailHandle.setFrom(fromAddress);
			emailHandle.setNeedAuth(true);
			emailHandle.setSubject(subject);
			emailHandle.setBody(content);
			emailHandle.setTo(toAddress);
			/**添加抄送**/
			if(StrKit.notBlank(ccAdress)){
				emailHandle.setCopyTo(ccAdress);
			}
			emailHandle.setFrom(fromAddress);
			emailHandle.setNamePass(fromAddress, fromPass);
			/** 附件文件路径 **/
			for(String file : fileList){
				emailHandle.addFileAffix(file);
			}
			emailHandle.sendEmail();
			System.out.println("邮件发送结束!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
