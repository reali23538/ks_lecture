package com.ks.lap.common.response.code;

import lombok.Getter;

@Getter
public enum BaseResultCode {

	SUCCESS( "성공 하였습니다.", "처리 성공"),
	FAIL("실패 하였습니다.", "처리 실패"),

	SUCCESS_CREATE( "등록 되었습니다.", "등록 성공"),

	// 공통
	COMMON_INTERNAL_SERVER_ERROR( "처리 실패 하였습니다.", "서버 내부 에러"),
	COMMON_PARSE_EXCEPTION("처리 실패 하였습니다.", "파싱 에러"),
	COMMON_INVALID_PARAMS( "등록 값을 확인해주세요.", "유효하지않은 파라미터"),

	// 강연
	NO_APPLY_LECTURE("신청한 강연이 없습니다.", "신청한 강연이 없습니다."),
	SOLD_OUT_LECTURE("매진된 강연입니다.", "매진된 강연입니다."),

	// 신청
	APPLIED_LECTURE("이미 신청한 강연입니다.", "이미 신청한 강연입니다."),
	NOT_APPLIED_LECTURE("신청하지않은 강연입니다.", "신청하지않은 강연입니다.")
	;

	private String message; // 결과 메시지

	private String developerMessage; // 개발자 확인 메시지

	BaseResultCode(String message, String developerMessage) {
		this.message = message;
		this.developerMessage = developerMessage;
	}

}
