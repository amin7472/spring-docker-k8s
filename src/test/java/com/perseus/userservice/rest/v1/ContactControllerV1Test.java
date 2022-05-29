package com.perseus.userservice.rest.v1;

import com.perseus.userservice.TestUtil;
import com.perseus.userservice.UserServiceApplication;
import com.perseus.userservice.domain.Contact;
import com.perseus.userservice.mapper.ContactMapper;
import com.perseus.userservice.repository.ContactRepository;
import com.perseus.userservice.repository.EmailRepository;
import com.perseus.userservice.repository.PhoneNumberRepository;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ContactControllerV1Test {

    private static final String DEFAULT_FIRST_NAME = "Amin";
    private static final String UPDATED_FIRST_NAME = "Ahmadi";

    private static final String DEFAULT_LAST_NAME = "Nima";
    private static final String UPDATED_LAST_NAME = "Ahmadi";

    private static final String NEW_EMAIL_ADDRESS = "amin@yahoo.com";
    private static final String NEW_PHONE_NUMBER = "0910005000";

    private static final String UPDATED_EMAIL_NAME = "aminNew@yahoo.com";
    private static final String UPDATED_PHONE_NUMBER = "09120525";


    private static final String ENTITY_API_URL = "/v1/contacts";

    private static final String ENTITY_API_URL_FIND_BY_ID = ENTITY_API_URL + "/findById/{id}";
    private static final String ENTITY_API_URL_FIND_BY_NAME = ENTITY_API_URL + "/findByName/{name}/{lastName}";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_API_ADD_EMAIL_URL_ID = ENTITY_API_URL_ID + "/email";
    private static final String ENTITY_API_ADD_PHONE_NUMBER_URL_ID = ENTITY_API_URL_ID + "/phoneNumber";

    private static final String ENTITY_API_UPDATE_EMAIL_URL_ID = ENTITY_API_ADD_EMAIL_URL_ID + "/{id}";
    private static final String ENTITY_API_UPDATE_PHONE_NUMBER_URL_ID = ENTITY_API_ADD_PHONE_NUMBER_URL_ID + "/{id}";

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private EntityManager em;
    @Autowired
    private MockMvc restContactMockMvc;

    @Autowired
    private CacheManager cacheManager;


    private Contact contact;


    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return contact;
    }


    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
        contactRepository.deleteAll();
        emailRepository.deleteAll();
        phoneNumberRepository.deleteAll();
    }

    @Test
    void create_new_contact() throws Exception {
        int databaseSizeBeforeCreate = contactService.findAll(Pageable.unpaged()).getContent().size();
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
                .andExpect(status().isCreated());
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        ContactDTO testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }


    @Test
    void should_return_contact_by_id() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc
                .perform(get(ENTITY_API_URL_FIND_BY_ID, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));

    }

    @Test
    void should_exist_contact_in_cache() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc.perform(get(ENTITY_API_URL_FIND_BY_ID, id));
        ContactDTO contactOnCache = (ContactDTO) cacheManager.getCache("contact-by-id").get(id).get();
        assertThat(contactOnCache.getFirstName()).isEqualTo(contact.getFirstName());
    }

    @Test
    void should_not_exist_contact_in_cache_after_deleted_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc.perform(get(ENTITY_API_URL_FIND_BY_ID, id));
        restContactMockMvc.perform(delete(ENTITY_API_URL_ID, id).accept(MediaType.APPLICATION_JSON));
        assertThat(cacheManager.getCache("contact-by-id").get(id)).isEqualTo(null);
    }

    @Test
    void should_not_exist_contact_in_cache_after_add_email() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc.perform(get(ENTITY_API_URL_FIND_BY_ID, id));

        ContactDTO contactOnCache = (ContactDTO) cacheManager.getCache("contact-by-id").get(id).get();
        assertThat(contactOnCache.getFirstName()).isEqualTo(contact.getFirstName());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());

        assertThat(cacheManager.getCache("contact-by-id").get(id)).isEqualTo(null);
    }


    @Test
    void should_not_exist_contact_in_cache_after_add_number() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc.perform(get(ENTITY_API_URL_FIND_BY_ID, id));

        ContactDTO contactOnCache = (ContactDTO) cacheManager.getCache("contact-by-id").get(id).get();
        assertThat(contactOnCache.getFirstName()).isEqualTo(contact.getFirstName());

        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());

        assertThat(cacheManager.getCache("contact-by-id").get(id)).isEqualTo(null);
    }


    @Test
    void should_one_time_hit_to_db() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        restContactMockMvc
                .perform(get(ENTITY_API_URL_FIND_BY_ID, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));

    }

    @Test
    void should_status_code_be_not_found() throws Exception {
        restContactMockMvc.perform(get(ENTITY_API_URL_FIND_BY_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }


    @Test
    void should_return_contact_by_name() throws Exception {
        ContactDTO contactDTO = contactService.save(contactMapper.toDto(contact));
        restContactMockMvc
                .perform(get(ENTITY_API_URL_FIND_BY_NAME, contactDTO.getFirstName(), contactDTO.getLastName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].firstName").value(DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.[*].lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    void should_get_bad_request_during_add_existed_contact() throws Exception {
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);
        int databaseSizeBeforeCreate = contactService.findAll(Pageable.unpaged()).getContent().size();
        restContactMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
                .andExpect(status().isBadRequest());
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void should_delete_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        int databaseSizeBeforeDelete = contactService.findAll(Pageable.unpaged()).getContent().size();
        restContactMockMvc
                .perform(delete(ENTITY_API_URL_ID, id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        List<ContactDTO> contactList = contactService.findAll(Pageable.unpaged()).getContent();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    void add_email_to_existed_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        List<EmailDTO> emailDTOS = contactService.findOne(id).get().getEmails();
        assertThat(emailDTOS.stream().anyMatch(email -> email.getMail().equals(NEW_EMAIL_ADDRESS)))
                .isTrue();
    }

    @Test
    void delete_email_from_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        List<EmailDTO> emailDTOS = contactService.findOne(id).get().getEmails();
        Optional<EmailDTO> optionalEmailDTO = emailDTOS
                .stream()
                .filter(email -> email.getMail().equals(NEW_EMAIL_ADDRESS)).findFirst();
        assertThat(optionalEmailDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_EMAIL_URL_ID, id, optionalEmailDTO.get().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        List<EmailDTO> emailDTOSAfterDelete = contactService.findOne(id).get().getEmails();
        assertThat(emailDTOSAfterDelete).hasSize(0);
    }

    @Test
    void update_existed_email() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        List<EmailDTO> emailDTOS = contactService.findOne(id).get().getEmails();
        Optional<EmailDTO> optionalEmailDTO = emailDTOS
                .stream()
                .filter(email -> email.getMail().equals(NEW_EMAIL_ADDRESS)).findFirst();
        assertThat(optionalEmailDTO.isPresent()).isTrue();
        emailDTO.setMail(UPDATED_EMAIL_NAME);
        restContactMockMvc
                .perform(
                        put(ENTITY_API_UPDATE_EMAIL_URL_ID, id, optionalEmailDTO.get().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        Optional<EmailDTO> optionalEmailDTO1 = contactService.findOne(id).get()
                .getEmails().stream().filter(email -> email.getId().equals(optionalEmailDTO.get().getId()))
                .findFirst();
        assertThat(optionalEmailDTO1.isPresent()).isTrue();
        assertThat(optionalEmailDTO1.get().getMail()).isEqualTo(UPDATED_EMAIL_NAME);
    }

    @Test
    void update_existed_number() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());
        List<PhoneNumberDTO> phoneNumberDTOS = contactService.findOne(id).get().getPhoneNumbers();
        Optional<PhoneNumberDTO> optionalPhoneNumberDTO = phoneNumberDTOS
                .stream()
                .filter(email -> email.getNumber().equals(NEW_PHONE_NUMBER)).findFirst();
        assertThat(optionalPhoneNumberDTO.isPresent()).isTrue();
        phoneNumberDTO.setNumber(UPDATED_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        put(ENTITY_API_UPDATE_PHONE_NUMBER_URL_ID, id, optionalPhoneNumberDTO.get().getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());
        Optional<PhoneNumberDTO> optionalPhoneNumberDTO1 = contactService.findOne(id).get()
                .getPhoneNumbers().stream().filter(phoneNumberDTO1 -> phoneNumberDTO1.getId().equals(optionalPhoneNumberDTO.get().getId()))
                .findFirst();
        assertThat(optionalPhoneNumberDTO1.isPresent()).isTrue();
        assertThat(optionalPhoneNumberDTO1.get().getNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    void should_return_not_found_status_code_during_set_invalid_contact_id_on_email_deleting() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        List<EmailDTO> emailDTOS = contactService.findOne(id).get().getEmails();
        Optional<EmailDTO> optionalEmailDTO = emailDTOS
                .stream()
                .filter(email -> email.getMail().equals(NEW_EMAIL_ADDRESS)).findFirst();
        assertThat(optionalEmailDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_EMAIL_URL_ID, Long.MAX_VALUE, optionalEmailDTO.get().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_status_code_during_set_invalid_email_id() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setMail(NEW_EMAIL_ADDRESS);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_EMAIL_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(emailDTO))
                )
                .andExpect(status().isOk());
        List<EmailDTO> emailDTOS = contactService.findOne(id).get().getEmails();
        Optional<EmailDTO> optionalEmailDTO = emailDTOS
                .stream()
                .filter(email -> email.getMail().equals(NEW_EMAIL_ADDRESS)).findFirst();
        assertThat(optionalEmailDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_EMAIL_URL_ID, id, Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_number_from_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());

        List<PhoneNumberDTO> phoneNumberDTOS = contactService.findOne(id).get().getPhoneNumbers();
        Optional<PhoneNumberDTO> optionalPhoneNumberDTO = phoneNumberDTOS
                .stream()
                .filter(email -> email.getNumber().equals(NEW_PHONE_NUMBER)).findFirst();
        assertThat(optionalPhoneNumberDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_PHONE_NUMBER_URL_ID, id, optionalPhoneNumberDTO.get().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        List<PhoneNumberDTO> emailDTOSAfterDelete = contactService.findOne(id).get().getPhoneNumbers();
        assertThat(emailDTOSAfterDelete).hasSize(0);
    }


    @Test
    void should_return_not_found_status_code_during_set_invalid_contact_id_on_number_deleting() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());

        List<PhoneNumberDTO> phoneNumberDTOS = contactService.findOne(id).get().getPhoneNumbers();
        Optional<PhoneNumberDTO> optionalPhoneNumberDTO = phoneNumberDTOS
                .stream()
                .filter(email -> email.getNumber().equals(NEW_PHONE_NUMBER)).findFirst();
        assertThat(optionalPhoneNumberDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_PHONE_NUMBER_URL_ID, Long.MAX_VALUE, optionalPhoneNumberDTO.get().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_status_code_during_set_invalid_number_id_on_number_deleting() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());

        List<PhoneNumberDTO> phoneNumberDTOS = contactService.findOne(id).get().getPhoneNumbers();
        Optional<PhoneNumberDTO> optionalPhoneNumberDTO = phoneNumberDTOS
                .stream()
                .filter(email -> email.getNumber().equals(NEW_PHONE_NUMBER)).findFirst();
        assertThat(optionalPhoneNumberDTO.isPresent()).isTrue();
        restContactMockMvc
                .perform(delete(ENTITY_API_UPDATE_PHONE_NUMBER_URL_ID, id, Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void add_number_to_existed_contact() throws Exception {
        Long id = contactService.save(contactMapper.toDto(contact)).getId();
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setNumber(NEW_PHONE_NUMBER);
        restContactMockMvc
                .perform(
                        post(ENTITY_API_ADD_PHONE_NUMBER_URL_ID, id, phoneNumberDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(phoneNumberDTO))
                )
                .andExpect(status().isOk());
        List<PhoneNumberDTO> emailDTOS = contactService.findOne(id).get().getPhoneNumbers();
        assertThat(emailDTOS.stream().anyMatch(phoneNumberDTO1 -> phoneNumberDTO1.getNumber().equals(NEW_PHONE_NUMBER)))
                .isTrue();
    }

}