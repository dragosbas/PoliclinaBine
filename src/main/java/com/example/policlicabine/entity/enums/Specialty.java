package com.example.policlicabine.entity.enums;

public enum Specialty {
    FACE("Face"),
    NECK("Neck"),
    MOLES("Moles"),
    GENERAL_DERMATOLOGY("General Dermatology"),
    COSMETIC_DERMATOLOGY("Cosmetic Dermatology"),
    MEDICAL_DERMATOLOGY("Medical Dermatology");

    private final String displayName;

    Specialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
