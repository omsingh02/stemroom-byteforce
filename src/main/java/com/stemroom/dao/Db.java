package com.stemroom.dao;

import com.stemroom.exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC helpers shared by the DAOs. Every statement is a PreparedStatement,
 * so values go in as parameters and never become part of the SQL text.
 * Each call opens its own connection and closes it before returning.
 */
public final class Db {

    /** Turns one row of a result set into an object. Lets one method serve every DAO. */
    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    // Environment variables win, so the real password does not have to live in the code.
    private static final String URL = env("STEMROOM_DB_URL",
            "jdbc:mysql://localhost:3306/stemroom?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = env("STEMROOM_DB_USER", "root");
    private static final String PASSWORD = env("STEMROOM_DB_PASSWORD", "root");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Driver may already be registered or on container classpath
        }
    }

    private Db() {
    }

    /** Runs a SELECT and maps every row. The params fill the ? marks in order. */
    public static <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) {
        List<T> rows = new ArrayList<>();
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read from the database", e);
        }
        return rows;
    }

    /** Runs an INSERT and returns the id MySQL generated for the new row. */
    public static int insert(String sql, Object... params) {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not save the row", e);
        }
    }

    /** Runs an UPDATE or a DELETE. */
    public static void update(String sql, Object... params) {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not change the row", e);
        }
    }

    /** The first row, or empty when the list is empty. Keeps "not found" out of null checks. */
    public static <T> Optional<T> first(List<T> rows) {
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Puts each value into its ? mark. A null value becomes SQL NULL. */
    private static void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
