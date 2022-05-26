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
class ContactServiceImplTest {

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

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return contact;
    }

    @Test
    @Transactional
    void createContact() {
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
    void createContactWithExistingId() {
        ContactDTO contactDTO = contactMapper.toDto(contact);
        contactDTO = contactService.save(contactDTO);
        int databaseSizeBeforeCreate = contactService.findAll(Pageable.unpaged()).getContent().size();
        contactService.save(contactDTO);
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContacts() {
        contactService.save(contactMapper.toDto(contact));
        assertThat(contactService.findAll(Pageable.unpaged()).getContent()).hasSize(1);

    }

    @Test
    @Transactional
    void getContact() {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        assertThat(contactService.findOne(id).isPresent()).isEqualTo(true);
    }

    @Test
    @Transactional
    void getContactByName() {
        String name = contactService.save(contactMapper.toDto(contact)).getFirstName();
        assertThat(contactService.findOneByName(name).isPresent()).isEqualTo(true);
    }

    @Test
    @Transactional
    void getNonExistingContact() {
        assertThat(contactService.findOne(Long.MAX_VALUE).isPresent()).isEqualTo(false);
    }


    @Test
    @Transactional
    void deleteContact() {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        int databaseSizeBeforeDelete = contactService.findAll(Pageable.unpaged()).getContent().size();
        contactService.delete(id);
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}