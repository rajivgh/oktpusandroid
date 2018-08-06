package com.app.oktpus.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gyandeep on 16/1/17.
 */

public class Validations {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static Matcher matcher;

    public static boolean isEmailValid2(String email) {
        return (email.contains("@") && email.contains("."));
    }

    public static boolean isEmailValid(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

}