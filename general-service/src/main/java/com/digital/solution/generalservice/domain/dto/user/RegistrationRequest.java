package com.digital.solution.generalservice.domain.dto.user;

import com.digital.solution.generalservice.domain.constant.user.GenderConstant;
import com.digital.solution.generalservice.domain.constant.user.MaritalStatusConstant;
import com.digital.solution.generalservice.domain.constant.user.ResidentTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.intellij.lang.annotations.RegExp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegistrationRequest extends EmailValidationRequest implements Serializable {
    private static final long serialVersionUID = -8777279224302948235L;

    @NotNull
    private String fullName;

    @NotNull
    private String shortName;

    @NotNull
    private GenderConstant gender;

    @NotNull
    private String placeOfBirth;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    private MaritalStatusConstant maritalStatus;

    @NotNull
    private ResidentTypeConstant residentType;

    @NotNull
    @RegExp(prefix = "^(08)(\\d{3,4}-?){2}\\d{3,4}$")
    private String mobilePhone;
}
