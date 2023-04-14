package com.digital.solution.generalservice.domain.dto.user;

import com.digital.solution.generalservice.domain.dto.user.authorization.TokenResponse;
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
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 6515993670653204424L;

    private UserInformation userInformation;
    private TokenResponse tokenResponse;
}
