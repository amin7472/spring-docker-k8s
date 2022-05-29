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
        contactDTO.setLastName("amin");
        ContactDTO contactAfterSave = new ContactDTO();
        contactAfterSave.setFirstName(contactDTO.getFirstName());
        contactAfterSave.setId(10L);
        given(contactService.save(any())).willReturn(contactAfterSave);
        facadeContactServiceV1.createNewContact(contactDTO);
        assertThat(contactAfterSave.getId()).isEqualTo(10L);

    }

    @Test
    public void should_throws_bad_request_exception_because_of_contact_id() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(10L);
        contactDTO.setLastName("amin");
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.createNewContact(contactDTO);
        });
    }

    @Test
    public void should_throws_bad_request_exception_because_of_email_id_is_not_null() {
        ContactDTO contactDTO = new ContactDTO();
        List<EmailDTO> emailDTOS = new ArrayList<>();
        emailDTOS.add(new EmailDTO(1l, "test"));
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
        phoneNumberDTOS.add(new PhoneNumberDTO(1l, "test"));
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
        assertThat(facadeContactServiceV1.getContact(10L)).isEqualTo(contactDTO);


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
        contactDTO.setId(10L);
        contactDTO.setFirstName("amin");
        contactDTO.setLastName("ahmadi");

        given(contactService.findByName(any(), any())).willReturn(Arrays.asList(contactDTO));

        assertThat(facadeContactServiceV1.getContact(any(), any())).contains(contactDTO);
    }

    @Test
    public void should_return_empty_list_by_name() {
        given(contactService.findByName(any(), any())).willReturn(new ArrayList<>());
        assertThat(facadeContactServiceV1.getContact(any(), any())).isEmpty();
    }

    @Test
    public void should_delete_contact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(10L);
        contactDTO.setFirstName("amin");
        contactDTO.setLastName("ahmadi");
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
        facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(null, "01111"), any());
    }

    @Test
    public void should_add_new_phone_throws_not_found_exception_because_of_not_exit_contact_id() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(null, "01111"), any());
        });
    }

    @Test
    public void should_add_new_phone_throws_bad_request_exception_because_of_number_id_is_not_null() {
        given(contactService.findOne(any())).willReturn(Optional.of(new ContactDTO()));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.addNewPhoneNumber(new PhoneNumberDTO(10L, "01111"), any());
        });
    }

    @Test
    public void should_add_new_email() {
        given(contactService.findOne(any())).willReturn(Optional.of(new ContactDTO()));
        facadeContactServiceV1.addNewEmail(new EmailDTO(null, "01111"), any());
    }

    @Test
    public void should_add_new_email_throws_not_found_exception_because_of_not_exit_contact_id() {
        given(contactService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.addNewEmail(new EmailDTO(null, "a@yahoo.com"), any());
        });
    }

    @Test
    public void should_add_new_email_throws_bad_request_exception_because_of_number_id_is_not_null() {
        given(contactService.findOne(any())).willReturn(Optional.of(new ContactDTO()));
        assertThrows(BadRequestAlertException.class, () -> {
            facadeContactServiceV1.addNewEmail(new EmailDTO(10L, "a@yahoo.com"), any());
        });
    }


    @Test
    public void should_delete_email() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(10L);
        given(emailService.findOne(any())).willReturn(Optional.of(emailDTO));
        facadeContactServiceV1.deleteEmail(anyLong(), 10L);
    }


    @Test
    public void should_delete_email_throws_not_found_exception_because_of_email_id_is_not_exist() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(10L);
        given(emailService.findOne(any())).willReturn(Optional.of(emailDTO));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deleteEmail(11L, any());
        });
    }

    @Test
    public void should_delete_number() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(10L);
        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDTO));
        facadeContactServiceV1.deletePhoneNumber(anyLong(), 10L);
    }


    @Test
    public void should_delete_phone_number_throws_not_found_exception_because_of_number_id_is_not_exist() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(10L);
        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDTO));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deletePhoneNumber(11L, any());
        });
    }

    @Test
    public void should_delete_phone_number_throws_not_found_exception_because_of_number_not_found() {
        given(phoneNumberService.findOne(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.deletePhoneNumber(11L, any());
        });
    }

    @Test
    public void should_update_email() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContactId(10L);
        emailDTO.setMail("new@yahoo.com");
        emailDTO.setId(11L);

        EmailDTO emailDb = new EmailDTO();
        emailDb.setContactId(10L);
        emailDb.setMail("db@yahoo.comm");
        emailDb.setId(11L);

        given(emailService.findOne(any())).willReturn(Optional.of(emailDb));
        given(emailService.save(any())).willReturn(emailDTO);
        assertThat(facadeContactServiceV1.updateEmail(10L, 11L, emailDTO)).isEqualTo(emailDTO);
    }

    @Test
    public void should_update_email_throws_not_found_exception_because_of_contact_is_not_exist() {
        EmailDTO emailDb = new EmailDTO();
        emailDb.setContactId(10L);
        emailDb.setMail("db@yahoo.comm");
        emailDb.setId(11L);
        given(emailService.findOne(any())).willReturn(Optional.of(emailDb));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.updateEmail(15L, any(),new EmailDTO(null,"test"));
        });
    }
    @Test
    public void should_update_number() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(10L);
        phoneNumberDTO.setNumber("123");
        phoneNumberDTO.setId(11L);

        PhoneNumberDTO phoneNumberDb = new PhoneNumberDTO();
        phoneNumberDb.setContactId(10L);
        phoneNumberDb.setNumber("1233");
        phoneNumberDb.setId(11L);

        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDb));
        given(phoneNumberService.save(any())).willReturn(phoneNumberDTO);
        assertThat(facadeContactServiceV1.updatePhoneNumber(10L, 11L, phoneNumberDTO)).isEqualTo(phoneNumberDTO);
    }


    @Test
    public void should_update_number_throws_not_found_exception_because_of_contact_is_not_exist() {
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
        phoneNumberDTO.setContactId(10L);
        phoneNumberDTO.setNumber("123");
        phoneNumberDTO.setId(11L);
        given(phoneNumberService.findOne(any())).willReturn(Optional.of(phoneNumberDTO));
        assertThrows(NotFoundException.class, () -> {
            facadeContactServiceV1.updatePhoneNumber(15L, any(),new PhoneNumberDTO(null,"152"));
        });
    }
}