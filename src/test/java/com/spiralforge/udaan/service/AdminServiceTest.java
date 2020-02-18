package com.spiralforge.udaan.service;

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

import com.spiralforge.udaan.dto.LoginRequestDto;
import com.spiralforge.udaan.dto.LoginResponseDto;
import com.spiralforge.udaan.dto.SchemeList;
import com.spiralforge.udaan.entity.Admin;
import com.spiralforge.udaan.entity.Donation;
import com.spiralforge.udaan.entity.Scheme;
import com.spiralforge.udaan.exception.AdminNotFoundException;
import com.spiralforge.udaan.exception.SchemeListEmptyException;
import com.spiralforge.udaan.repository.AdminRepository;
import com.spiralforge.udaan.repository.DonationRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdminServiceTest {
	Admin admin = null;
	LoginRequestDto loginRequestDto = null;
	LoginResponseDto loginResponseDto = null;
	LoginRequestDto loginRequestDto1 = null;

	List<SchemeList> schemeList = null;
	List<SchemeList> schemeList1 = null;
	SchemeList schemeDetails = null;
	List<Donation> donationList = null;
	List<Donation> donationList1 = null;
	Scheme scheme = null;
	Donation donation = null;

	@Mock
	AdminRepository adminRepository;

	@InjectMocks
	AdminServiceImpl adminServiceImpl;

	@Mock
	DonationRepository donationRepository;

	@Before
	public void before() {
		admin = new Admin();
		admin.setAdminId(1L);
		admin.setMobileNumber(9876543210L);
		admin.setAdminName("Muthu");
		admin.setPassword("hello123");

		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("hello123");

		loginRequestDto1 = new LoginRequestDto();
		loginRequestDto1.setMobileNumber(9876543210L);
		loginRequestDto1.setPassword("hello1234");

		loginResponseDto = new LoginResponseDto();
		loginResponseDto.setAdminId(1L);
		loginResponseDto.setAdminName("Muthu");

		scheme = new Scheme();
		scheme.setSchemeId(1L);
		donation = new Donation();
		donationList = new ArrayList<>();
		donation.setScheme(scheme);
		donationList.add(donation);
		schemeList = new ArrayList<>();
		schemeDetails = new SchemeList();
		schemeDetails.setCount(2);
		schemeDetails.setSchemeName("ChildCare");
		schemeList.add(schemeDetails);
		
		donationList1=new ArrayList<>();
	}

	@Test
	public void testCheckLoginPositive() throws AdminNotFoundException {
		Mockito.when(adminRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword())).thenReturn(admin);
		LoginResponseDto response = adminServiceImpl.checkLogin(loginRequestDto);
		assertEquals(admin.getAdminName(), response.getAdminName());
	}

	@Test(expected = AdminNotFoundException.class)
	public void testCheckLoginException() throws AdminNotFoundException {
		Mockito.when(adminRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword())).thenReturn(admin);
		adminServiceImpl.checkLogin(loginRequestDto1);
	}

	@Test
	public void testGetStatisticsDetailsPositive() throws SchemeListEmptyException {
		Mockito.when(donationRepository.findAll()).thenReturn(donationList);
		List<SchemeList> response = adminServiceImpl.getStatisticsDetails();
		assertEquals(schemeList.size(), response.size());
	}

	@Test(expected = SchemeListEmptyException.class)
	public void testGetStatisticsDetailsNegative() throws SchemeListEmptyException {
		Mockito.when(donationRepository.findAll()).thenReturn(donationList1);
		adminServiceImpl.getStatisticsDetails();
	}
}
