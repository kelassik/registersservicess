package com.digital.solution.generalservice.domain.constant.user;

public enum MaritalStatusConstant {
    DIVORCED("D"),
    MARRIED("M"),
    SINGLE("S"),
    UNKNOWN("U"),
    WIDOWED("W"),
    SEPARATED("X");

    private final String maritalStatus;

    MaritalStatusConstant(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    @SuppressWarnings("all")
    public MaritalStatusConstant getMaritalStatus(String maritalStatus) {
        for (MaritalStatusConstant maritalStatusConstant : MaritalStatusConstant.values()) {
            if (maritalStatusConstant.getMaritalStatus().equals(maritalStatus)) {
                return maritalStatusConstant;
            }
        }

        return null;
    }
}
