package com.perseus.userservice.service.impl;

import com.perseus.userservice.IntegrationTest;
import com.perseus.userservice.domain.PhoneNumber;
import com.perseus.userservice.mapper.PhoneNumberMapper;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class PhoneNumberServiceImplTestIT {

    private static final String DEFAULT_NUMBER = "1230";


    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private EntityManager em;


    private PhoneNumber phoneNumber;


    public  PhoneNumber createEntity(EntityManager em) {
        PhoneNumber phoneNumber = new PhoneNumber().number(DEFAULT_NUMBER);
        return phoneNumber;
    }

    @BeforeEach
    public void initTest() {
        phoneNumber = createEntity(em);
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
        assertThat(phoneNumberService.findOne(phoneNumberDTO.getId()).isPresent()).isEqualTo(true);
    }

    @Test
    @Transactional
    void should_return_empty_optional_when_not_exist() {
        assertThat(phoneNumberService.findOne(Long.MAX_VALUE).isPresent()).isEqualTo(false);
    }

    @Test
    @Transactional
    void delete_number()  {
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.save(phoneNumberMapper.toDto(phoneNumber));
        int databaseSizeBeforeDelete = phoneNumberService.findAll().size();
        phoneNumberService.delete(phoneNumberDTO.getId());
        List<PhoneNumberDTO> phoneNumberList = phoneNumberService.findAll();
        assertThat(phoneNumberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}