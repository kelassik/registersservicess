package com.digital.solution.generalservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateToClobRequest implements Serializable {
    private static final long serialVersionUID = 7056512105724355845L;

    @NotNull
    private String fileName;

    @Min(value = 100)
    @Builder.Default
    private int intervalToClob = 100;
}
