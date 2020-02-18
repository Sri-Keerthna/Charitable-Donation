package com.spiralforge.udaan.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.udaan.constants.ApiConstant;
import com.spiralforge.udaan.dto.LoginRequestDto;
import com.spiralforge.udaan.dto.LoginResponseDto;
import com.spiralforge.udaan.dto.SchemeList;
import com.spiralforge.udaan.dto.StatisticsResponseDto;
import com.spiralforge.udaan.exception.AdminNotFoundException;
import com.spiralforge.udaan.exception.SchemeListEmptyException;
import com.spiralforge.udaan.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class AdminController {
	@Autowired
	AdminService adminService;

	/**
	 * @author Muthu
	 * 
	 *         Method is used for login for the admin
	 * 
	 * @param loginRequestDto which takes the mobile number and password as
	 *                        parameters
	 * @return LoginResponseDto which has name,id,message and status code as a
	 *         response
	 * @throws AdminNotFoundException is called when the entered credentials is
	 *                                invalid
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> checkLogin(@Valid @RequestBody LoginRequestDto loginRequestDto)
			throws AdminNotFoundException {
		log.info("For checking whether the person is staff or a customer");
		LoginResponseDto loginResponse = adminService.checkLogin(loginRequestDto);
		log.info(ApiConstant.LOGIN_SUCCESS);
		loginResponse.setStatusCode(ApiConstant.SUCCESS_CODE);
		loginResponse.setMessage(ApiConstant.LOGIN_SUCCESS);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	/**
	 * @author Muthu
	 * 
	 *         Method is used for getting the count which is used for giving a
	 *         pictorial view for admin
	 * 
	 * @return StatisticsResponseDto which includes the scheme name and the number
	 *         of donors for each scheme
	 * @throws SchemeListEmptyException
	 */
	@GetMapping
	public ResponseEntity<StatisticsResponseDto> getStatisticsDetails() throws SchemeListEmptyException {
		List<SchemeList> schemeList = adminService.getStatisticsDetails();
		StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
		if (schemeList.isEmpty()) {
			log.info(ApiConstant.SCHEMELIST_EMPTY_MESSAGE);
			statisticsResponseDto.setMessage(ApiConstant.SCHEMELIST_EMPTY_MESSAGE);
			return new ResponseEntity<>(statisticsResponseDto, HttpStatus.NOT_FOUND);
		}
		log.info(ApiConstant.SCHEMELIST_MESSAGE);
		statisticsResponseDto.setSchemeList(schemeList);
		statisticsResponseDto.setMessage(ApiConstant.SCHEMELIST_MESSAGE);
		return new ResponseEntity<>(statisticsResponseDto, HttpStatus.OK);
	}
}
