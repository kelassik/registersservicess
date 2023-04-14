package com.digital.solution.generalservice.domain.dto.error;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.Serializable;

public class LowerCaseClassNameResolver extends TypeIdResolverBase implements Serializable {
    private static final long serialVersionUID = 74426745127471671L;

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }


    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        return TypeFactory.defaultInstance().constructType(ApiError.class);
    }
}
