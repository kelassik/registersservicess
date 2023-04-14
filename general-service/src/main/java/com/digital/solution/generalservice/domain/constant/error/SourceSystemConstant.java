package com.digital.solution.generalservice.domain.constant.error;

public enum SourceSystemConstant {
    SOURCE_SYSTEM_CLICKS("CLICKS");

    private final String sourceSystem;

    SourceSystemConstant(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    @SuppressWarnings("all")
    public SourceSystemConstant getSourceSystem(String sourceSystem) {
        for (SourceSystemConstant sourceSystemConstant : SourceSystemConstant.values()) {
            if (sourceSystemConstant.getSourceSystem().equals(sourceSystem)) {
                return sourceSystemConstant;
            }
        }

        return null;
    }
}
