package com.stemroom.model;

/** Manages accounts, reviews songs and changes settings. */
public class Admin extends User {

    public Admin(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public boolean canModerate() {
        return true;
    }
}
