package com.digital.solution.generalservice.web;

import com.digital.solution.generalservice.domain.dto.user.Metadata;
import com.digital.solution.generalservice.domain.entity.user.UserProfile;
import com.digital.solution.generalservice.repository.UserProfileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.ACCESS_TOKEN;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.HEADER_FORWARD;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.USER_AGENT;

public class BaseController {

    @Autowired
    @SuppressWarnings("all")
    private UserProfileRepository userProfileRepository;

    protected Metadata generateMetadata(HttpServletRequest request) {
        String ipAddress = request.getHeader(HEADER_FORWARD);
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader(USER_AGENT);

        var userProfile = UserProfile.builder().build();
        String accessToken = request.getHeader(ACCESS_TOKEN);
        if (StringUtils.isNotEmpty(accessToken)) {
            userProfile = getUserProfile(Long.parseLong(accessToken));
        }

        return Metadata.builder()
                .userProfile(userProfile)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
    }

    private UserProfile getUserProfile(Long id) {
        return userProfileRepository.getById(id);
    }
}
