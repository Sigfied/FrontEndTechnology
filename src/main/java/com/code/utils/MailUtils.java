package com.code.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author lenovo
 */
@Slf4j
@Component
public class MailUtils {
    //发送第二封验证码邮件
    /**设置发送邮件的主机  smtp.qq.com*/
    private final static String HOST = "smtp.qq.com";
    /**1.创建连接对象，连接到邮箱服务器*/
    private static final Properties PROPS = System.getProperties();

    public static void sendMail(String to,String vo) throws Exception{
        //Properties 用来设置服务器地址，主机名 。。 可以省略
        //设置邮件服务器
        PROPS.setProperty("mail.smtp.host", HOST);
        PROPS.put("mail.smtp.auth", "true");
        //SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        PROPS.put("mail.smtp.ssl.enable","true");
        PROPS.put("mail.smtp.ssl.socketFactory", sf);
        //PROPS：用来设置服务器地址，主机名；Authenticator：认证信息
        Session session = Session.getDefaultInstance(PROPS,new Authenticator() {
            @Override
            //通过密码认证信息
            protected PasswordAuthentication getPasswordAuthentication() {
                //new PasswordAuthentication(用户名, password);
                //这个用户名密码就可以登录到邮箱服务器了,用它给别人发送邮件
                return new PasswordAuthentication("2034689681@qq.com","mjolwzyowjwrbdgi");
            }
        });
        try {
            Message message = new MimeMessage(session);
            //2.1设置发件人：
            message.setFrom(new InternetAddress("2034689681@qq.com"));
            //2.2设置收件人 这个TO就是收件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            //2.3邮件的主题
            message.setSubject("来自 Pointer 验证码邮件");
            //2.4设置邮件的正文 第一个参数是邮件的正文内容 第二个参数是：是文本还是html的连接
            message.setContent("<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                    "  <table cellpadding=\"0\" align=\"center\"\n" +
                    "         style=\"width: 800px;height: 100%; margin: 0 auto; text-align: left; position: relative; border-radius: 5px;font-size: 14px; font-family:微软雅黑, 黑体,serif; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0 0 5px; border-collapse: collapse; background: #fff initial initial initial initial;\">\n" +
                    "    <tbody>\n" +
                    "    <tr>\n" +
                    "      <th valign=\"middle\"\n" +
                    "          style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: RGB(148,0,211); background-color: RGB(148,0,211); border-radius: 5px 5px 0 0;\">\n" +
                    "        <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">欢迎使用Pointer</font>\n" +
                    "      </th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "      <td style=\"word-break:break-all\">\n" +
                    "        <div style=\"padding:25px 35px 40px; background-color:#fff;opacity:0.8;\">\n" +
                    "\n" +
                    "          <h2 style=\"margin: 5px 0; \">\n" +
                    "            <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                    "              <font style=\"line-height: 22px; \" size=\"4\">\n" +
                    "                尊敬的用户：</font>\n" +
                    "            </font>\n" +
                    "          </h2>\n" +
                    "          <!-- 中文 -->\n" +
                    "          <p>您好！感谢您使用Pointer，您的账号正在进行邮箱验证，验证码为：<br>\n" +
                    "            <span style=\"margin-left: 250px; font-family: 'Comic Sans MS', cursive; font-weight: 700; color: #ff8c00; font-size: 25px; text-align: center;\" >"+ vo+ "</span>\n" +
                    "            <br>请尽快填写验证码完成验证且妥善保存您的验证码！</p><br>\n" +
                    "          <!-- 英文 -->\n" +
                    "          <h2 style=\"margin: 5px 0; \">\n" +
                    "                        <span style=\"line-height: 20px;  color: #333333; \">\n" +
                    "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                    "                                Dear user:</font>\n" +
                    "                        </span>\n" +
                    "          </h2>\n" +
                    "          <p>Hello! Thanks for using Pointer, your account is being authenticated by email, the\n" +
                    "            verification code is:<br>\n" +
                    "            <span style=\"margin-left: 250px; font-family: 'Comic Sans MS', cursive; font-weight: 700; color: #ff8c00; font-size: 25px; \"\">"+vo +"</span>\n" +
                    "            <br>Please fill in the verification code as soon as\n" +
                    "            possible and keep your verification code!</p>\n" +
                    "          <div style=\"width:100%;margin:0 auto;\">\n" +
                    "            <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                    "              <p>Pointer团队</p>\n" +
                    "              <p>联系我们：2034689681@qq,com</p>\n" +
                    "              <br>\n" +
                    "              <p>此为系统邮件，请勿回复<br>\n" +
                    "                Please do not reply to this system email\n" +
                    "              </p>\n" +
                    "           \n" +
                    "            </div>\n" +
                    "          </div>\n" +
                    "        </div>\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    </tbody>\n" +
                    "  </table>\n" +
                    "</div>", "text/html;charset=UTF-8");
            //3.发送一封激活邮件
            Transport.send(message);
        }catch(MessagingException mex){
            mex.printStackTrace();
        }

    }

}
