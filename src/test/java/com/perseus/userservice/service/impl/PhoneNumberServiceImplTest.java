package com.perseus.userservice.service.impl;

import com.perseus.userservice.UserServiceApplication;
import com.perseus.userservice.domain.PhoneNumber;
import com.perseus.userservice.mapper.PhoneNumberMapper;
import com.perseus.userservice.repository.PhoneNumberRepository;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
class PhoneNumberServiceImplTest {

    private static final String DEFAULT_NUMBER = "1230";


    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;


    private PhoneNumber phoneNumber;


    public PhoneNumber createEntity() {
        PhoneNumber phoneNumber = new PhoneNumber().number(DEFAULT_NUMBER);
        return phoneNumber;
    }

    @BeforeEach
    public void initTest() {
        phoneNumberRepository.deleteAll();
        phoneNumber = createEntity();
    }

    @Test
    @Transactional
    void create_new_number() {
        int databaseSizeBeforeCreate = phoneNumberService.findAll().size();
        phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        List<PhoneNumberDTO> phoneNumberList = phoneNumberService.findAll();
        assertThat(phoneNumberList).hasSize(databaseSizeBeforeCreate + 1);
        PhoneNumberDTO testPhoneNumber = phoneNumberList.get(phoneNumberList.size() - 1);
        assertThat(testPhoneNumber.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void should_not_create_number_with_existing_id() {
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        List<PhoneNumberDTO> phoneNumberList = phoneNumberService.findAll();
        int databaseSizeBeforeCreate = phoneNumberService.findAll().size();
        phoneNumberService.save(phoneNumberDTO);
        assertThat(phoneNumberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void get_all_numbers() {
        phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        assertThat(phoneNumberService.findAll().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void get_number() {
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        assertThat(phoneNumberService.findOne(phoneNumberDTO.getId()).isPresent()).isTrue();
    }

    @Test
    @Transactional
    void should_return_empty_optional_when_not_exist() {
        assertThat(phoneNumberService.findOne(Long.MAX_VALUE).isPresent()).isFalse();
    }

    @Test
    @Transactional
    void delete_number() {
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        int databaseSizeBeforeDelete = phoneNumberService.findAll().size();
        phoneNumberService.delete(phoneNumberDTO.getId());
        List<PhoneNumberDTO> phoneNumberList = phoneNumberService.findAll();
        assertThat(phoneNumberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}