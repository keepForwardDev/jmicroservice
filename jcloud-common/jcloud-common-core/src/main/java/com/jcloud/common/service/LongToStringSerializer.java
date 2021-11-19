package com.jcloud.common.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * js最大支持长度17位，解决精度丢失问题
 * @author jiaxm
 * @date 2021/8/12
 */
public class LongToStringSerializer extends StdSerializer<Long> {

    public LongToStringSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeObject(null);
        } else if (value.toString().length() > 16) {
            gen.writeString(value.toString());
        } else {
            gen.writeNumber(value);
        }
    }
}
