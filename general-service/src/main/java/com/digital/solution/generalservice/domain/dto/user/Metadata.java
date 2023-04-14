package com.digital.solution.generalservice.domain.dto.user;

import com.digital.solution.generalservice.domain.entity.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata implements Serializable {
    private static final long serialVersionUID = 3662945374780296659L;

    private UserProfile userProfile;
    private String userAgent;
    private String ipAddress;
}
