package com.stayconnected.utils.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailService {

	Properties props = new Properties();

	@PostConstruct
	public void setProps()
	{
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}


	public void sendMail(String message)
	{

		String[] data = System.getenv("EMAIL_NOTIFICATION_DATA").split(",");

		String from = data[0];
		String to = data[1];
		String password = data[2];
		String subject = data[3];
		send(from,password,to,subject,message);
	}

	private void send(String from, String password, String to, String sub, String msg) {
		//Get properties object

		//get Session
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, password);
					}
				});
		//compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			//message.setText(msg);
			message.setContent(	msg,"text/html");
			//send message
			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}    