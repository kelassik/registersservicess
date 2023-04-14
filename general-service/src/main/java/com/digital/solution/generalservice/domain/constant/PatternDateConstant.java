package com.digital.solution.generalservice.domain.constant;

public enum PatternDateConstant {
    PATTERN_DATE_NO_SEPARATOR("yyyyMMdd");

    private final String pattern;

    PatternDateConstant(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
