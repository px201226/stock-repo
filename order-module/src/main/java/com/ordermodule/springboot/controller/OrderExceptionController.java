package com.ordermodule.springboot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermodule.domain.exception.BusinessException;
import com.ordermodule.domain.exception.Error;
import com.ordermodule.domain.exception.NetworkException;
import com.ordermodule.springboot.dto.ErrorResponse;
import com.sun.net.httpserver.HttpServer;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class OrderExceptionController {

	private final ObjectMapper objectMapper;

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException exception) {
		log.error("occurred exception", exception);

		if (exception.getCause() instanceof SocketTimeoutException) {
			var errorResponse = new ErrorResponse(
					Error.TIMEOUT_ERROR.getErrorCode(),
					Error.TIMEOUT_ERROR.getMessage(),
					LocalDateTime.now(),
					Collections.EMPTY_MAP
			);

			return ResponseEntity.internalServerError().body(errorResponse);
		}

		return ResponseEntity.internalServerError().body(ErrorResponse.unknownError());
	}

	@ExceptionHandler(NetworkException.class)
	public ResponseEntity<Object> handleNetworkException(NetworkException exception) throws JsonProcessingException {

		log.error("occurred exception ", exception);

		if (exception.getCause() instanceof HttpClientErrorException || exception.getCause() instanceof HttpServerErrorException) {
			var httpException = (HttpStatusCodeException) exception.getCause();

			return ResponseEntity.status(httpException.getStatusCode())
					.body(objectMapper.readValue(httpException.getResponseBodyAsString(), Map.class));
		}

		return ResponseEntity.internalServerError().body(ErrorResponse.unknownError());
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
