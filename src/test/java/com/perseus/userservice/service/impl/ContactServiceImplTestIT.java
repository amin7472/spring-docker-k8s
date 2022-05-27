package com.perseus.userservice.service.impl;

import com.perseus.userservice.IntegrationTest;
import com.perseus.userservice.domain.Contact;
import com.perseus.userservice.mapper.ContactMapper;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.dto.ContactDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ContactServiceImplTestIT {

    private static final String DEFAULT_FIRST_NAME = "Amin";

    private static final String DEFAULT_LAST_NAME = "Ahmadi";

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private EntityManager em;
    private Contact contact;

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    public Contact createEntity(EntityManager em) {
        Contact contact = new Contact().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return contact;
    }

    @Test
    @Transactional
    void create_new_contract() {
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
        String name = contactService.save(contactMapper.toDto(contact)).getFirstName();
        assertThat(contactService.findByName(name).isEmpty()).isEqualTo(false);
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