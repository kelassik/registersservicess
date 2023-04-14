package com.digital.solution.generalservice.exception;

import com.digital.solution.generalservice.domain.dto.error.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

@SuppressWarnings("all")
public class ApiErrorRestException extends RestClientException {
    private static final long serialVersionUID = -2221694369979014760L;

    private ResponseEntity<ApiError> responseEntity;

    public ApiErrorRestException(String message) {
        super(message);
    }

    public ResponseEntity<ApiError> getResponseEntity() {
        return this.responseEntity;
    }

    public void setResponseEntity(ResponseEntity<ApiError> responseEntity) {
        this.responseEntity =  responseEntity;
    }
}
