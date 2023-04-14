package com.digital.solution.generalservice.domain.constant.user.audit;

@SuppressWarnings("all")
public enum ModuleNameConstant {
    REGISTRATION("REGISTRATION"),
    LOGIN("LOGIN");

    private final String moduleName;

    ModuleNameConstant(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ModuleNameConstant getModuleName(String moduleName) {
        for (ModuleNameConstant moduleNameConstant : ModuleNameConstant.values()) {
            if (moduleNameConstant.getModuleName().equals(moduleName)) {
                return moduleNameConstant;
            }
        }

        return null;
    }
}
