package com.swyp.libri.global.config.feign;


import com.swyp.libri.global.error.ErrorResponse;
import com.swyp.libri.global.error.exception.FeignClientException;
import com.swyp.libri.global.util.JsonUtils;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public FeignClientException decode(final String methodKey, Response response) {
        String message = "Null Response Body.";
        if (response.body() != null) {
            ErrorResponse errorResponse = JsonUtils.readErrorResponse(response.body().toString());
            return new FeignClientException(errorResponse.getResultMessage(), errorResponse.getErrorData());
        }
        return new FeignClientException(response.status(), message);
    }
}
