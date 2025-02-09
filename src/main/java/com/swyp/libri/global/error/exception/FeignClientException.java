package com.swyp.libri.global.error.exception;

import com.swyp.libri.global.error.ErrorResponse;

public class FeignClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int status;
    private final String resultMessage;
    private ErrorResponse.ErrorData errorData;

    public FeignClientException(String resultMessage, ErrorResponse.ErrorData errorData) {
        this.resultMessage = resultMessage;
        this.errorData = errorData;
    }

    public FeignClientException(int status, String resultMessage) {
        this.status = status;
        this.resultMessage = resultMessage;
    }

    public int getStatus() {
        return status;
    }


    public ErrorResponse.ErrorData getErrorData() {
        return errorData;
    }
}
