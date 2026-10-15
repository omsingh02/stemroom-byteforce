package com.stemroom.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Stores passwords as salt$hash, where hash is SHA-256 of salt plus password.
 * Fine for a learning project. A real product should use BCrypt or PBKDF2.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String password) {
        String salt = randomSalt();
        return salt + "$" + digest(salt, password);
    }

    public static boolean matches(String password, String stored) {
        int split = stored.indexOf('$');
        if (split < 0) {
            return false;
        }
        String salt = stored.substring(0, split);
        return digest(salt, password).equals(stored.substring(split + 1));
    }

    private static String digest(String salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available on this Java install", e);
        }
    }

    private static String randomSalt() {
        byte[] bytes = new byte[8];
        new SecureRandom().nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
