package com.productmodule.domain.exception;

import lombok.Getter;

@Getter
public enum Error {

	NOT_FOUND_ENTITY("1000", "리소스를 찾을 수 없습니다."),
	SOLDOUT_STOCK("1001", "재고가 부족합니다"),
	INVALID_REQUEST("9999", "잘못된 요청입니다.");


	private String errorCode;
	private String message;

	Error(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}
