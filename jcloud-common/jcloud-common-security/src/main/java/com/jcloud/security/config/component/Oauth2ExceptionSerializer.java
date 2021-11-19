package com.jcloud.security.config.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jcloud.security.exception.Oauth2AuthenticationException;

import java.io.IOException;

/**
 * 自定义jackson 序列化Oauth2AuthenticationException
 * @author jiaxm
 * @date 2021/3/29
 */
public class Oauth2ExceptionSerializer extends StdSerializer<Oauth2AuthenticationException> {

    protected Oauth2ExceptionSerializer() {
        super(Oauth2AuthenticationException.class);
    }

    @Override
    public void serialize(Oauth2AuthenticationException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("code", value.getCode());
        gen.writeStringField("msg", value.getMessage());
        gen.writeObjectField("data", value.getData());
        gen.writeEndObject();
    }
}
