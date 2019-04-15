package com.chenym.pig.common.exception;

import com.chenym.pig.common.serializer.PigAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = PigAuth2ExceptionSerializer.class)
public class PigAuth2Exception extends OAuth2Exception {

    @Getter
    private String errorCode;

    public PigAuth2Exception(String msg) {
        super(msg);
    }

    public PigAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
