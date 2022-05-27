package com.perseus.userservice.service.impl;

import com.perseus.userservice.UserServiceApplication;
import com.perseus.userservice.domain.Email;
import com.perseus.userservice.mapper.EmailMapper;
import com.perseus.userservice.service.EmailService;
import com.perseus.userservice.service.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = UserServiceApplication.class)

class EmailServiceImplTest {
    private static final String DEFAULT_MAIL = "amin@yahoo.com";

    @Autowired
    private EmailService emailService;


    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EntityManager em;


    private Email email;


    public  Email createEntity(EntityManager em) {
        Email email = new Email().mail(DEFAULT_MAIL);
        return email;
    }


    @BeforeEach
    public void initTest() {
        email = createEntity(em);
    }

    @Test
    @Transactional
    void create_new_email() {
        int databaseSizeBeforeCreate = emailService.findAll().size();
        EmailDTO emailDTO = emailMapper.toDto(email);
        emailService.save(emailDTO);
        List<EmailDTO> emailList = emailService.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate + 1);
        EmailDTO testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    void should_not_create_email_with_existing_id() {
        EmailDTO emailDTO = emailService.save(emailMapper.toDto(email));
        int databaseSizeBeforeCreate = emailService.findAll().size();
        emailService.save(emailDTO);
        List<EmailDTO> emailList = emailService.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void get_all_emails() {
        emailService.save(emailMapper.toDto(email));
        assertThat(emailService.findAll()).hasSize(1);
    }

    @Test
    @Transactional
    void get_email() {
        EmailDTO emailDTO = emailService.save(emailMapper.toDto(email));
        assertThat(emailService.findOne(emailDTO.getId()).isPresent()).isEqualTo(true);
    }

    @Test
    @Transactional
    void should_return_empty_optional_when_not_exist() {
        assertThat(emailService.findOne(Long.MAX_VALUE).isPresent()).isEqualTo(false);
    }

    @Test
    @Transactional
    void delete_email() {
        EmailDTO emailDTO = emailService.save(emailMapper.toDto(email));
        int databaseSizeBeforeDelete = emailService.findAll().size();
        emailService.delete(emailDTO.getId());
        List<EmailDTO> emailList = emailService.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}