package com.stemroom.model;

import java.util.Arrays;
import java.util.Optional;

/** Whether an admin has looked at a song yet. */
public enum ModerationStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String label;

    ModerationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Optional<ModerationStatus> parse(String value) {
        return Arrays.stream(values()).filter(s -> s.name().equals(value)).findFirst();
    }
}
