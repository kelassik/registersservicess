package com.digital.solution.generalservice.domain.dto.user.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse implements Serializable {
    private static final long serialVersionUID = 5938707086271274776L;

    private Long userId;
    private String accessToken;
    private Boolean isLoginSuspect;
    private Integer expiresIn;
    private Object transactionSessionMap;
    private List<String> transactionSession;
    private Boolean isTransactionSessionSuccess;
}
