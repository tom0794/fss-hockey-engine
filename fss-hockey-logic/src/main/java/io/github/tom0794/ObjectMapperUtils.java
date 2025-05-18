package io.github.tom0794;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

@Configuration
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

    @Bean
    @Qualifier("dbMapper")
    public static ObjectMapper createDatabaseMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(mapper.getSerializationConfig().withView(Views.DatabaseInsert.class));
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean
    @Primary
    @Qualifier("responseMapper")
    public static ObjectMapper createResponseMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setConfig(mapper.getSerializationConfig().withView(Views.PublicResponse.class));
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFormat);
        return mapper;
    }
}
