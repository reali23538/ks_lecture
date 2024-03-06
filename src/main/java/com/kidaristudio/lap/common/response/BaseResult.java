package com.kidaristudio.lap.common.response;

import com.kidaristudio.lap.common.response.code.BaseResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseResult<T> {

	@Schema(description = "결과 코드", example = "SUCCESS")
	private String code;

	private T contents;

	@Schema(description = "결과 메시지", example = "성공 하였습니다.")
	private String message;

	@Schema(description = "개발자 확인 메시지", example = "처리 성공")
	private String developerMessage;

	@Schema(description = "필드 에러 (등록시)")
	private Map<String, String> fieldErrors = new HashMap<>();

	public BaseResult(T t, BaseResultCode baseResultCode) {
		this.contents = t;
		this.setBaseResult(baseResultCode);
	}
	
	public void setBaseResult(BaseResultCode baseResultCode) {
		this.code = baseResultCode.name();
		this.message = baseResultCode.getMessage();
		this.developerMessage = baseResultCode.getDeveloperMessage();
	}

}