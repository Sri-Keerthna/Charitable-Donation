package com.spiralforge.udaan.helper;

/**
 * @author Sujal
 * @since 2020-02-14.
 */
public interface MailService {
	
	/**
	 * @author Sujal
	 *  	This method is used to send the email
	 * 
	 * @param toMailId
	 * @param fromMailId
	 * @param subject
	 * @param text
	 */
	void sendMail(String toMailId, String subject, String text);

	void sendMail(String toMailId, String subject, String text, byte[] bis);

}
