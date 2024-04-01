package com.productmodule.sprinboot.controller;


import com.productmodule.domain.exception.BusinessException;
import com.productmodule.domain.exception.Error;
import com.productmodule.sprinboot.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ProductExceptionController {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

		log.error("occurred exception ", exception);

		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		log.error("occurred exception ", exception);

		Map extensions = exception.getBindingResult().getAllErrors().stream()
				.filter(error -> error instanceof FieldError)
				.map(error -> (FieldError) error)
				.collect(Collectors.groupingBy(
						FieldError::getField,
						Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()) // 각 필드의 에러 메시지들을 리스트로 수집
				));

		var errorResponse = new ErrorResponse(
				Error.INVALID_REQUEST.getErrorCode(),
				Error.INVALID_REQUEST.getMessage(),
				LocalDateTime.now(),
				extensions
		);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {

		log.error("occurred exception ", exception);

		var errorResponse = new ErrorResponse(
				exception.getError().getErrorCode(),
				exception.getError().getMessage(),
				LocalDateTime.now(),
				exception.getExtensions()
		);

		return ResponseEntity.badRequest().body(errorResponse);
	}
}
