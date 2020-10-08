package com.eqms.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailService {
		
	final String SMTP_USER = "marektomczak5503@gmail.com";
	final String SMTP_USER_PASSWORD = "wroquczbjudmjwod";
	final String SMTP_HOST = "smtp.gmail.com";
	final String SMTP_PORT = "587";
	
	private String initialEmailAddress;
	private String destinationEmailAddress;
	private String subject;
	private String message;
	private String verificationToken;
	
	public EmailService(String destinationEmailAddress, String subject, String message, String verificationToken) {
		super();
		this.destinationEmailAddress = destinationEmailAddress;
		this.subject = subject;
		this.message = message;
		this.verificationToken = verificationToken;
	}
	
	public EmailService(String initialEmailAddress, String destinationEmailAddress, String subject, String message, String verificationToken) {
		super();
		this.initialEmailAddress = initialEmailAddress;
		this.destinationEmailAddress = destinationEmailAddress;
		this.subject = subject;
		this.message = message;
		this.verificationToken = verificationToken;
	}
	
	public Multipart createActivateAccountMessageBody() {
		
		Multipart multipart = new MimeMultipart("alternative");
		BodyPart messageBodyPart = new MimeBodyPart();
		
		// To activate your account, please open the following link
		String htmlMessage = "HTML message received <br> <br> "
				+ getMessage() + ": <br /> <br /> "
				+ "<a href= \"http://localhost:8080/EQMS/auth/registrationConfirm?token=" + getVerificationToken() + "\" >Activated link</a>";
	
		try {
			messageBodyPart.setContent(htmlMessage, "text/html");
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return multipart;
	}
	
	public Multipart createAskForAccessMessageBody() {
		
		Multipart multipart = new MimeMultipart("alternative");
		BodyPart messageBodyPart= new MimeBodyPart();
		
		// If you want grant access to this subject for ..., please open the following link
		String htmlMessage = "HTML message received <br> <br> "
				+ "Message from " + getInitialEmailAddress() + ": "
				+ getMessage() + "<br /> <br /> "
				+ "If you want grant access to this subject for " + getInitialEmailAddress() + ", please open the following link: <br /> <br /> "
				+ "<a href= \"http://localhost:8080/EQMS/auth/askForAccessConfirm?initial=" + getInitialEmailAddress() + "&token=" + getVerificationToken() + "\" >Activated link</a>";
	
		try {
			messageBodyPart.setContent(htmlMessage, "text/html");
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return multipart;
		
	}
	
	public void sendMessage() {
		
		Properties props = new Properties();
		props.put("mail.smtp.user", SMTP_USER);
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//props.put("mail.mime.charset", "utf-8");
		
		Session session = Session.getDefaultInstance(props, 
				new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTP_USER, SMTP_USER_PASSWORD);
				}
		});
		session.setDebug(true);
		
		MimeMessage msg = new MimeMessage(session);
		
		try {
			msg.setFrom(new InternetAddress(SMTP_USER, "EQMS"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(getDestinationEmailAddress(), "User"));	// adr.wojtasik@gmail.com    // kmiecioots@o2.pl    // lewmac@wp.pl
			msg.setSubject(getSubject(), "utf-8");	// Activate your account
			
			/*if(getInitialEmailAddress() != null) {
				msg.setContent(createAskForAccessMessageBody(), "text/plain; charset=\"utf-8\"");
			} else {
				msg.setContent(createActivateAccountMessageBody(), "text/plain; charset=utf-8");
			}*/
			
			
			// Create a message part to represent the body text 
			BodyPart messageBodyPart = new MimeBodyPart(); 
			if(getInitialEmailAddress() != null) {
				messageBodyPart.setContent(createAskForAccessMessageBody(), "text/html; charset=\"ISO-8859-1\"" );
			} else {
				messageBodyPart.setContent(createActivateAccountMessageBody(), "text/html; charset=\"ISO-8859-1\"" );
			}

			// Use a MimeMultipart as we need to handle the file attachments 
			Multipart multipart = new MimeMultipart(); 

			// Add the message body to the mime message 
			multipart.addBodyPart(messageBodyPart); 

			// Put all message parts in the message 
			msg.setContent(multipart);
			
			
			Transport transport = session.getTransport("smtps");
			transport.connect(SMTP_HOST, SMTP_USER, SMTP_USER_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getInitialEmailAddress() {
		return initialEmailAddress;
	}

	public void setInitialEmailAddress(String initialEmailAddress) {
		this.initialEmailAddress = initialEmailAddress;
	}
	
	public String getDestinationEmailAddress() {
		return destinationEmailAddress;
	}
	public void setDestinationEmailAddress(String destinationEmailAddress) {
		this.destinationEmailAddress = destinationEmailAddress;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

}
