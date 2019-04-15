package com.chenym.pig.common.exception;

import com.chenym.pig.common.serializer.PigAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = PigAuth2ExceptionSerializer.class)
public class UnauthorizedException extends PigAuth2Exception {
    public UnauthorizedException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "unauthorized";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
