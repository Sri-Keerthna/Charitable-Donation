package com.spiralforge.udaan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import com.spiralforge.udaan.constants.ApiConstant;
import com.spiralforge.udaan.constants.ApplicationConstants;
import com.spiralforge.udaan.dto.PaymentRequestDto;
import com.spiralforge.udaan.dto.PaymentResponseDto;
import com.spiralforge.udaan.entity.Scheme;
import com.spiralforge.udaan.entity.User;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.exception.UserNotFoundException;
import com.spiralforge.udaan.repository.DonationRepository;
import com.spiralforge.udaan.repository.SchemeRepository;
import com.spiralforge.udaan.repository.UserRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	SchemeRepository schemeRepository;
	
	@Mock
	DonationRepository donationRepository;
	
	PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
	PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
	Scheme scheme = new Scheme();
	User user = new User();
	Double amount = 20D;
	
	@Before
	public void setUp() {
		scheme.setSchemeId(1L);
		scheme.setSchemeAmount(1000D);
		scheme.setTaxBenefit(2f);
		paymentRequestDto.setSchemeId(1L);
		user.setUserId(1L);
		user.setUserName("Sri");
		userService.saveDonationData(user, scheme);
		BeanUtils.copyProperties(paymentRequestDto, user);
		BeanUtils.copyProperties(paymentRequestDto, paymentResponseDto);
		BeanUtils.copyProperties(scheme, paymentResponseDto);
		paymentResponseDto.setTaxBenefit(amount);
		paymentResponseDto.setUserId(user.getUserId());
		user.setUserStatus(ApplicationConstants.ACTIVE_STATUS);
		paymentResponseDto.setMessage(ApiConstant.PAYMENT_SUCCESS);
		paymentResponseDto.setStatusCode(ApiConstant.SUCCESS_CODE);
	}
	
	@Test
	public void testCharitablePaymentPositive() throws SchemeNotFoundException, UserNotFoundException {
		Mockito.when(schemeRepository.findById(paymentRequestDto.getSchemeId())).thenReturn(Optional.of(scheme));
		PaymentResponseDto result=userService.charitablePayment(paymentRequestDto);
		assertEquals(200, result.getStatusCode());
	}
	
	@Test(expected = SchemeNotFoundException.class)
	public void testCharitablePaymentNegative() throws SchemeNotFoundException, UserNotFoundException {
		Mockito.when(schemeRepository.findById(2L)).thenReturn(Optional.of(scheme));
		userService.charitablePayment(paymentRequestDto);
	}

}
