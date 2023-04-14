package com.digital.solution.generalservice.domain.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.TRACE_ID_FIELD;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError implements Serializable {
    private static final long serialVersionUID = 7008843568581020853L;

    private HttpStatus status;

    @Builder.Default
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    private String errorTitle;
    private String message;
    private String errorCode;
    private String debugMessage;
    private String sourceSystem;
    private String errorTitleIdn;
    private String errorMessageIdn;
    private String errorTitleEng;
    private String errorMessageEng;

    @Builder.Default
    private String traceId = MDC.get(TRACE_ID_FIELD);
    private String transactionId;
    private String handlerInfo;
}
