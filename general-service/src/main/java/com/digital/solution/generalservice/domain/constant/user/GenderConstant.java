package com.digital.solution.generalservice.domain.constant.user;

public enum GenderConstant {
    MAN("L"),
    WOMEN("P");

    private final String gender;

    GenderConstant(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    @SuppressWarnings("all")
    public GenderConstant getGender(String gender) {
        for (GenderConstant genderConstant : GenderConstant.values()) {
            if (genderConstant.getGender().equals(gender)) {
                return genderConstant;
            }
        }

        return null;
    }
}
