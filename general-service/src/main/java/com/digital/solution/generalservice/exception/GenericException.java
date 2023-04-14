package com.digital.solution.generalservice.exception;

import com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant;
import com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GenericException extends RuntimeException {
    private static final long serialVersionUID = -4852007042375366694L;

    private final SourceSystemConstant sourceSystem;
    private final ErrorCodeConstant errorCode;
    private final String transactionId;

    public GenericException(SourceSystemConstant sourceSystem, ErrorCodeConstant errorCode) {
        this.sourceSystem = sourceSystem;
        this.errorCode = errorCode;
        transactionId = null;
    }
}
