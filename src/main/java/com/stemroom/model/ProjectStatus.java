package com.stemroom.model;

import java.util.Arrays;
import java.util.Optional;

/** Where a project stands. The names match the ENUM in schema.sql. */
public enum ProjectStatus {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String label;

    ProjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Optional<ProjectStatus> parse(String value) {
        return Arrays.stream(values()).filter(s -> s.name().equals(value)).findFirst();
    }
}
