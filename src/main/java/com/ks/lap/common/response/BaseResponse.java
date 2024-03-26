package com.ks.lap.common.response;

import com.ks.lap.common.response.code.BaseResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseResponse {

	public static <T> ResponseEntity<BaseResult<T>> ok() {
		return getResponseEntity(null, BaseResultCode.SUCCESS, null);
	}

	public static <T> ResponseEntity<BaseResult<T>> ok(T t) {
		return getResponseEntity(t, BaseResultCode.SUCCESS, null);
	}

	public static <T> ResponseEntity<BaseResult<T>> fail(BaseResultCode baseResultCode) {
		return getResponseEntity(null, baseResultCode);
	}

	public static <T> ResponseEntity<BaseResult<T>> getResponseEntity(
			T t, BaseResultCode resultCode) {
		return getResponseEntity(t, resultCode, null);
	}

	public static <T> ResponseEntity<BaseResult<T>> getResponseEntity(
			BaseResultCode resultCode, List<ObjectError> objectErrors) {
		Map<String, String> fieldErrors = new HashMap<>();

		objectErrors.forEach(objectError -> {
			FieldError fieldError = (FieldError) objectError;

			String key = String.format("%s_error", fieldError.getField());
			fieldErrors.put(key, fieldError.getDefaultMessage());
		});
		return getResponseEntity(null, resultCode, fieldErrors);
	}

	public static <T> ResponseEntity<BaseResult<T>> getResponseEntity(
			T t, BaseResultCode resultCode, Map<String, String> fieldErrors) {
		BaseResult<T> result = new BaseResult<>(t, resultCode);
		
		// httpStatus 셋팅
		HttpStatus httpStatus = HttpStatus.OK;
		if ( BaseResultCode.SUCCESS_CREATE  == resultCode ) {
			httpStatus = HttpStatus.CREATED;
		} else if ( BaseResultCode.COMMON_INVALID_PARAMS == resultCode ) {
			httpStatus = HttpStatus.BAD_REQUEST;
			result.setFieldErrors(fieldErrors);
		} else if ( BaseResultCode.COMMON_INTERNAL_SERVER_ERROR == resultCode ) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(result, httpStatus);
	}

}
