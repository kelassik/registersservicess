package com.digital.solution.generalservice.domain.dto.generatequery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = -4416454920020812524L;

    private String schemaName;

    @NotNull
    private String tableName;
}
