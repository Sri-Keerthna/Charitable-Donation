package com.spiralforge.udaan.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Scheme {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long schemeId;
	private String schemeName;
	private Double schemeAmount;
	private String description;
	private Float taxBenefit;
	private String schemeStatus;

}
