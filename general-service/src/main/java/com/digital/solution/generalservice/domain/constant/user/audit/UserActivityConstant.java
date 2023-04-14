package com.digital.solution.generalservice.domain.constant.user.audit;

@SuppressWarnings("all")
public enum UserActivityConstant {
    // REGISTRATION
    REGISTRATION_ACCOUNT("User Registration Account"),
    VALIDATION_EMAIL("User Validation Email"),
    CREATE_AUTHENTICATION("User Create Authentication"),
    LOGIN_INPUT_USERNAME_AND_PASSWORD("User Input Username And Password");

    private final String userActivity;

    UserActivityConstant(String userActivity) {
        this.userActivity = userActivity;
    }

    public String getUserActivity() {
        return userActivity;
    }

    public UserActivityConstant getUserActivity(String userActivity) {
        for (UserActivityConstant userActivityConstant : UserActivityConstant.values()) {
            if (userActivityConstant.getUserActivity().equals(userActivity)) {
                return userActivityConstant;
            }
        }

        return null;
    }
}
