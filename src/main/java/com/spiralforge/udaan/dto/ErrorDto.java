package com.spiralforge.udaan.dto;

import lombok.Data;

@Data
public class ErrorDto {
	private Integer statusCode;
	private String message;
}
