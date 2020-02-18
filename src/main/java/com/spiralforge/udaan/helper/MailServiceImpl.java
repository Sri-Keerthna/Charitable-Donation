package com.spiralforge.udaan.helper;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @author Sujal
 * @since 2020-02-14.
 */
@EnableAsync
@Service
public class MailServiceImpl implements MailService {

	Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String fromEmailId;

	/**
	 * @author Sujal This method is used to send the email
	 * 
	 * @param toMailId
	 * @param fromMailId
	 * @param subject
	 * @param text
	 */
	@Async
	@Override
	public void sendMail(String toMailId, String subject, String text) {
		try {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(toMailId);
			mail.setFrom(fromEmailId);
			mail.setSubject(subject);
			mail.setText(text);
			javaMailSender.send(mail);
		} catch (Exception exception) {
			logger.error("Unable to send the email");
		}
	}

	/**
	 * @author Sujal This method is used to send the email with attachment
	 * 
	 * @param toMailId
	 * @param fromMailId
	 * @param subject
	 * @param text
	 */
	@Async
	@Override
	public void sendMail(String toMailId, String subject, String content, byte[] attachement) {
		try {
			logger.info("inside send mail");
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(fromEmailId);
			mimeMessageHelper.setTo(toMailId);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content, true);

			ByteArrayDataSource byteArrayDataSource = null;

			if (attachement.length > 0) {

				byteArrayDataSource = new ByteArrayDataSource(attachement, "application/pdf");
				mimeMessageHelper.addAttachment(byteArrayDataSource.getName() + "udaan.pdf", byteArrayDataSource);

			}
			javaMailSender.send(mimeMessage);
			logger.info("sent mail");

		} catch (Exception e) {
			logger.info("Mail not sent");
		}
	}
}
