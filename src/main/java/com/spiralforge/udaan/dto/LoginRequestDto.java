package com.spiralforge.udaan.dto;

import javax.validation.constraints.NotBlank;

import com.spiralforge.udaan.constants.ApiConstant;

import lombok.Data;

@Data
public class LoginRequestDto {
	//@Size(min = 10, max = 10, message = ApiConstant.INVALID_MOBILENUMBER_MESSAGE)
	private Long mobileNumber;
	@NotBlank(message = ApiConstant.EMPTY_CUSTOMERINPUT_MESSAGE)
	private String password;
}
