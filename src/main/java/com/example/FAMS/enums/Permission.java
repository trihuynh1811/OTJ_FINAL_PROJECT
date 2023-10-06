package com.example.FAMS.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    USER_IMPORT("user:import"),

    SYLLABUS_CREATE("syllabus:create"),
    SYLLABUS_READ("syllabus:read"),
    SYLLABUS_UPDATE("syllabus:update"),
    SYLLABUS_DELETE("syllabus:delete"),
    SYLLABUS_IMPORT("syllabus:import"),

    TRAINING_CREATE("training:create"),
    TRAINING_READ("training:read"),
    TRAINING_UPDATE("training:update"),
    TRAINING_DELETE("training:delete"),
    TRAINING_IMPORT("training:import"),

    CLASS_CREATE("class:create"),
    CLASS_READ("class:read"),
    CLASS_UPDATE("class:update"),
    CLASS_DELETE("class:delete"),
    CLASS_IMPORT("class:import"),

    MATERIAL_CREATE("material:create"),
    MATERIAL_READ("material:read"),
    MATERIAL_UPDATE("material:update"),
    MATERIAL_DELETE("material:delete"),
    MATERIAL_IMPORT("material:import")
    ;

    @Getter
    private final String permission;

}
