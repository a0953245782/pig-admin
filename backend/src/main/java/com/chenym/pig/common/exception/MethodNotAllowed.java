package com.chenym.pig.common.exception;

import com.chenym.pig.common.serializer.PigAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = PigAuth2ExceptionSerializer.class)
public class MethodNotAllowed extends PigAuth2Exception {
    public MethodNotAllowed(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "method_not_allowed";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.METHOD_NOT_ALLOWED.value();
    }
}
