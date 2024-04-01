package com.ordermodule.springboot.dto;

import com.ordermodule.domain.exception.Error;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public record ErrorResponse(
		String errorCode,
		String message,
		LocalDateTime timestamp,
		Map<Object, Object> extensions

) {

	public static ErrorResponse unknownError(){
		return new ErrorResponse(
				Error.UNKNOWN_ERROR.getErrorCode(),
				Error.UNKNOWN_ERROR.getMessage(),
				LocalDateTime.now(),
				Collections.EMPTY_MAP
		);
	}
}
