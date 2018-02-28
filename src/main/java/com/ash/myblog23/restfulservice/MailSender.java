package com.ash.myblog23.restfulservice;

import java.util.Properties;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author AsH
 */
@Named("mailSender")
public class MailSender {

    
    public MailSender(){
        emailSend();
    }
    
    public static void emailSend() {

        final String username = "mercurysoftware701@gmail.com";
        final String password = "b3foreiforg3t";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("dessmon7@gmail.com"));
            msg.setSubject("Testing Email Sender API");
            msg.setText("Dear, Gorcc ------------------," + "\n\n This is my first email sended through API!");
            Transport.send(msg);
            System.out.println("DONE!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
