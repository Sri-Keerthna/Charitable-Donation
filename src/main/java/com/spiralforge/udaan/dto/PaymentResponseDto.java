package com.spiralforge.udaan.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sri Keerthna.
 * @since 2020-02-14.
 */
@Getter
@Setter
public class PaymentResponseDto {

	private String message;
	private Integer statusCode;
	private String userName;
	private String emailId;
	private Long mobileNumber;
	private String panNumber;
	private String creditCardNumber;
	private String schemeName;
	private Double schemeAmount;
	private Double taxBenefit;
	private Long userId;
}
