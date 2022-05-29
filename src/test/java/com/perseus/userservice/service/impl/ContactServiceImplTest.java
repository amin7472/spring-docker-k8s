package com.perseus.userservice.service.impl;

import com.perseus.userservice.UserServiceApplication;
import com.perseus.userservice.domain.Contact;
import com.perseus.userservice.mapper.ContactMapper;
import com.perseus.userservice.repository.ContactRepository;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.dto.ContactDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
class ContactServiceImplTest {

    private static final String DEFAULT_FIRST_NAME = "Amin";

    private static final String DEFAULT_LAST_NAME = "Ahmadi";
    private static final String DEFAULT_LAST_NAME_EXAMPLE = "Karimi";

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactMapper contactMapper;

    private Contact contact;

    @BeforeEach
    public void initTest() {
        contactRepository.deleteAll();
        contact = createEntity();
    }

    public Contact createEntity() {
        Contact contact = new Contact().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return contact;
    }

    @Test
    @Transactional
    void create_new_contact() {
        int databaseSizeBeforeCreate = contactService.findAll(Pageable.unpaged()).getContent().size();
        contactService.save(contactMapper.toDto(contact));
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        ContactDTO testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void should_not_create_contact_with_existing_id() {
        ContactDTO contactDTO = contactMapper.toDto(contact);
        contactDTO = contactService.save(contactDTO);
        int databaseSizeBeforeCreate = contactService.findAll(Pageable.unpaged()).getContent().size();
        contactService.save(contactDTO);
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void get_all_contacts() {
        contactService.save(contactMapper.toDto(contact));
        assertThat(contactService.findAll(Pageable.unpaged()).getContent()).hasSize(1);

    }

    @Test
    @Transactional
    void get_one_contact() {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        assertThat(contactService.findOne(id).isPresent()).isEqualTo(true);
    }

    @Test
    @Transactional
    void get_contact_by_name() {
        ContactDTO contactDTO = contactService.save(contactMapper.toDto(contact));
        assertThat(contactService.findByName(contactDTO.getFirstName(), contactDTO.getLastName()).isEmpty()).isEqualTo(false);
    }

    @Test
    @Transactional
    void get_contact_by_name_without_last_name() {
        ContactDTO contactDTO1 = contactService.save(contactMapper.toDto(contact));
        contact.setLastName(DEFAULT_LAST_NAME_EXAMPLE);
        ContactDTO contactDTO2 = contactService.save(contactMapper.toDto(contact));
        assertThat(contactService.findByName(contactDTO1.getFirstName(), null).size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void get_contact_by_name_with_last_name() {
        ContactDTO contactDTO1 = contactService.save(contactMapper.toDto(contact));
        contact.setLastName(DEFAULT_LAST_NAME_EXAMPLE);
        ContactDTO contactDTO2 = contactService.save(contactMapper.toDto(contact));
        assertThat(contactService.findByName(contactDTO1.getFirstName(), contactDTO2.getLastName()).contains(contactDTO2)).isEqualTo(true);
    }


    @Test
    @Transactional
    void should_return_empty_optional_when_not_exist() {
        assertThat(contactService.findOne(Long.MAX_VALUE).isPresent()).isEqualTo(false);
    }


    @Test
    @Transactional
    void delete_contact() {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        int databaseSizeBeforeDelete = contactService.findAll(Pageable.unpaged()).getContent().size();
        contactService.delete(id);
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}