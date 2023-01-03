package com.org.Report;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

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

import com.org.CoreUtils.Log;
import com.org.resourceUtils.ResourceReader;

public class EmailConfig {
	// email properties
	final String host = ResourceReader.getEmail("HOST");
	final String userName = ResourceReader.getEmail("USER_NAME");
	final String passWord = ResourceReader.getEmail("PASSWORD");
	final String from = ResourceReader.getEmail("FROM");
	final String to = ResourceReader.getEmail("TO");
	final String subject = ResourceReader.getEmail("SUBJECT_LINE");
	final String PROJECT_NAME = ResourceReader.getTestLinkSettings("PROJECT_NAME");
	final String PLAN_NAME = ResourceReader.getTestLinkSettings("PLAN_NAME");
	// final String BUILD_NAME = ResourceReader.getTestLinkSettings("BUILD_NAME");
	final String srcPathReports = System.getProperty("user.dir") + "\\reports\\";

	public EmailConfig(Date startDate, Date endDate, int totalTest, int passTest, int failTest, String totalTime) {
		sendEmail(startDate, endDate, totalTest, passTest, failTest, totalTime);
	}

	public void sendEmail(Date startDate, Date endDate, int totalTest, int passTest, int failTest, String totalTime) {
		// Get system properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// Get the Session object
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, passWord);
			}
		});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			Multipart multipart = new MimeMultipart();
			MimeBodyPart attachmentPart = new MimeBodyPart();
			MimeBodyPart textPart = new MimeBodyPart();

			try {
				File f = new File(srcPathReports + "abercrombie.com _Automation_Report.html");
				attachmentPart.attachFile(f);
				textPart.setContent(
						"<html><body>Hi Team, <br><br>Automated Test Execution is Completed , here are the details <br><br><b>Application under test : </b>"
								+ PROJECT_NAME + " <br><b>Type of testing : </b>" + PLAN_NAME
								+ " <br><br><br><table  style=\"width:65%\" cellpadding=\"1\" border \"1px solid white\" border-radius=\"10px\"  border-style=\"outset\"><tr bgcolor=#5F9EA0><th colspan=\"4\">Test Execution Report</th></tr><tr bgcolor=#D5D8DC align=\"left\"><th>Execution Start Time </th><th>"
								+ startDate + "</th><tr bgcolor=#D5D8DC align=\"left\"><th>Execution End Time </th><th>"
								+ endDate
								+ " </th></tr><tr bgcolor=#D5D8DC align=\"left\"><th>Total Execution Time</th><th>"
								+ totalTime + "</th><tr bgcolor=#D5D8DC align=\"left\"><th>Total Test Cases</th><th>"
								+ totalTest
								+ "</th></tr><tr bgcolor=#D5D8DC align=\"left\"><th>Total Cases Passed</th><th>"
								+ passTest + "</th><tr bgcolor=#D5D8DC align=\"left\"><th>Test Case Failed</th><th>"
								+ failTest
								+ "</th></tr></table><br><br> <br>Please find the attachment for detailed report <br><br><br><b>Regards, <br>Team TechVates <br><b></body></html>",
						"text/html; charset=utf-8");
				multipart.addBodyPart(textPart);
				multipart.addBodyPart(attachmentPart);
			} catch (IOException e) {
				e.printStackTrace();
			}
			message.setContent(multipart);
			Log.info("Email Sending");
			Transport.send(message);
			Log.info("Sent message successfull to: " + to);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
