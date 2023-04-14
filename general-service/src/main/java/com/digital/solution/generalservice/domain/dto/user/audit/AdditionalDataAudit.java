package com.digital.solution.generalservice.domain.dto.user.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalDataAudit implements Serializable {
    private static final long serialVersionUID = -4527236491232330718L;

    private String token;
}
