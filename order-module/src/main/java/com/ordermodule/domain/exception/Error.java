package com.ordermodule.domain.exception;

import lombok.Getter;

@Getter
public enum Error {

	NOT_FOUND_ENTITY("1000", "리소스를 찾을 수 없습니다."),
	INVALID_REQUEST("1001", "잘못된 요청입니다."),
	INTERNAL_ERROR("9999", "알 수 없는 에러입니다.");


	private String errorCode;
	private String message;

	Error(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}
