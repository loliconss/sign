package com.sign.tool;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class Email {

    static Logger log= LoggerFactory.getLogger(Email.class);

    public static void Send(String data, String url) {
        // 跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 设置邮件服务器主机名
        p.setProperty("mail.host", "smtp.qq.com");// 指定邮件服务器，默认端口 25
        // 发送服务器需要身份验证
        p.setProperty("mail.smtp.auth", "true");// 要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");
        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);
        // 开启debug调试，以便在控制台查看
        // session.setDebug(true);也可以这样设置
        // p.setProperty("mail.debug", "true");
        // 创建session
        Session session = Session.getDefaultInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名可以用QQ账号也可以用邮箱的别名
                PasswordAuthentication pa = new PasswordAuthentication(Param.sendEmail, Param.authorization);
                // 后面的字符是授权码，用qq密码不行！！
                return pa;
            }
        });
        try {
            // 声明一个Message对象(代表一封邮件),从session中创建
            MimeMessage msg = new MimeMessage(session);
            // 邮件信息封装
            // 1发件人
            msg.setFrom(new InternetAddress(Param.sendEmail));
            // 2收件人
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(Param.acceptEmail));
            // 3邮件内容:主题、内容
            String subject = "脚本运行通知";
            String encodedSubject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset("gb2312"), null);
            msg.setSubject(encodedSubject);
            StringBuilder sbd = new StringBuilder();
            sbd.append(data+"：<br/>当前时间："+ Tool.getTime()+"<br/>");
            sbd.append("<font color='red'><a href='"+url+"' target='_blank'");
            sbd.append(">前往网站查看</a></font><br/>");
            sbd.append("或者点击下面链接进入网站:<br/>");
            sbd.append(url+" <br/>");
            sbd.append("这是一封自动发送的邮件；脚本写于2020-12-05 9:14:00");
            msg.setContent(sbd.toString(), "text/html;charset=utf-8");
            // 发送动作
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
            log.error("发送邮件时异常 AddressException");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送邮件时异常 MessagingException");
        } catch (UnsupportedEncodingException e) {
            log.error("发送邮件时异常 UnsupportedEncodingException");
            e.printStackTrace();
        }
    }


}
