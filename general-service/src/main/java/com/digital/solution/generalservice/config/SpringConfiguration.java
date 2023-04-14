package com.digital.solution.generalservice.config;

import com.digital.solution.generalservice.domain.dto.error.ApiError;
import com.digital.solution.generalservice.exception.ApiErrorRestErrorHandler;
import com.digital.solution.generalservice.utils.ObjectMapperUtils;
import com.digital.solution.generalservice.utils.ReadAndWriteFileUtils;
import com.digital.solution.generalservice.utils.logging.ControllerRequestResponseLogger;
import com.digital.solution.generalservice.utils.logging.RestTemplateRequestResponseLogger;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableSwagger2
@SuppressWarnings("all")
@Import(ControllerRequestResponseLogger.class)
public class SpringConfiguration {

    @Value(value = "${rest.client.connect.timeout:2}")
    private long restClientConnectTimeout;

    @Value(value = "${enabled.swagger:true}")
    private boolean enabledSwagger;

    @Value(value = "${rest.client.read.timeout:5}")
    private long restClientReadTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = restTemplateBuilder
                .setReadTimeout(Duration.ofSeconds(restClientReadTimeout))
                .setConnectTimeout(Duration.ofSeconds(restClientConnectTimeout))
                .build();
        restTemplate.setRequestFactory(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateRequestResponseLogger()));
        restTemplate.setErrorHandler(new ApiErrorRestErrorHandler<>(ApiError.class));
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .enable(enabledSwagger);
    }

    @Bean
    public ObjectMapperUtils objectMapperUtils() {
        return new ObjectMapperUtils();
    }

    @Bean
    public ReadAndWriteFileUtils readAndWriteFileUtils() {
        return new ReadAndWriteFileUtils();
    }
}
