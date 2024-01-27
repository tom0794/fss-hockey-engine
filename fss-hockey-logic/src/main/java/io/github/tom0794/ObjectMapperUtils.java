package io.github.tom0794;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class ObjectMapperUtils {
    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapObject = new ObjectMapper();
        mapObject.findAndRegisterModules();
        mapObject.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapObject.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapObject.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapObject.setDateFormat(dateFormat);
        return mapObject;
    }
}
