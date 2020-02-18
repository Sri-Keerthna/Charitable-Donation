package com.spiralforge.udaan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.udaan.constants.ApiConstant;
import com.spiralforge.udaan.constants.ApplicationConstants;
import com.spiralforge.udaan.controller.SchemeController;
import com.spiralforge.udaan.dto.SchemeDetailsResponseDto;
import com.spiralforge.udaan.dto.SchemeResponseDto;
import com.spiralforge.udaan.entity.Scheme;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.repository.SchemeRepository;
import com.spiralforge.udaan.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SchemeServiceImpl implements SchemeService {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SchemeController.class);

	@Autowired
	SchemeRepository schemeRepository;

	/**
	 * @author Muthu
	 * 
	 *         Method is used to get the details of the particular scheme
	 * 
	 * @param schemeId Value for identifying a particular scheme
	 * @return SchemeDetailsResponseDto which has the scheme details that includes
	 *         name,description,tax amount,tax benefit
	 * @throws SchemeNotFoundException is called when the scheme is not found
	 */
	@Override
	public SchemeDetailsResponseDto getSchemeDetails(Long schemeId) throws SchemeNotFoundException {
		SchemeDetailsResponseDto schemeDetailsResponseDto = new SchemeDetailsResponseDto();
		Optional<Scheme> scheme = schemeRepository.findById(schemeId);
		if (!(scheme.isPresent())) {
			log.error(ApiConstant.SCHEME_NOTFOUND_MESSAGE);
			throw new SchemeNotFoundException(ApiConstant.SCHEME_NOTFOUND_MESSAGE);
		}
		Double taxBenefitAmount = Utility.calculateCharges(scheme.get().getSchemeAmount(), scheme.get().getTaxBenefit());
		schemeDetailsResponseDto.setTaxBenefitAmount(taxBenefitAmount);
		BeanUtils.copyProperties(scheme.get(), schemeDetailsResponseDto);
		return schemeDetailsResponseDto;
	}

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-14. In this method scheme list is fetched from database.
	 * @return list of scheme list.
	 * @throws SchemeNotFoundException if there is no scheme.
	 */
	@Override
	public List<SchemeResponseDto> getSchemeList() throws SchemeNotFoundException {
		List<Scheme> schemeList = schemeRepository.findBySchemeStatus(ApplicationConstants.ACTIVE_STATUS);
		if (schemeList.isEmpty()) {
			logger.error("No schemes available");
			throw new SchemeNotFoundException(ApplicationConstants.SCHEME_NOT_FOUND_EXCEPTION);
		}
		List<SchemeResponseDto> responseList = new ArrayList<>();
		schemeList.forEach(scheme -> {
			SchemeResponseDto schemeResponse = new SchemeResponseDto();
			BeanUtils.copyProperties(scheme, schemeResponse);
			responseList.add(schemeResponse);
		});
		logger.info("Got the list of scheme");
		return responseList;
	}
}
