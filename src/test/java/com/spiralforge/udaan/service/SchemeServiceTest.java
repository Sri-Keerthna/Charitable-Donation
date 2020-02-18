package com.spiralforge.udaan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import com.spiralforge.udaan.constants.ApplicationConstants;
import com.spiralforge.udaan.dto.SchemeDetailsResponseDto;
import com.spiralforge.udaan.dto.SchemeResponseDto;
import com.spiralforge.udaan.entity.Scheme;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.repository.SchemeRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SchemeServiceTest {
	Scheme schemeDetails = null;
	SchemeDetailsResponseDto schemeDetailsResponseDto = null;

	@InjectMocks
	SchemeServiceImpl schemeService;

	@Mock
	SchemeRepository schemeRepository;

	List<SchemeResponseDto> resultList = new ArrayList<>();
	List<Scheme> schemeList = new ArrayList<>();
	Scheme scheme = new Scheme();
	SchemeResponseDto schemeResponse = new SchemeResponseDto();

	@Before
	public void setUp() {
		scheme.setSchemeId(1L);
		scheme.setSchemeStatus(ApplicationConstants.ACTIVE_STATUS);
		schemeList.add(scheme);
		BeanUtils.copyProperties(scheme, schemeResponse);
		resultList.add(schemeResponse);

		schemeDetails = new Scheme();
		schemeDetails.setSchemeAmount(200.00);
		schemeDetails.setTaxBenefit(10.0f);
		schemeDetails.setSchemeId(1L);
		schemeDetails.setDescription("abc");
		schemeDetails.setSchemeStatus("active");
		schemeDetails.setSchemeName("Agri");
		schemeDetailsResponseDto = new SchemeDetailsResponseDto();
		schemeDetailsResponseDto.setSchemeName("Agri");
	}

	@Test
	public void testGetSchemeList() throws SchemeNotFoundException {
		Mockito.when(schemeRepository.findBySchemeStatus(ApplicationConstants.ACTIVE_STATUS)).thenReturn(schemeList);
		List<SchemeResponseDto> result = schemeService.getSchemeList();
		assertEquals(1, result.size());
	}

	@Test(expected = SchemeNotFoundException.class)
	public void testGetSchemeListNegative() throws SchemeNotFoundException {
		Mockito.when(schemeRepository.findBySchemeStatus("INACTIVE")).thenReturn(schemeList);
		schemeService.getSchemeList();
	}

	@Test
	public void testGetSchemeDetailsPositive() throws SchemeNotFoundException {
		Long schemeId = 1L;
		Mockito.when(schemeRepository.findById(schemeId)).thenReturn(Optional.of(schemeDetails));
		SchemeDetailsResponseDto response = schemeService.getSchemeDetails(schemeId);
		assertEquals(schemeDetailsResponseDto.getSchemeName(), response.getSchemeName());
	}

	@Test(expected = SchemeNotFoundException.class)
	public void testGetSchemeDetailsNegative() throws SchemeNotFoundException {
		Long schemeId = 1L;
		Mockito.when(schemeRepository.findById(schemeId)).thenReturn(Optional.ofNullable(null));
		schemeService.getSchemeDetails(2L);
	}

}
