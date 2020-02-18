package com.spiralforge.udaan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.udaan.constants.ApiConstant;
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
import com.spiralforge.udaan.repository.SchemeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	SchemeRepository schemeRepository;

	@Autowired
	DonationRepository donationRepository;

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
	@Override
	public LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws AdminNotFoundException {
		log.info("For checking whether the credentials are valid or not");
		Admin admin = adminRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword());
		if (Objects.isNull(admin)) {
			log.error(ApiConstant.ADMIN_NOTFOUND_MESSAGE);
			throw new AdminNotFoundException(ApiConstant.ADMIN_NOTFOUND_MESSAGE);
		}
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		BeanUtils.copyProperties(admin, loginResponseDto);
		log.info("Admin details are being set as a response");
		return loginResponseDto;
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
	@Override
	public List<SchemeList> getStatisticsDetails() throws SchemeListEmptyException {
		List<SchemeList> schemeList = new ArrayList<>();
		List<Donation> scheme = donationRepository.findAll();
		if (scheme.isEmpty()) {
			log.error(ApiConstant.SCHEMELIST_EMPTY_MESSAGE);
			throw new SchemeListEmptyException(ApiConstant.SCHEMELIST_EMPTY_MESSAGE);
		}
		Map<Scheme, Integer> schemeDetails = scheme.stream()
				.collect(Collectors.groupingBy(Donation::getScheme, Collectors
						.collectingAndThen(Collectors.mapping(Donation::getScheme, Collectors.toList()), List::size)));
		schemeDetails.forEach((schemeName, count) -> {
			SchemeList schemeDetail = new SchemeList();
			schemeDetail.setSchemeName(schemeName.getSchemeName());
			schemeDetail.setCount(count);
			schemeList.add(schemeDetail);
		});
		log.info("Return's the count for each scheme");
		return schemeList;
	}
}
