package com.spiralforge.udaan.util;

import com.spiralforge.udaan.constants.ApiConstant;

public class Utility {
	private Utility() {
	}

	public static Double getTotalPrice(Integer quantity, Double price) {
		return price * quantity;
	}

	public static Double calculateCharges(Double amount) {
		Double charge = (amount * (5.0f / 100.0f));
		return (Math.round(charge * 100.0) / 100.0);
	}

	public static Double calculateCharges(Double amount, Float percentage) {
		Double taxBenefitAmount = (amount * (percentage / ApiConstant.PERCENTAGE_DIVIDE_VALUE));
		return (Math.round(taxBenefitAmount * 100.0) / 100.0);
	}
	
	public static String getContent(String name, Double amount) {
		StringBuilder text = new StringBuilder("Dear "+name+", \n \n We have received your donation amount INR: "+amount);
		text.append("\n \n THANK YOU FOR YOUR DONATION THROUGH.");
		text.append("\n \n \n NOTE: Please don't reply to this mail.");
		text.append("\n \n Sincerely, \n Udaan Team");

		return text.toString();
	}

}
