package com.digital.solution.generalservice.domain.dto.user;

import com.digital.solution.generalservice.domain.entity.user.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse implements Serializable {
    private static final long serialVersionUID = -2720289380859985542L;

    @SuppressWarnings("all")
    private UserInformation userInformation;
}
