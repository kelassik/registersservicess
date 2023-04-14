package com.digital.solution.generalservice.domain.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCodeResponse implements Serializable {
    private static final long serialVersionUID = -416408289486315857L;

    private String sourceSystem;
    private String errorCode;
    private String description;
    private String engTittle;
    private String engMessage;
    private String idnTittle;
    private String idnMessage;
    private String handlerInfo;
}
