package com.spiralforge.udaan.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.udaan.dto.LoginRequestDto;
import com.spiralforge.udaan.dto.LoginResponseDto;
import com.spiralforge.udaan.dto.SchemeList;
import com.spiralforge.udaan.dto.StatisticsResponseDto;
import com.spiralforge.udaan.exception.AdminNotFoundException;
import com.spiralforge.udaan.exception.SchemeListEmptyException;
import com.spiralforge.udaan.service.AdminService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdminControllerTest {
	LoginRequestDto loginRequestDto = null;
	LoginResponseDto loginResponseDto = null;

	List<SchemeList> schemeList = null;
	List<SchemeList> schemeList1 = null;
	SchemeList schemeDetails = null;

	@Mock
	AdminService adminService;

	@InjectMocks
	AdminController adminController;

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("hello123");

		loginResponseDto = new LoginResponseDto();
		loginResponseDto.setAdminId(1L);
		loginResponseDto.setAdminName("Muthu");

		schemeList = new ArrayList<>();
		schemeDetails = new SchemeList();
		schemeDetails.setCount(2);
		schemeDetails.setSchemeName("ChildCare");
		schemeList.add(schemeDetails);
		
		schemeList1 = new ArrayList<>();
	}

	@Test
	public void testCheckLoginPositive() throws AdminNotFoundException {
		Mockito.when(adminService.checkLogin(loginRequestDto)).thenReturn(loginResponseDto);
		ResponseEntity<LoginResponseDto> response = adminController.checkLogin(loginRequestDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetStatisticsDetailsPositive() throws SchemeListEmptyException {
		Mockito.when(adminService.getStatisticsDetails()).thenReturn(schemeList);
		ResponseEntity<StatisticsResponseDto> response = adminController.getStatisticsDetails();
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetStatisticsDetailsNegative() throws SchemeListEmptyException {
		Mockito.when(adminService.getStatisticsDetails()).thenReturn(schemeList1);
		ResponseEntity<StatisticsResponseDto> response = adminController.getStatisticsDetails();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
