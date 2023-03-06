package com.authorization.service.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author hjl
 * @version 1.0
 * @description 反序列字段
 * @date 2022/11/11 20:48
 */
public class CustomAuthorityDeserializer extends StdSerializer {

    protected CustomAuthorityDeserializer(Class t) {
        super(t);
    }

    protected CustomAuthorityDeserializer(JavaType type) {
        super(type);
    }

    protected CustomAuthorityDeserializer(Class t, boolean dummy) {
        super(t, dummy);
    }

    protected CustomAuthorityDeserializer(StdSerializer src) {
        super(src);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    }
}
