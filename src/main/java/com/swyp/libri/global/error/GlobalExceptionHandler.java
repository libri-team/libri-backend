package com.swyp.libri.global.error;


import static com.swyp.libri.global.common.ResultCode.*;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.swyp.libri.global.common.ResultCode;
import com.swyp.libri.global.common.ResultMessage;
import com.swyp.libri.global.error.exception.BusinessException;
import com.swyp.libri.global.error.exception.CustomIllegalArgumentException;
import com.swyp.libri.global.error.exception.DataBaseException;
import com.swyp.libri.global.error.exception.ErrorCode;
import com.swyp.libri.global.error.exception.FeignClientException;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final ReloadableResourceBundleMessageSource messageSource;

	@ExceptionHandler(CustomIllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(CustomIllegalArgumentException e) {
		log.debug(null, e);
		var value = ErrorCode.ERR_INVALID_INPUT_VALUE.getValue();
		var message = e.getMessage();
		var response = ErrorResponse.of(
			getResultMessage(CODE_400),
			ErrorResponse.ErrorData.CodeWithMessageBuilder()
				.errorCode(value)
				.errorMessage(message)
				.build());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}


	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		log.debug(null, e);
		String errorMessage = messageSource.getMessage(e.getErrorCode().getValue(), e.getStringArgList(),
			LocaleContextHolder.getLocale());
		ErrorResponse response = ErrorResponse.of(
			getResultMessage(CODE_412),
			ErrorResponse.ErrorData.CodeWithMessageBuilder()
				.errorCode(e.getErrorCode().getValue())
				.errorMessage(StringUtils.hasText(errorMessage) ? errorMessage :
					messageSource.getMessage(ErrorCode.ERR_BUSINESS.getValue(), null, LocaleContextHolder.getLocale()))
				.build()
		);

		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
	}


	@ExceptionHandler(FeignClientException.class)
	protected ResponseEntity<ErrorResponse> handleReleaseOverdueUnavailableException(FeignClientException e) {
		log.debug(null, e);
		ErrorResponse response = ErrorResponse.of(
			messageSource.getMessage(ErrorCode.ERR_FEIGN_CLIENT.getValue(), null, LocaleContextHolder.getLocale()),
			ErrorResponse.ErrorData.CodeWithMessageBuilder()
				.errorCode(e.getErrorData().getErrorCode())
				.errorMessage(e.getErrorData().getErrorMessage())
				.build());
		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		final MissingServletRequestParameterException e) {
		log.debug(null, e);
		final ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorCode(ErrorCode.ERR_INVALID_INPUT_VALUE.getValue())
			.errorMessage(e.getMessage())
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_400), errorData);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
	 * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
	 * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.debug(null, e);
		final ErrorResponse response = ErrorResponse.of(
			getResultMessage(CODE_400),
			ErrorCode.ERR_INVALID_INPUT_VALUE.getValue(),
			e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
	 * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.debug(null, e);
		final ErrorResponse response = ErrorResponse.of(
			getResultMessage(CODE_400),
			ErrorCode.ERR_INVALID_INPUT_VALUE.getValue(),
			e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * enum type 일치하지 않아 binding 못할 경우 발생
	 * 주로 @RequestParam enum으로 binding 못했을 경우 발생
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		log.debug(null, e);
		final ErrorResponse response = ErrorResponse.of(
			getResultMessage(CODE_400),
			ErrorCode.ERR_INVALID_TYPE_VALUE.getValue(),
			e
		);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * "multipart/form-data" 요청 시 필수 @RequestPart binding 못했을 경우 발생
	 */
	@ExceptionHandler({MissingServletRequestPartException.class, HttpMediaTypeNotSupportedException.class})
	protected ResponseEntity<ErrorResponse> handleServletException(ServletException e) {
		log.debug(null, e);
		final ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorCode(ErrorCode.ERR_INVALID_INPUT_VALUE.getValue())
			.errorMessage(e.getMessage())
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_400), errorData);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Request body 매핑 실패 시 발생
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.debug(null, e);
		if (e.getCause().getCause() instanceof BusinessException) {
			BusinessException businessException = (BusinessException)e.getCause().getCause();

			String errorMessage = messageSource.getMessage(businessException.getErrorCode().getValue(),
				businessException.getStringArgList(),
				LocaleContextHolder.getLocale());

			ErrorResponse response = ErrorResponse.of(
				getResultMessage(CODE_412),
				ErrorResponse.ErrorData.CodeWithMessageBuilder()
					.errorCode(businessException.getErrorCode().getValue())
					.errorMessage(StringUtils.hasText(errorMessage) ? errorMessage :
						messageSource.getMessage(ErrorCode.ERR_BUSINESS.getValue(), null,
							LocaleContextHolder.getLocale()))
					.build()
			);
			return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
		}
		final ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorCode(ErrorCode.ERR_INVALID_INPUT_VALUE.getValue())
			.errorMessage(e.getMessage())
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_400), errorData);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		log.debug(null, e);
		ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorCode(ErrorCode.ERR_METHOD_NOT_ALLOWED.getValue())
			.errorMessage(StringUtils.hasText(e.getMessage()) ? e.getMessage() :
				messageSource.getMessage(ErrorCode.ERR_METHOD_NOT_ALLOWED.getValue(), null,
					LocaleContextHolder.getLocale()))
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_405), errorData);
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * 500
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error(null, e);
		ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorCode(ErrorCode.ERR_INTERNAL_SERVER_ERROR.getValue())
			.errorMessage(StringUtils.hasText(e.getMessage()) ? e.getMessage()
				: messageSource.getMessage(ErrorCode.ERR_INTERNAL_SERVER_ERROR.getValue(), null,
				LocaleContextHolder.getLocale()))
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_500), errorData);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * DB 에러 처리
	 */
	@ExceptionHandler({DataBaseException.class})
	public ResponseEntity<ErrorResponse> handleDataBaseException(final DataBaseException e) {
		log.error(null, e);
		String errorCode = e.getErrorCode().getValue();
		String message = messageSource.getMessage(errorCode, null, LocaleContextHolder.getLocale());
		ErrorResponse.ErrorData errorData = ErrorResponse.ErrorData.CodeWithMessageBuilder()
			.errorMessage(message)
			.errorCode(errorCode)
			.build();
		final ErrorResponse response = ErrorResponse.of(getResultMessage(CODE_412), errorData);
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
	}

	private String getResultMessage(ResultCode resultCode) {
		return messageSource.getMessage(ResultMessage.fromResultCode(resultCode).getValue(), null,
			LocaleContextHolder.getLocale());
	}

}
