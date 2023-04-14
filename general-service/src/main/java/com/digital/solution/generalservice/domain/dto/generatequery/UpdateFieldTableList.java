package com.digital.solution.generalservice.domain.dto.generatequery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFieldTableList implements Serializable {
    private static final long serialVersionUID = 2124978363188080260L;

    @NotNull
    private String fieldName;

    @NotNull
    private String value;
}
