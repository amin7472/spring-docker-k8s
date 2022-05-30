package com.perseus.userservice.rest.v1;

import com.perseus.userservice.exception.BadRequestAlertException;
import com.perseus.userservice.exception.NotFoundException;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.EmailService;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FacadeContactServiceV1UnitTest {
    private static final String DEFAULT_FIRST_NAME = "Amin";
    private static final String DEFAULT_LAST_NAME = "Ahmadi";
    private static final Long DEFAULT_CONTACT_ID = 10L;
    private static final String DEFAULT_PHONE_NUMBER = "2055550125";
    private static final String UPDATE_PHONE_NUMBER = "2055553639";
    private static final Long DEFAULT_PHONE_ID = 12L;

    private static final String DEFAULT_EMAIL = "amin_bb@yahoo.com";
    private static final Long DEFAULT_EMAIL_ID = 12L;

    private static final String UPDATE_EMAIL = "amin_update@yahoo.com";

    private static final String INVALID_EMAIL = "amin_update@yahoo.com";
    private static final String INVALID_PHONE_NUMBER = "2055553639";


    @Mock
    ContactService contactService;
    @Mock
    EmailService emailService;
    @Mock
    PhoneNumberService phoneNumberService;

    @InjectMocks
    FacadeContactServiceV1 facadeContactServiceV1;


    @Test
    public void save_contact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setLastName(DEFAULT_FIRST_NAME);
        ContactDTO contactAfterSave = new ContactDTO();
        contactAfterSave.setFirstName(contactDTO.getFirstName());
        contactAfterSave.setId(DEFAULT_CONTACT_ID);
        given(contactService.save(any())).willReturn(contactAfterSave);
        facadeContactServiceV1.createNewContact(contactDTO);
        assertThat(contactAfterSave.getId()).isEqualTo(DEFAULT_CONTACT_ID);

    }

    @Test
    public void should_throws_bad_request_exception_because_of_contact_id() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(DEFAULT_CONTACT_ID);
        contactDTO.setLastName(DEFAULT_LAST_NAME);
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.createNewContact(contactDTO);
        });
    }

    @Test
    public void should_throws_bad_request_exception_because_of_email_id_is_not_null() {
        ContactDTO contactDTO = new ContactDTO();
        List<EmailDTO> emailDTOS = new ArrayList<>();
        emailDTOS.add(new EmailDTO(1l, DEFAULT_EMAIL));
        contactDTO.setEmails(emailDTOS);
        given(contactService.save(any())).willReturn(contactDTO);
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.createNewContact(contactDTO);
        });
    }

    @Test
    public void should_throws_bad_request_exception_because_of_number_id_is_not_null() {
        ContactDTO contactDTO = new ContactDTO();
        List<PhoneNumberDTO> phoneNumberDTOS = new ArrayList<>();
        phoneNumberDTOS.add(new PhoneNumberDTO(1l, DEFAULT_PHONE_NUMBER));
        contactDTO.setPhoneNumbers(phoneNumberDTOS);
        given(contactService.save(any())).willReturn(contactDTO);
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.createNewContact(contactDTO);
        });
    }

    @Test
    public void should_return_contact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(10L);
        given(contactService.findOne(any())).willReturn(Optional.of(contactDTO));
        assertThat(facadeContactServiceV1.getContact(DEFAULT_CONTACT_ID)).isEqualTo(contactDTO);
    }

    @Test
    public void should_throws_not_found_exception() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.getContact(any());
        });
    }


    @Test
    public void should_return_contact_by_name() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(DEFAULT_CONTACT_ID);
        contactDTO.setFirstName(DEFAULT_FIRST_NAME);
        contactDTO.setLastName(DEFAULT_LAST_NAME);

        given(contactService.contactFilterByName(any(), any())).willReturn(Arrays.asList(contactDTO));

        assertThat(facadeContactServiceV1.getContact(any(), any())).contains(contactDTO);
    }

    @Test
    public void should_return_empty_list_by_name() {
        given(contactService.contactFilterByName(any(), any())).willReturn(new ArrayList<>());
        assertThat(facadeContactServiceV1.getContact(any(), any())).isEmpty();
    }

    @Test
    public void should_delete_contact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(DEFAULT_CONTACT_ID);
        contactDTO.setFirstName(DEFAULT_FIRST_NAME);
        contactDTO.setLastName(DEFAULT_LAST_NAME);
        given(contactService.findOne(any())).willReturn(Optional.of(contactDTO));
        facadeContactServiceV1.deleteContact(any());
    }

    @Test
    public void should_throws_not_found_exception_on_delete() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deleteContact(any());
        });
    }


    @Test
    public void should_add_new_phone() {
        given(contactService.findOne(any())).willReturn(Optional.of(new ContactDTO()));
        facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(null, DEFAULT_PHONE_NUMBER), any());
    }

    @Test
    public void should_add_new_phone_throws_not_found_exception_because_of_not_exit_contact_id() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(null, DEFAULT_PHONE_NUMBER), any());
        });
    }


    @Test
    public void should_add_new_email() {
        given(contactService.findOne(any())).willReturn(Optional.of(new ContactDTO()));
        facadeContactServiceV1.addNewEmail(new EmailDTO(null, DEFAULT_PHONE_NUMBER), any());
    }

    @Test
    public void should_add_new_email_throws_not_found_exception_because_of_not_exit_contact_id() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.addNewEmail(new EmailDTO(null, DEFAULT_EMAIL), any());
        });
    }


    @Test
    public void should_delete_email() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(10L);
        given(emailService.findOne(any())).willReturn(Optional.of(emailDTO));
        facadeContactServiceV1.deleteEmail(anyLong(), DEFAULT_CONTACT_ID);
    }


    @Test
    public void should_delete_email_throws_not_found_exception_because_of_email_id_is_not_exist() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(DEFAULT_CONTACT_ID);
        given(emailService.findOne(any())).willReturn(Optional.of(emailDTO));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deleteEmail(Long.MAX_VALUE, any());
        });
    }

    @Test
    public void should_delete_number() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(DEFAULT_CONTACT_ID);
        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDTO));
        facadeContactServiceV1.deletePhoneNumber(anyLong(), DEFAULT_CONTACT_ID);
    }


    @Test
    public void should_delete_phone_number_throws_not_found_exception_because_of_number_id_is_not_exist() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(DEFAULT_CONTACT_ID);
        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDTO));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deletePhoneNumber(Long.MAX_VALUE, any());
        });
    }

    @Test
    public void should_delete_phone_number_throws_not_found_exception_because_of_number_not_found() {
        given(phoneNumberService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deletePhoneNumber(Long.MAX_VALUE, any());
        });
    }

    @Test
    public void should_update_email() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(DEFAULT_CONTACT_ID);
        emailDTO.setMail(DEFAULT_EMAIL);
        emailDTO.setId(11L);

        EmailDTO emailDb = new EmailDTO();
        emailDb.setContactId(DEFAULT_CONTACT_ID);
        emailDb.setMail(DEFAULT_EMAIL);
        emailDb.setId(11L);

        given(emailService.findByIdAndContactId(any(), any())).willReturn(Optional.of(emailDb));
        given(emailService.save(any())).willReturn(emailDTO);
        assertThat(facadeContactServiceV1.updateEmail(DEFAULT_CONTACT_ID, 11L, emailDTO)).isEqualTo(emailDTO);
    }

    @Test
    public void should_update_number() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(10L);
        phoneNumberDTO.setNumber(DEFAULT_PHONE_NUMBER);
        phoneNumberDTO.setId(11L);

        PhoneNumberDTO phoneNumberDb = new PhoneNumberDTO();
        phoneNumberDb.setContactId(10L);
        phoneNumberDb.setNumber(DEFAULT_PHONE_NUMBER);
        phoneNumberDb.setId(11L);

        given(phoneNumberService.findByIdAndContactId(any(), any())).willReturn(Optional.of(phoneNumberDb));
        given(phoneNumberService.save(any())).willReturn(phoneNumberDTO);
        assertThat(facadeContactServiceV1.updatePhoneNumber(DEFAULT_CONTACT_ID, 11L, phoneNumberDTO)).isEqualTo(phoneNumberDTO);
    }

    @Test
    public void should_add_new_phone_throws_bad_request_exception_because_of_number_id_is_not_null() {
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(DEFAULT_CONTACT_ID, DEFAULT_PHONE_NUMBER), DEFAULT_CONTACT_ID);
        });
    }

    @Test
    public void should_add_new_email_throws_bad_request_exception_because_of_number_id_is_not_null() {
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.addNewEmail(new EmailDTO(DEFAULT_CONTACT_ID, DEFAULT_EMAIL), DEFAULT_CONTACT_ID);
        });
    }

    @Test
    public void should_update_email_throws_not_found_exception_because_of_contact_is_not_exist() {
        EmailDTO emailDb = new EmailDTO();
        emailDb.setContactId(DEFAULT_CONTACT_ID);
        emailDb.setMail(DEFAULT_EMAIL);
        emailDb.setId(DEFAULT_EMAIL_ID);
        given(emailService.findByIdAndContactId(DEFAULT_EMAIL_ID, DEFAULT_CONTACT_ID)).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.updateEmail(DEFAULT_CONTACT_ID, DEFAULT_EMAIL_ID, new EmailDTO(null, UPDATE_EMAIL));
        });
    }

    @Test
    public void should_update_number_throws_not_found_exception_because_of_contact_is_not_exist() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(DEFAULT_CONTACT_ID);
        phoneNumberDTO.setNumber(DEFAULT_PHONE_NUMBER);
        phoneNumberDTO.setId(DEFAULT_PHONE_ID);
        given(phoneNumberService.findByIdAndContactId(any(), any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.updatePhoneNumber(DEFAULT_CONTACT_ID, DEFAULT_PHONE_ID, new PhoneNumberDTO(null, UPDATE_PHONE_NUMBER));
        });
    }


}