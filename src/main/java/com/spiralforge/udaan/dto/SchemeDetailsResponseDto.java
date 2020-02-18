package com.spiralforge.udaan.dto;

import lombok.Data;

@Data
public class SchemeDetailsResponseDto {
	private String schemeName;
	private Double schemeAmount;
	private String description;
	private Float taxBenefit;
	private Double taxBenefitAmount;

}
