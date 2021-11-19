package com.jcloud.common.util;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonUtils {

    public static ObjectMapper objectMapper;

    static {
        try {
            objectMapper = SpringUtil.getBean(ObjectMapper.class);
        } catch (Exception e) {
            objectMapper = new ObjectMapper();
        }
    }


    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] toJsonByte(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readObject(String value, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readObject(String value, TypeReference<T> typeReference) {
        if (!StringUtils.isEmpty(value)) {
            try {
                return objectMapper.readValue(value, typeReference);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
