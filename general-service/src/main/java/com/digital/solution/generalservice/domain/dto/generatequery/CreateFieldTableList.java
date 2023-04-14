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
public class CreateFieldTableList implements Serializable {
    private static final long serialVersionUID = 836348214678151435L;

    @NotNull
    private String filedName;

    @NotNull
    private String typeData;
    private int length;
    private String secondLength;

    @Builder.Default
    private boolean isNullable = true;

    @Builder.Default
    private boolean isPrimaryKey = false;
    private String defaultValue;
    private String comment;
}
