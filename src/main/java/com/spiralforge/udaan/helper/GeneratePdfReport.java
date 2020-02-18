package com.spiralforge.udaan.helper;

import com.spiralforge.udaan.entity.Donation;
import com.spiralforge.udaan.entity.User;

/**
 * @author Sujal
 * @since 2020-02-14.
 */
public interface GeneratePdfReport {

	byte[] generatePdf(User user, Donation donation);

}
