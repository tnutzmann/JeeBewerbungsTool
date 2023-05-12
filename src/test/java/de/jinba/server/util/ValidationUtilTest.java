package de.jinba.server.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
class ValidationUtilTest {

    @Test
    void shouldReturnTrueForValidPassword() {
        assertTrue(ValidationUtil
                .isValidPassword("1aB!56789"));
    }

    @Test
    void shouldReturnFalseForPasswordWithoutNumber() {
        assertFalse(ValidationUtil
                .isValidPassword("aB!abcdefgh"));
    }

    @Test
    void shouldReturnFalseForPasswordWithoutUppercase() {
        assertFalse(ValidationUtil
                .isValidPassword("1a!abcdefgh"));
    }

    @Test
    void shouldReturnFalseForPasswordWithoutLowercase() {
        assertFalse(ValidationUtil
                .isValidPassword("1A!ABCDEFGH"));
    }

    @Test
    void shouldReturnFalseForPasswordWithoutSpecialCharacter() {
        assertFalse(ValidationUtil
                .isValidPassword("1aBcdefghij"));
    }

    @Test
    void shouldReturnFalseForPasswordWithLessThan8Characters() {
        assertFalse(ValidationUtil
                .isValidPassword("1aB!abc"));
    }

    @Test
    void shouldReturnFalseForPasswordWithMoreThan20Characters() {
        assertFalse(ValidationUtil
                .isValidPassword("1aB!abcdefghijabcdefghij"));
    }

    @Test
    void shouldReturnFalseForPasswordWithWhitespace() {
        assertFalse(ValidationUtil
                .isValidPassword("1aB!abc def"));
    }

    @Test
    void shouldReturnFalseForPasswordWithNull() {
        assertFalse(ValidationUtil
                .isValidPassword(null));
    }

    @Test
    void shouldReturnFalseForPasswordWithEmptyString() {
        assertFalse(ValidationUtil
                .isValidPassword(""));
    }

    @Test
    void shouldReturnTrueForValidEmail() {
        assertTrue(ValidationUtil
                .isValidEmail("abc@abc.de"));
    }

    @Test
    void shouldReturnFalseForEmailWithoutAt() {
        assertFalse(ValidationUtil
                .isValidEmail("abcabc.de"));
    }

    @Test
    void shouldReturnFalseForEmailWithoutDot() {
        assertFalse(ValidationUtil
                .isValidEmail("abc@abcde"));
    }

    @Test
    void shouldReturnFalseForEmailWithWhitespace() {
        assertFalse(ValidationUtil
                .isValidEmail("abc@abc de"));
    }

    @Test
    void shouldReturnFalseForEmailWithNull() {
        assertFalse(ValidationUtil
                .isValidEmail(null));
    }

    @Test
    void shouldReturnFalseForEmailWithEmptyString() {
        assertFalse(ValidationUtil
                .isValidEmail(""));
    }
}