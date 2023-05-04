package de.jinba.server.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static final String VALID_EMAIL_ADDRESS_REGEX =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static final Pattern VALID_EMAIL_ADDRESS_PATTERN =
            Pattern.compile(VALID_EMAIL_ADDRESS_REGEX, Pattern.CASE_INSENSITIVE);

    // (?=.*[0-9]) a digit must occur at least once
    // (?=.*[a-z]) a lower case letter must occur at least once
    // (?=.*[A-Z]) an upper case letter must occur at least once
    // (?=.*[@#$%^&+=]) a special character must occur at least once
    // (?=\\S+$) no whitespace allowed in the entire string
    // .{8,20} min 8 max 20 characters

    public static final String VALID_PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
    public static final Pattern VALID_PASSWORD_PATTERN =
            Pattern.compile(VALID_PASSWORD_REGEX);

    /**
     * Method to test weather the email is valid or not
     */
    public static boolean isValidEmail(String email) {
        if(Objects.isNull(email)) {
            return false;
        }
        return VALID_EMAIL_ADDRESS_PATTERN.matcher(email).find();
    }

    /**
     * Method to test weather the password is valid or not. Rules are:<br>
     * - at least one digit<br>
     * - at least one lower case letter<br>
     * - at least one upper case letter<br>
     * - at least one special character<br>
     * - no whitespace allowed<br>
     * - min 8 max 20 characters<br>
     */
    public static boolean isValidPassword(String password) {
        if(Objects.isNull(password)) {
            return false;
        }
        return VALID_PASSWORD_PATTERN.matcher(password).find();
    }
}
