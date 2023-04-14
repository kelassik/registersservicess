package com.digital.solution.generalservice.service.user;

import com.digital.solution.generalservice.domain.constant.user.StatusConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.ActivityStatusConstant;
import com.digital.solution.generalservice.domain.dto.user.LoginRequest;
import com.digital.solution.generalservice.domain.dto.user.LoginResponse;
import com.digital.solution.generalservice.domain.dto.user.Metadata;
import com.digital.solution.generalservice.domain.entity.user.UserInformation;
import com.digital.solution.generalservice.domain.entity.user.UserProfile;
import com.digital.solution.generalservice.exception.GenericException;
import com.digital.solution.generalservice.repository.UserAuthenticationRepository;
import com.digital.solution.generalservice.repository.UserInformationRepository;
import com.digital.solution.generalservice.repository.UserProfileRepository;
import com.digital.solution.generalservice.security.aes.AesSecurity;
import com.digital.solution.generalservice.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.SYSTEM_STRING;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_GENERAL;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_PASSWORD;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_USERNAME;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_USER_BLOCKED;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;
import static com.digital.solution.generalservice.domain.constant.user.audit.ModuleNameConstant.LOGIN;
import static com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant.LOGIN_INPUT_USERNAME_AND_PASSWORD;

@Slf4j
@Service
public class UserService extends BaseService {

    @Autowired
    @SuppressWarnings("all")
    private UserInformationRepository userInformationRepository;

    @Autowired
    @SuppressWarnings("all")
    private UserProfileRepository userProfileRepository;

    @Autowired
    @SuppressWarnings("all")
    private UserAuthenticationRepository userAuthenticationRepository;

//    @Autowired
//    @SuppressWarnings("all")
//    private TokenService tokenService;

    public LoginResponse login(LoginRequest request, Metadata metadata) {
        log.info("start login");
        log.info("request data : {}", request);
        var errorCode = ERROR_CODE_GENERAL;
        var sourceSystem = SOURCE_SYSTEM_CLICKS;
        var activityStatus = ActivityStatusConstant.FAILED;
        var transactionId = readAndWriteFileUtils.getTransactionId();
        var result = LoginResponse.builder().build();
        try {
            var userAuthentication = userAuthenticationRepository.findByUsername(request.getUsername()).orElseThrow(() -> new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_USERNAME));
            var passwordEncrypt = userAuthentication.getPassword();
            var passwordDecrypt = AesSecurity.decrypt(passwordEncrypt);
            var userProfile = userAuthentication.getUserProfile();
            metadata.setUserProfile(userProfile);
            if (!passwordDecrypt.equals(request.getPassword())) {
                failedLoginCounter(userProfile);
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_PASSWORD);
            }

            if (userProfile.getNumFailedLogin() >= 3) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_USER_BLOCKED);
            }

            userProfile.setDateLastSuccessLogin(LocalDateTime.now());
            userProfile = userProfileRepository.save(userProfile);
            var userInformation = userInformationRepository.findByUserProfileId(userProfile.getId()).orElse(UserInformation.builder().build());
            result.setUserInformation(userInformation);

//            var tokenResponse = tokenService.createAuthorization(userInformation.getUserProfile().getId());
//            result.setTokenResponse(tokenResponse);
            activityStatus = ActivityStatusConstant.SUCCESS;
        } catch (GenericException genericException) {
            log.error("Error GenericException login : {}", genericException.getMessage(), genericException);
            sourceSystem = genericException.getSourceSystem();
            errorCode = genericException.getErrorCode();
            throw genericException;
        } finally {
            auditService.storeAudit(generateStoreAuditRequest(transactionId, metadata, LOGIN, LOGIN_INPUT_USERNAME_AND_PASSWORD, activityStatus, sourceSystem, errorCode, null));
        }

        return result;
    }

    private void failedLoginCounter(@NotNull UserProfile userProfile) {
        var numFailedLogin = 0;
        LocalDate lastDateFailedLogin = LocalDate.now();
        var dateTimeLast = userProfile.getDateLastFailedLogin();
        if (dateTimeLast != null) {
            lastDateFailedLogin = dateTimeLast.toLocalDate();
        }

        if (lastDateFailedLogin.equals(LocalDate.now())) {
            numFailedLogin = userProfile.getNumFailedLogin();
        }

        if (numFailedLogin < 2) {
            numFailedLogin += 1;
        } else {
            if (numFailedLogin < 3) {
                numFailedLogin += 1;
            }
            userProfile.setStatus(StatusConstant.BLOCKED);
        }

        userProfile.setNumFailedLogin(numFailedLogin);
        userProfile.setDateLastFailedLogin(LocalDateTime.now());
        userProfile.setModifiedDate(LocalDateTime.now());
        userProfile.setModifiedBy(SYSTEM_STRING);
        userProfile = userProfileRepository.save(userProfile);

        if (userProfile.getStatus().equals(StatusConstant.BLOCKED)) {
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_USER_BLOCKED);
        }
    }
}
