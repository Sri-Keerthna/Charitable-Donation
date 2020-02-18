package com.spiralforge.udaan.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.spiralforge.udaan.constants.ApiConstant;
import com.spiralforge.udaan.dto.ErrorDto;
import com.spiralforge.udaan.dto.ExceptionResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * @description Handle NullPointer Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleNullPointerExceptions(NullPointerException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.NO_ELEMENT_FOUND, defaultMessage);
	}

	/**
	 * @description Handle MethodArgumentNotValid exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponseDto handleValidationError(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		FieldError fieldError = bindingResult.getFieldError();
		String defaultMessage = fieldError.getDefaultMessage();
		return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED, defaultMessage);
	}

	/**
	 * @description Handle Runtime Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleAllRuntimeExceptions(RuntimeException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
	}

	/**
	 * @description Handle all Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleAllExceptions(Exception exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
	}
	
	/**
	 * @description Handle all validation Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleValidationFailedException(UserNotFoundException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR, defaultMessage);
	}
	
	@ExceptionHandler(SchemeNotFoundException.class)
	public ResponseEntity<ErrorDto> schemeNotFoundException() {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(ApiConstant.SCHEME_NOTFOUND_MESSAGE);
		errorDto.setStatusCode(ApiConstant.FAILURE_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	
	@ExceptionHandler(AdminNotFoundException.class)
	public ResponseEntity<ErrorDto> adminNotFoundException() {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(ApiConstant.ADMIN_NOTFOUND_MESSAGE);
		errorDto.setStatusCode(ApiConstant.FAILURE_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	
	@ExceptionHandler(SchemeListEmptyException.class)
	public ResponseEntity<ErrorDto> schemeListEmptyException() {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(ApiConstant.SCHEMELIST_EMPTY_MESSAGE);
		errorDto.setStatusCode(ApiConstant.FAILURE_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
}
