package com.ordermodule.domain.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

	private final Error error;

	private Map<Object, Object> extensions;
	public BusinessException(Error error) {
		super(error.getMessage());
		this.error = error;
	}

	public BusinessException(Error error, Map<Object, Object> extensions) {
		super(error.getMessage());
		this.error = error;
		this.extensions = extensions;
	}

	public static BusinessException of(Error error) {
		return new BusinessException(error);
	}

	public static BusinessException of(Error error, Map<Object, Object> extensions) {
		return new BusinessException(error, extensions);
	}
}
