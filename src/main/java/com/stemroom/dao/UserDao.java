package com.stemroom.dao;

import com.stemroom.model.Role;
import com.stemroom.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Reads and writes the users table. */
public class UserDao implements CrudDao<User> {

    private static final String SELECT = "SELECT id, name, email, password_hash, role FROM users";

    @Override
    public List<User> findAll() {
        return Db.query(SELECT + " ORDER BY name", UserDao::mapRow);
    }

    @Override
    public Optional<User> findById(int id) {
        return Db.first(Db.query(SELECT + " WHERE id = ?", UserDao::mapRow, id));
    }

    /** Emails are stored in lower case, so pass a normalised email here. */
    public Optional<User> findByEmail(String email) {
        return Db.first(Db.query(SELECT + " WHERE email = ?", UserDao::mapRow, email));
    }

    @Override
    public int insert(User user) {
        return Db.insert("INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)",
                user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole().name());
    }

    public void updateRole(int id, Role role) {
        Db.update("UPDATE users SET role = ? WHERE id = ?", role.name(), id);
    }

    @Override
    public void deleteById(int id) {
        Db.update("DELETE FROM users WHERE id = ?", id);
    }

    /** Turns one row into the matching User subclass. Other DAOs reuse this for member lists. */
    static User mapRow(ResultSet rs) throws SQLException {
        return User.create(
                Role.valueOf(rs.getString("role")),
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"));
    }
}
