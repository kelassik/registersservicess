package com.digital.solution.generalservice.domain.constant.error;

public enum ErrorCodeConstant {
    ERROR_CODE_INTERNAL_SERVER_ERROR("IX"),
    ERROR_CODE_SUCCESS("0"),
    ERROR_CODE_INVALID_REQUEST("0001"),
    ERROR_CODE_INVALID_READ_FILE("0002"),
    ERROR_CODE_INVALID_WRITE_FILE("0003"),
    ERROR_CODE_CIF_ALREADY_EXIST("0004"),
    ERROR_CODE_EMAIL_ALREADY_EXIST("0005"),
    ERROR_CODE_MOBILE_PHONE_ALREADY_EXIST("0006"),
    ERROR_CODE_DATA_NOT_FOUND("0007"),
    ERROR_CODE_USERNAME_ALREADY_EXIST("0008"),
    ERROR_CODE_INVALID_USERNAME("0009"),
    ERROR_CODE_INVALID_PASSWORD("0010"),
    ERROR_CODE_USER_BLOCKED("0011"),
    ERROR_CODE_ACCESS_TOKEN_EXPIRED("0012"),
    ERROR_CODE_INVALID_TOKEN("0013"),
    ERROR_CODE_INVALID_ACCESS_TOKEN("0014"),
    ERROR_CODE_CLIENT_NOT_VALID("0015"),
    ERROR_CODE_GENERAL("0099"),
    ERROR_CODE_ENC_INVALID_KEY("4001"),
    ERROR_CODE_ENC_INVALID_CIPHER("4002"),
    ERROR_CODE_ENC_INVALID_ALGORITHM("4003"),
    ERROR_CODE_UNHANDLED("8888");

    private final String errorCode;

    ErrorCodeConstant(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @SuppressWarnings("all")
    public static ErrorCodeConstant getErrorCode(String errorCode) {
        for (ErrorCodeConstant errorCodeConstant : ErrorCodeConstant.values()) {
            if (errorCodeConstant.getErrorCode().equals(errorCode)) {
                return errorCodeConstant;
            }
        }

        return null;
    }
}
