package com.onejava.modelMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@UtilityClass
public class ModelMapper {
    private static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    @NonNull
    public static <T> String convert(@Nullable final T object) {
        if (Objects.isNull(object)) {
            return "";
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error("Object to JSON Conversion failed. Object:{} Message:{}",
                    object, e.getCause());
            throw new RuntimeException(e);
        }
    }


    @Nullable
    public static <T> T convert(@NonNull final String json,
                                @NonNull final Class<T> classType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, classType);

        } catch (IOException e) {
            log.error("JSON to Object Conversion failed. Data:{} Message:{}", json, e.getCause());
            throw new RuntimeException(e);
        }
    }


    @Nullable
    public static <T> T convert(@NonNull final Object object,
                                @NonNull final TypeReference<T> typeReference) {
        try {
            return objectMapper.convertValue(object, typeReference);
        } catch (IllegalArgumentException iae) {
            log.error("Object conversion failed for object: {}", object, iae);
            return null;
        }
    }


    public static <T> T convert(Object object, Class<T> eventDataClass) {
        return objectMapper.convertValue(object, eventDataClass);
    }
}
