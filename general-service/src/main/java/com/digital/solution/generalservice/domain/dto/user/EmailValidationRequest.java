package com.digital.solution.generalservice.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.intellij.lang.annotations.RegExp;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmailValidationRequest implements Serializable {
    private static final long serialVersionUID = 9092771620546743123L;

    @NotNull
    private String cif;

    @NotNull
    @RegExp(prefix = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    private String email;
}
