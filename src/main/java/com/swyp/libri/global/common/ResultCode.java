package com.swyp.libri.global.common;

/**
 * 응답 코드
 */
public enum ResultCode {
	CODE_200(200),//정상 처리
	CODE_201(201),//Created
	CODE_204(204),// No Content
	CODE_400(400),//Parameter Error
	CODE_401(401),//권한 없음
	CODE_403(403),//인가 실패
	CODE_404(404),//not found

	CODE_405(405),// 허락 되지 않은 Http 요청 메소드
	CODE_412(412),//비즈니스 에러
	CODE_500(500);//예상치 못한 시스템 에러

	private final int value;

	ResultCode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
