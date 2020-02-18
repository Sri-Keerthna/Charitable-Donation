package com.spiralforge.udaan.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.udaan.dto.PaymentRequestDto;
import com.spiralforge.udaan.dto.PaymentResponseDto;
import com.spiralforge.udaan.exception.DonationNotFoundException;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.exception.UserNotFoundException;
import com.spiralforge.udaan.service.UserService;

/**
 * @author Sri Keerthna.
 * @since 2020-02-14.
 */
@RestController
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RequestMapping("/users")
public class UserController {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-14. In this method user will give their details to make a
	 *        payment.
	 * @param paymentRequestDto has username,pan number,emailId and mobile number.
	 * @return success message is sent to user.
	 * @throws SchemeNotFoundException if no scheme found.
	 * @throws UserNotFoundException 
	 */
	@PostMapping
	public ResponseEntity<PaymentResponseDto> charitablePayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto)
			throws SchemeNotFoundException, UserNotFoundException {
		logger.info("Entered into charitablePayment method in user controller");
		PaymentResponseDto paymentResponseDto = userService.charitablePayment(paymentRequestDto);
		return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
	}

	/**
	 * @author Sujal.
	 * @since 2020-02-14. In this method user will download the donation detail
	 *        after payment.
	 * @param userId
	 * @return InputStreamResource to download pdf file
	 * @throws IOException
	 * @throws UserNotFoundException
	 * @throws DonationNotFoundException 
	 */
	@GetMapping(value = "{userId}/download", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadPDF(@PathVariable("userId") Long userId)
			throws UserNotFoundException, DonationNotFoundException {

		byte[] byteData = userService.download(userId);

		if (Objects.isNull(byteData) || byteData.length < 1) {
			return ResponseEntity.badRequest().build();
		} else {
			logger.info("inside download pdf method");
			ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		}
	}

	/**
	 * @author Sujal.
	 * @since 2020-02-14. In this method user will download the donation detail
	 *        after payment and send send the mail with pdf attachment.
	 * @param userId
	 * @return InputStreamResource to download pdf file
	 * @throws IOException
	 * @throws UserNotFoundException
	 * @throws DonationNotFoundException 
	 */
	@GetMapping(value = "{userId}/email", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> sendPDFInMail(@PathVariable("userId") Long userId)
			throws UserNotFoundException, DonationNotFoundException {

		byte[] byteData = userService.sendPDFInMail(userId);

		if (Objects.isNull(byteData) || byteData.length < 1) {
			return ResponseEntity.badRequest().build();
		} else {
			logger.info("inside download pdf and send PDF in Mail");
			ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=udaan.pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		}
	}
}
