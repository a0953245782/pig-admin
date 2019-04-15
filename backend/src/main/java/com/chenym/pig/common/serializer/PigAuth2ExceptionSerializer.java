package com.chenym.pig.common.serializer;

import com.chenym.pig.common.constant.CommonConstants;
import com.chenym.pig.common.exception.PigAuth2Exception;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PigAuth2ExceptionSerializer extends StdSerializer<PigAuth2Exception> {
    public PigAuth2ExceptionSerializer() {
        super(PigAuth2Exception.class);
    }

    @Override
    public void serialize(PigAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("status", CommonConstants.FAIL);
        jsonGenerator.writeStringField("msg", e.getMessage());
        jsonGenerator.writeStringField("data", e.getErrorCode());
        jsonGenerator.writeEndObject();
    }
}
