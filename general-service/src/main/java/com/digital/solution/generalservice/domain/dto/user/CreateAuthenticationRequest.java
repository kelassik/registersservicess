package com.digital.solution.generalservice.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAuthenticationRequest extends LoginRequest implements Serializable {
    private static final long serialVersionUID = 4276934745920720130L;

    @NotNull
    private String cif;
}
