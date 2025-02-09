package com.swyp.libri.global.common;

/**
 * 응답 메시지
 */
public enum ResultMessage {
	CODE_200("CODE_200"),//정상 처리
	CODE_201("CODE_201"),//Created
	CODE_204("CODE_204"),//No Content
	CODE_400("CODE_400"),//Parameter Error
	CODE_401("CODE_401"),//권한 없음
	CODE_403("CODE_403"),//인가 실패
	CODE_404("CODE_404"),//not found
	CODE_405("CODE_405"),//http method not allowed
	CODE_412("CODE_412"),//비즈니스 에러
	CODE_500("CODE_500");//예상치 못한 시스템 에러

	public static ResultMessage fromResultCode(ResultCode resultCode) {
		switch (resultCode) {
			case CODE_200:
				return CODE_200;
			case CODE_201:
				return CODE_201;
			case CODE_204:
				return CODE_204;
			case CODE_400:
				return CODE_400;
			case CODE_401:
				return CODE_401;
			case CODE_403:
				return CODE_403;
			case CODE_404:
				return CODE_404;
			case CODE_405:
				return CODE_405;
			case CODE_412:
				return CODE_412;
			case CODE_500:
				return CODE_500;
		}
		return null;
	}

	private final String value;

	ResultMessage(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
