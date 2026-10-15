package com.stemroom.model;

/**
 * Base class for every account. Each subclass decides what its role may do,
 * so the rest of the code can ask a user instead of checking the role by hand.
 */
public abstract class User {

    private final int id;
    private final String name;
    private final String email;
    private final String passwordHash;

    protected User(int id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /** Builds the subclass for a role. Use id 0 for an account that is not saved yet. */
    public static User create(Role role, int id, String name, String email, String passwordHash) {
        return switch (role) {
            case ADMIN -> new Admin(id, name, email, passwordHash);
            case MUSICIAN -> new Musician(id, name, email, passwordHash);
        };
    }

    public abstract Role getRole();

    /** Whether this account may approve or reject songs. */
    public abstract boolean canModerate();

    public String getRoleLabel() {
        return getRole().getLabel();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
