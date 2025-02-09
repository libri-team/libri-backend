package com.swyp.libri.global.error;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"resultMessage", "errorData"})
public class ErrorResponse {

	@JsonProperty("resultMessage")
	private String resultMessage;

	@JsonProperty("errorData")
	private ErrorData errorData;

	private ErrorResponse(String resultMessage, ErrorData errorData) {
		this.resultMessage = resultMessage;
		this.errorData = errorData;
	}

	public static ErrorResponse of(String errorCode, ErrorData errorData) {
		return new ErrorResponse(errorCode, errorData);
	}
	public static ErrorResponse of(final String resultMessage, final String errorCode,
		final BindingResult bindingResult) {
		ErrorData errorData = ErrorData.CodeWithFieldErrorsBuilder()
			.errorCode(errorCode)
			.fieldErrors(FieldError.of(bindingResult))
			.build();
		return new ErrorResponse(resultMessage, errorData);
	}

	public static ErrorResponse of(String resultMessage, String errorCode,
		MethodArgumentTypeMismatchException e) {
		final String value = e.getValue() == null ? "" : e.getValue().toString();
		final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
		ErrorData errorData = ErrorData.CodeWithFieldErrorsBuilder()
			.errorCode(errorCode)
			.fieldErrors(errors)
			.build();
		return new ErrorResponse(resultMessage, errorData);
	}


	@Getter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@NoArgsConstructor
	@JsonPropertyOrder({"errorCode", "errorMessage", "fieldErrors", "permissionType"})
	public static class ErrorData {

		@JsonProperty("errorCode")
		private String errorCode;

		@JsonProperty("errorMessage")
		private String errorMessage;

		@JsonProperty("permissionType")
		private String permissionType;

		@JsonProperty("redirectItemId")
		private Long redirectItemId;

		private List<FieldError> fieldErrors;

		@Builder(builderClassName = "CodeWithMessageBuilder", builderMethodName = "CodeWithMessageBuilder")
		public ErrorData(String errorCode, String errorMessage, String permissionType, Long redirectItemId) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
			this.permissionType = permissionType;
			this.redirectItemId = redirectItemId;
		}

		@Builder(builderClassName = "CodeWithFieldErrorsBuilder", builderMethodName = "CodeWithFieldErrorsBuilder")
		public ErrorData(String errorCode, List<FieldError> fieldErrors) {
			this.errorCode = errorCode;
			this.fieldErrors = fieldErrors;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class FieldError {
		private String field;
		private String value;
		private String reason;

		private FieldError(final String field, final String value, final String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<FieldError> of(final String field, final String value, final String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		private static List<FieldError> of(final BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}