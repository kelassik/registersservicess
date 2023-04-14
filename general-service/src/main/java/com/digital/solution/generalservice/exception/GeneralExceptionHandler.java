package com.digital.solution.generalservice.exception;

import com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant;
import com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant;
import com.digital.solution.generalservice.domain.dto.error.ApiError;
import com.digital.solution.generalservice.domain.dto.error.ErrorCodeResponse;
import com.digital.solution.generalservice.service.GeneralService;
import com.digital.solution.generalservice.utils.ReadAndWriteFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INTERNAL_SERVER_ERROR;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_UNHANDLED;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Order
@RestControllerAdvice
@Slf4j
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    @SuppressWarnings("all")
    private ReadAndWriteFileUtils readAndWriteFileUtils;

    @Autowired
    @SuppressWarnings("all")
    private GeneralService generalService;

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> uncaughtException(Exception e) {
        log.error("Exception happened and UNCAUGHT, message [{}]", e.toString(), e);
        return buildResponseEntity(constructApiError(SOURCE_SYSTEM_CLICKS, ERROR_CODE_UNHANDLED, HttpStatus.BAD_REQUEST));
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleExceptionInternal(@NotNull Exception e, @Nullable Object body, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", e, 0);
            return buildResponseEntity(constructApiError(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {GenericException.class })
    public ResponseEntity<Object> genericException(GenericException e, WebRequest request) {
        log.info("handling GenericException... {}:{}", e.getSourceSystem().getSourceSystem(), e.getErrorCode().getErrorCode());
        return buildResponseEntity(constructApiError(e.getSourceSystem(), e.getErrorCode(), HttpStatus.BAD_REQUEST));
    }

    private ApiError constructApiError(SourceSystemConstant sourceSystem, ErrorCodeConstant errorCode, HttpStatus httpStatus) {
        ErrorCodeResponse errorCodeResponse = generalService.getErrorCode(sourceSystem, errorCode);
        return mapToApiError(errorCodeResponse, httpStatus);
    }

    private ApiError mapToApiError(ErrorCodeResponse errorCodeResponse, HttpStatus httpStatus) {
        return ApiError.builder()
                .status(httpStatus)
                .sourceSystem(errorCodeResponse.getSourceSystem())
                .errorCode(errorCodeResponse.getErrorCode())
                .errorTitle(errorCodeResponse.getEngTittle())
                .errorTitleIdn(errorCodeResponse.getIdnTittle())
                .errorTitleEng(errorCodeResponse.getEngTittle())
                .message(errorCodeResponse.getEngMessage())
                .errorMessageIdn(errorCodeResponse.getIdnMessage())
                .errorMessageEng(errorCodeResponse.getEngMessage())
                .debugMessage(errorCodeResponse.getEngMessage())
                .transactionId(readAndWriteFileUtils.getTransactionId(false))
                .handlerInfo(errorCodeResponse.getHandlerInfo())
                .build();
    }

    public ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
