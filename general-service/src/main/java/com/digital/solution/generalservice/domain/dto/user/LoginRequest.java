package com.digital.solution.generalservice.domain.dto.user;

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
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 4273064321120196293L;

    @NotNull
    private String username;

    @NotNull
    private String password;
}
