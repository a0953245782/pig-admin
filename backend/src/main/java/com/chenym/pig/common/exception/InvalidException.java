package com.chenym.pig.common.exception;

import com.chenym.pig.common.constant.CommonConstants;
import com.chenym.pig.common.serializer.PigAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PigAuth2ExceptionSerializer.class)
public class InvalidException extends PigAuth2Exception {
    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    @Override
    public int getHttpErrorCode() {
        return CommonConstants.INVALID_CODE;
    }
}
