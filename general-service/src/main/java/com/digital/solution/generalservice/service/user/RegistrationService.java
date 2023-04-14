package com.digital.solution.generalservice.service.user;

import com.digital.solution.generalservice.domain.constant.user.audit.ActivityStatusConstant;
import com.digital.solution.generalservice.domain.dto.user.CreateAuthenticationRequest;
import com.digital.solution.generalservice.domain.dto.user.EmailValidationRequest;
import com.digital.solution.generalservice.domain.dto.user.Metadata;
import com.digital.solution.generalservice.domain.dto.user.RegistrationRequest;
import com.digital.solution.generalservice.domain.dto.user.RegistrationResponse;
import com.digital.solution.generalservice.domain.entity.user.UserAuthentication;
import com.digital.solution.generalservice.domain.entity.user.UserInformation;
import com.digital.solution.generalservice.domain.entity.user.UserProfile;
import com.digital.solution.generalservice.exception.GenericException;
import com.digital.solution.generalservice.repository.UserAuthenticationRepository;
import com.digital.solution.generalservice.repository.UserInformationRepository;
import com.digital.solution.generalservice.repository.UserProfileRepository;
import com.digital.solution.generalservice.security.aes.AesSecurity;
import com.digital.solution.generalservice.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.SYSTEM_STRING;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_CIF_ALREADY_EXIST;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_DATA_NOT_FOUND;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_EMAIL_ALREADY_EXIST;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_GENERAL;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_MOBILE_PHONE_ALREADY_EXIST;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_SUCCESS;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_USERNAME_ALREADY_EXIST;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;
import static com.digital.solution.generalservice.domain.constant.user.EmailStatusConstant.NOT_VERIFIED;
import static com.digital.solution.generalservice.domain.constant.user.EmailStatusConstant.VERIFIED;
import static com.digital.solution.generalservice.domain.constant.user.LanguageConstant.ENGLISH;
import static com.digital.solution.generalservice.domain.constant.user.StatusConstant.ACTIVE;
import static com.digital.solution.generalservice.domain.constant.user.StatusConstant.NOT_ACTIVE;
import static com.digital.solution.generalservice.domain.constant.user.audit.ModuleNameConstant.REGISTRATION;
import static com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant.CREATE_AUTHENTICATION;
import static com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant.REGISTRATION_ACCOUNT;
import static com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant.VALIDATION_EMAIL;

@Slf4j
@Service
public class RegistrationService extends BaseService {

    @Autowired
    @SuppressWarnings("all")
    private UserProfileRepository userProfileRepository;

    @Autowired
    @SuppressWarnings("all")
    private UserInformationRepository userInformationRepository;

    @Autowired
    @SuppressWarnings("all")
    private UserAuthenticationRepository userAuthenticationRepository;

    private static final String REQUEST_DATA_LOG = "request data : {}";
    private static final String FLAG_USER_EMAIL_VALIDATION = "E";
    private static final String FLAG_USER_CREATE_AUTHENTICATION = "CA";

    public RegistrationResponse userRegistration(RegistrationRequest request, Metadata metadata) {
        log.info("start userRegistration");
        log.info(REQUEST_DATA_LOG, request);
        var result = RegistrationResponse.builder().build();
        var transactionId = readAndWriteFileUtils.getTransactionId();
        var activityStatus = ActivityStatusConstant.FAILED;
        var errorCode = ERROR_CODE_GENERAL;
        try {
            var email = request.getEmail();
            var cif = request.getCif();
            var mobilePhone = request.getMobilePhone();
            validateUserRegistration(cif, email, mobilePhone);

            var userProfileRequest = constructUserProfile(cif, email, mobilePhone);
            var userProfileResponse = userProfileRepository.saveAndFlush(userProfileRequest);
            metadata.setUserProfile(userProfileResponse);
            var userInformationRequest = constructUserInformation(userProfileResponse, request);
            var userInformationResponse = userInformationRepository.saveAndFlush(userInformationRequest);
            result.setUserInformation(userInformationResponse);
            activityStatus = ActivityStatusConstant.SUCCESS;
            errorCode = ERROR_CODE_SUCCESS;
        } finally {
            auditService.storeAudit(generateStoreAuditRequest(transactionId, metadata, REGISTRATION, REGISTRATION_ACCOUNT, activityStatus, SOURCE_SYSTEM_CLICKS, errorCode, null));
        }

        return result;
    }

    private UserInformation constructUserInformation(UserProfile userProfile, RegistrationRequest request) {
        return UserInformation.builder()
                .userProfile(userProfile)
                .fullName(request.getFullName())
                .shortName(request.getShortName())
                .gender(request.getGender().getGender())
                .maritalStatus(request.getMaritalStatus().getMaritalStatus())
                .placeOfBirth(request.getPlaceOfBirth())
                .dateOfBirth(request.getDateOfBirth())
                .residentType(request.getResidentType())
                .build();
    }

    private void validateUserRegistration(String cif, String email, String mobilePhone) {
        var userProfileList = userProfileRepository.findByCifOrEmailOrMobilePhone(cif, email, mobilePhone);
        if (CollectionUtils.isNotEmpty(userProfileList)) {
            userProfileList.forEach(userProfile -> {
                if (userProfile.getCif().equals(cif)) {
                    throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_CIF_ALREADY_EXIST);
                }

                if (userProfile.getEmail().equals(email)) {
                    throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_EMAIL_ALREADY_EXIST);
                }

                if (userProfile.getMobilePhone().equals(mobilePhone)) {
                    throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_MOBILE_PHONE_ALREADY_EXIST);
                }
            });
        }
    }

    public void emailValidation(EmailValidationRequest request, Metadata metadata) {
        log.info("start emailValidation");
        log.info(REQUEST_DATA_LOG, request);
        var transactionId = readAndWriteFileUtils.getTransactionId();
        var errorCode = ERROR_CODE_GENERAL;
        var activityStatus = ActivityStatusConstant.FAILED;
        var sourceSystem = SOURCE_SYSTEM_CLICKS;
        try {
            var cif = request.getCif();
            var email = request.getEmail();
            var userProfile = userProfileRepository.findByCifAndEmail(cif, email).orElseThrow(() -> new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_DATA_NOT_FOUND));
            userProfile = constructUserProfile(userProfile, FLAG_USER_EMAIL_VALIDATION);
            metadata.setUserProfile(userProfile);
            activityStatus = ActivityStatusConstant.SUCCESS;
            errorCode = ERROR_CODE_SUCCESS;
        } catch (GenericException genericException) {
            log.error("Error GenericException emailValidation : {}", genericException.getMessage(), genericException);
            errorCode = genericException.getErrorCode();
            sourceSystem = genericException.getSourceSystem();
            throw genericException;
        } finally {
            auditService.storeAudit(generateStoreAuditRequest(transactionId, metadata, REGISTRATION, VALIDATION_EMAIL, activityStatus, sourceSystem, errorCode, null));
        }
    }

    public void createAuthentication(CreateAuthenticationRequest request, Metadata metadata) {
        log.info("start createAuthentication");
        log.info(REQUEST_DATA_LOG, request);
        var errorCode = ERROR_CODE_GENERAL;
        var sourceSystem = SOURCE_SYSTEM_CLICKS;
        var activityStatus = ActivityStatusConstant.FAILED;
        var transactionId = readAndWriteFileUtils.getTransactionId();
        try {
            var userProfile = userProfileRepository.findByCifAndEmailStatus(request.getCif(), VERIFIED).orElseThrow(() -> new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_DATA_NOT_FOUND));
            metadata.setUserProfile(userProfile);
            var username = request.getUsername();
            var userAuthenticationOptional = userAuthenticationRepository.findByUsername(username);
            if (userAuthenticationOptional.isPresent()) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_USERNAME_ALREADY_EXIST);
            }

            var userAuthenticationRequest = UserAuthentication.builder()
                    .userProfile(userProfile)
                    .username(username)
                    .password(AesSecurity.encrypt(request.getPassword()))
                    .build();
            var userAuthentication = userAuthenticationRepository.save(userAuthenticationRequest);
            userProfile = constructUserProfile(userAuthentication.getUserProfile(), FLAG_USER_CREATE_AUTHENTICATION);
            metadata.setUserProfile(userProfile);
            activityStatus = ActivityStatusConstant.SUCCESS;
            errorCode = ERROR_CODE_SUCCESS;
        } catch (GenericException genericException) {
            log.error("Error GenericException createAuthentication : {}", genericException.getMessage(), genericException);
            sourceSystem = genericException.getSourceSystem();
            errorCode = genericException.getErrorCode();
            throw genericException;
        } finally {
            auditService.storeAudit(generateStoreAuditRequest(transactionId, metadata, REGISTRATION, CREATE_AUTHENTICATION, activityStatus, sourceSystem, errorCode, null));
        }
    }

    private UserProfile constructUserProfile(@NotNull String cif, @NotNull String email, @NotNull String mobilePhone) {
        var userProfileRequest = UserProfile.builder()
                .cif(cif)
                .email(email)
                .emailStatus(NOT_VERIFIED)
                .mobilePhone(mobilePhone)
                .language(ENGLISH.getLanguage())
                .numFailedLogin(0)
                .status(NOT_ACTIVE)
                .isDeleted(false)
                .build();
        return userProfileRepository.saveAndFlush(userProfileRequest);
    }

    private UserProfile constructUserProfile(@NotNull UserProfile userProfile, @NotNull String flagUser) {
        if (flagUser.equals(FLAG_USER_EMAIL_VALIDATION)) {
            userProfile.setEmailVerifiedDate(LocalDateTime.now());
            userProfile.setEmailStatus(VERIFIED);
        } else if (flagUser.equals(FLAG_USER_CREATE_AUTHENTICATION)) {
            userProfile.setStatus(ACTIVE);
        }
        userProfile.setModifiedDate(LocalDateTime.now());
        userProfile.setModifiedBy(SYSTEM_STRING);
        return userProfileRepository.save(userProfile);
    }
}
