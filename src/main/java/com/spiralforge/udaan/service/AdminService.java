package com.spiralforge.udaan.service;

import java.util.List;

import javax.validation.Valid;

import com.spiralforge.udaan.dto.LoginRequestDto;
import com.spiralforge.udaan.dto.LoginResponseDto;
import com.spiralforge.udaan.dto.SchemeList;
import com.spiralforge.udaan.exception.AdminNotFoundException;
import com.spiralforge.udaan.exception.SchemeListEmptyException;

public interface AdminService {

	LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws AdminNotFoundException;

	List<SchemeList> getStatisticsDetails() throws SchemeListEmptyException;

}
