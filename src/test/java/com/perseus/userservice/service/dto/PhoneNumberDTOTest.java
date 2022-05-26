package com.perseus.userservice.service.dto;

import com.perseus.userservice.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneNumberDTO.class);
        PhoneNumberDTO phoneNumberDTO1 = new PhoneNumberDTO();
        phoneNumberDTO1.setId(1L);
        PhoneNumberDTO phoneNumberDTO2 = new PhoneNumberDTO();
        assertThat(phoneNumberDTO1).isNotEqualTo(phoneNumberDTO2);
        phoneNumberDTO2.setId(phoneNumberDTO1.getId());
        assertThat(phoneNumberDTO1).isEqualTo(phoneNumberDTO2);
        phoneNumberDTO2.setId(2L);
        assertThat(phoneNumberDTO1).isNotEqualTo(phoneNumberDTO2);
        phoneNumberDTO1.setId(null);
        assertThat(phoneNumberDTO1).isNotEqualTo(phoneNumberDTO2);
    }
}
