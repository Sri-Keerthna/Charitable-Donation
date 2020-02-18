package com.spiralforge.udaan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.udaan.dto.SchemeDetailsResponseDto;
import com.spiralforge.udaan.dto.SchemeResponseDto;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.service.SchemeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RequestMapping("/schemes")
public class SchemeController {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SchemeController.class);

	@Autowired
	SchemeService schemeService;

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
	@GetMapping("/{schemeId}")
	public ResponseEntity<SchemeDetailsResponseDto> getSchemeDetails(@PathVariable(name = "schemeId") Long schemeId)
			throws SchemeNotFoundException {
		log.info("Getting details of a particular scheme by using its id");
		SchemeDetailsResponseDto schemeDetailsResponseDto = schemeService.getSchemeDetails(schemeId);
		return new ResponseEntity<>(schemeDetailsResponseDto, HttpStatus.OK);
	}

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-14. In this method scheme list is fetched from database.
	 * @return list of scheme list.
	 * @throws SchemeNotFoundException if there is no scheme.
	 */

	@GetMapping
	public ResponseEntity<List<SchemeResponseDto>> getSchemeList() throws SchemeNotFoundException {
		logger.info("Entered into getSchemeList method in scheme controller");
		List<SchemeResponseDto> schemeResponseDto = schemeService.getSchemeList();
		return new ResponseEntity<>(schemeResponseDto, HttpStatus.OK);
	}
}
