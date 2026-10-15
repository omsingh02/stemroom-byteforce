package com.stemroom.model;

/** Creates projects, adds people to them and keeps a song portfolio. */
public class Musician extends User {

    public Musician(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash);
    }

    @Override
    public Role getRole() {
        return Role.MUSICIAN;
    }

    @Override
    public boolean canModerate() {
        return false;
    }
}
