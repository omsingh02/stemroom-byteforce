package com.stemroom.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/** Small checks for form input. Each method answers one question. */
public final class Validation {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private Validation() {
    }

    /** Trims a form value. A missing value becomes an empty string, never null. */
    public static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    /** Trims and lower-cases an email, so one person cannot make two accounts by changing case. */
    public static String normalizeEmail(String value) {
        return clean(value).toLowerCase(Locale.ROOT);
    }

    public static boolean isEmail(String value) {
        return EMAIL.matcher(value).matches();
    }

    /** A whole number from the text, or the fallback if it is not one. */
    public static int parseIntOr(String value, int fallback) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /** An id from a query string or form field. Returns -1 when it is missing or not a positive number. */
    public static int parseId(String value) {
        int id = parseIntOr(value, -1);
        return id > 0 ? id : -1;
    }

    /** The date in yyyy-mm-dd form, or null when the text is blank or not a date. */
    public static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(clean(value));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
