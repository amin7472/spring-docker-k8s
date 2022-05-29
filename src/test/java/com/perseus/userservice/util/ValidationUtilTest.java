package com.perseus.userservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidationUtilTest {

    @Test
    public void email_is_valid() {
        assertThat(ValidationUtil.emailIsValid("amin.ahmadi@gmail.com")).isTrue();
    }

    @Test
    public void email_is_invalid() {
        assertThat(ValidationUtil.emailIsValid("amin@ahmadi@gmail.com")).isFalse();
    }

    @Test
    public void number_valid() {
        String[] validPhoneNumbers
                = {"2055550125", "202 555 0125", "(202) 555-0125", "+111 (202) 555-0125",
                "636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89"};
        for (String phoneNumber : validPhoneNumbers) {
            assertThat(ValidationUtil.numberIsValid(phoneNumber)).isTrue();
        }
    }
}