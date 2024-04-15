/**
 * Luis Hernandez, Guillermo Zendejas
 * April 2, 2024
 * InputValidator.java, this class holds the methods to check
 * whether inputted usernames and passwords are viable to application standards
 */

package com.example.lipt.Utils;

public class InputValidator {

    //regular alphanumeric characters for username
    private final static String username_criteria_alphanumeric = "^[a-zA-Z0-9]*$";

    //password criteria, for at least one letter and one number respectively
    private final static String password_criteria_letter = ".*[a-zA-Z]+.*";
    private final static String password_criteria_digit = ".*\\d+.*";

    //for username viability
    public static boolean viableUsername(String username) {
        int len = username.length();

        //username must be between 3-12 characters in length and strictly alphanumeric
        return len >= 3 && len <= 12 && username.matches(username_criteria_alphanumeric);
    }


    //for password viability
    public static boolean viablePassword(String password) {
        int len = password.length();

        //password must be between 8-16 in length and include at least one num and letter
        return (len >= 8 && len <= 16
                && password.matches(password_criteria_letter)
                && password.matches(password_criteria_digit));
    }
}
