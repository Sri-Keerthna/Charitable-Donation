package com.spiralforge.udaan.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sri Keerthna.
 * @since 2020-02-14.
 */
@Getter
@Setter
public class PaymentRequestDto {

	@NotNull
	private Long schemeId;
	@Size(min = 2, max = 10, message = "UserName should not be less than 2")
	private String userName;
	@Email(message = "Email should be valid")
	private String emailId;
	private Long mobileNumber;
	@Size(min = 10, max = 10, message = "Pan Number should be 10 digits")
	private String panNumber;
	private String creditCardNumber;
}
