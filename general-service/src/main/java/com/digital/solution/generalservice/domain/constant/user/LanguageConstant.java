package com.digital.solution.generalservice.domain.constant.user;

public enum LanguageConstant {
    INDONESIAN("IDN"),
    ENGLISH("ENG");

    private final String language;

    LanguageConstant(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    @SuppressWarnings("all")
    public LanguageConstant getLanguage(String language) {
        for (LanguageConstant languageConstant : LanguageConstant.values()) {
            if (languageConstant.getLanguage().equals(language)) {
                return languageConstant;
            }
        }

        return null;
    }
}
