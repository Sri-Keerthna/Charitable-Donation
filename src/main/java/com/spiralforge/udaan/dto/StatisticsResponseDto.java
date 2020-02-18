package com.spiralforge.udaan.dto;

import java.util.List;

import lombok.Data;

@Data
public class StatisticsResponseDto {
	private List<SchemeList> schemeList;
	private String message;
}
