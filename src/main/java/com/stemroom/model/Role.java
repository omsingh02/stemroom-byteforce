package com.stemroom.model;

import java.util.Arrays;
import java.util.Optional;

/** The two kinds of account. The name is what gets stored in the database. */
public enum Role {
    ADMIN("Admin"),
    MUSICIAN("Musician");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** Turns a form value such as "ADMIN" into a Role. Empty if the value is not a role. */
    public static Optional<Role> parse(String value) {
        return Arrays.stream(values()).filter(r -> r.name().equals(value)).findFirst();
    }
}
