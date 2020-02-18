package com.spiralforge.udaan.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.udaan.dto.PaymentRequestDto;
import com.spiralforge.udaan.dto.PaymentResponseDto;
import com.spiralforge.udaan.exception.DonationNotFoundException;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.exception.UserNotFoundException;
import com.spiralforge.udaan.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserService userService;

	PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
	PaymentRequestDto paymentRequestDto = new PaymentRequestDto();

	@Test
	public void testCharitablePayment() throws SchemeNotFoundException, UserNotFoundException {
		Mockito.when(userService.charitablePayment(paymentRequestDto)).thenReturn(paymentResponseDto);
		ResponseEntity<PaymentResponseDto> result = userController.charitablePayment(paymentRequestDto);
		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testDownloadPositive()
			throws SchemeNotFoundException, UserNotFoundException, DonationNotFoundException, IOException {
		Long userId = 1L;
		byte[] expectedRead = new byte[] { (byte) 129, (byte) 130, (byte) 131 };

		Mockito.when(userService.download(userId)).thenReturn(expectedRead);
		InputStreamResource result = userController.downloadPDF(userId).getBody();
		assertNotNull(result);
	}

	@Test
	public void testDownloadNegative()
			throws SchemeNotFoundException, UserNotFoundException, DonationNotFoundException, IOException {
		Long userId = 1L;
		Mockito.when(userService.download(userId)).thenReturn(Mockito.any(byte[].class));
		ResponseEntity<InputStreamResource> result = userController.downloadPDF(userId);
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

	}

	@Test
	public void testDownloadPDFPositive()
			throws SchemeNotFoundException, UserNotFoundException, DonationNotFoundException, IOException {
		Long userId = 1L;
		byte[] expectedRead = new byte[] { (byte) 129, (byte) 130, (byte) 131 };

		Mockito.when(userService.sendPDFInMail(userId)).thenReturn(expectedRead);
		InputStreamResource result = userController.sendPDFInMail(userId).getBody();
		assertNotNull(result);

	}

	@Test
	public void testDownloadPDFNegative()
			throws SchemeNotFoundException, UserNotFoundException, DonationNotFoundException, IOException {
		Long userId = 1L;
		Mockito.when(userService.sendPDFInMail(userId)).thenReturn(Mockito.any(byte[].class));
		ResponseEntity<InputStreamResource> result = userController.sendPDFInMail(userId);
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

	}

}
