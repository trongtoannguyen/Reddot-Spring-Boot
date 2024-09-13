package com.reddot.app.util;

public class Validator {
    public static boolean isUsernameValid(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,}$");
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-zA-Z]{2,}$");
    }

    public static boolean isPasswordValid(String password) {
        // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
