package com.perseus.userservice.rest.v1;

import com.perseus.userservice.exception.BadRequestAlertException;
import com.perseus.userservice.exception.ExceptionMessagesEnum;
import com.perseus.userservice.exception.NotFoundException;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.EmailService;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FacadeContactServiceV1 {
    private final Logger log = LoggerFactory.getLogger(FacadeContactServiceV1.class);

    private final ContactService contactService;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;

    public FacadeContactServiceV1(ContactService contactService, EmailService emailService, PhoneNumberService phoneNumberService) {
        this.contactService = contactService;
        this.emailService = emailService;
        this.phoneNumberService = phoneNumberService;
    }


    @Transactional
    public ContactDTO createNewContact(ContactDTO contactDTO) {
        log.debug("REST request to save contact : {}", contactDTO);
        if (contactDTO.getId() != null) {
            throw new BadRequestAlertException(ExceptionMessagesEnum.CONTACT_ID_SHOULD_BE_NULL.getMessage());
        }
        contactDTO = contactService.save(contactDTO);

        prepareEmails(contactDTO.getEmails(), contactDTO.getId());
        preparePhoneNumbers(contactDTO.getPhoneNumbers(), contactDTO.getId());
        return contactDTO;
    }

    @Cacheable(cacheNames = "contact-by-id", key = "#contactId")
    public ContactDTO getContact(Long contactId) {
        log.info("REST request to get contact : {}", contactId);
        Optional<ContactDTO> optionalContact = contactService.findOne(contactId);
        return optionalContact.orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId));
    }

    public List<ContactDTO> getContact(String name, String lastName) {
        log.debug("REST request to get contact : {}", name);
        return contactService.contactFilterByName(name, lastName);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public void deleteContact(Long contactId) {
        log.debug("REST request to delete contact : {}", contactId);
        if (!contactService.findOne(contactId).isPresent()) {
            throw new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId);
        }
        contactService.delete(contactId);
    }


    public void prepareEmails(List<EmailDTO> emailDTO, Long contactId) {
        if (emailDTO != null && !emailDTO.isEmpty()) {
            emailDTO.stream().forEach(email -> {
                email.setContactId(contactId);
                if (email.getId() != null) {
                    throw new BadRequestAlertException(ExceptionMessagesEnum.EMAIL_ID_SHOULD_BE_NULL.getMessage());
                }
            });
            emailService.saveAll(emailDTO);
        }
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public PhoneNumberDTO addNewPhoneNumber(PhoneNumberDTO phoneNumberDTO, Long contactId) {
        log.debug("REST request to add PhoneNumber to contact: {}", contactId);
        contactService.findOne(contactId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId));
        if (phoneNumberDTO.getId() != null) {
            throw new NotFoundException(ExceptionMessagesEnum.NUMBER_ID_SHOULD_BE_NULL.getMessage());
        }
        phoneNumberDTO.setContactId(contactId);
        return phoneNumberService.save(phoneNumberDTO);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public EmailDTO addNewEmail(EmailDTO emailDTO, Long contactId) {
        log.debug("REST request to add Email to contact : {}", contactId);
        contactService.findOne(contactId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId));
        if (emailDTO.getId() != null) {
            throw new BadRequestAlertException(ExceptionMessagesEnum.EMAIL_ID_SHOULD_BE_NULL.getMessage());
        }
        emailDTO.setContactId(contactId);
        return emailService.save(emailDTO);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public void deleteEmail(Long emailId, Long contactId) {
        log.debug("REST request to delete Email : {} from contact : {}", emailId, contactId);
        EmailDTO emailDTO = emailService.findOne(emailId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_EMAIL_BY_ID.getMessage() + contactId));
        if (!emailDTO.getContactId().equals(contactId))
            throw new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId);
        emailService.delete(emailId);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public void deletePhoneNumber(Long phoneNumberId, Long contactId) {
        log.debug("REST request to delete PhoneNumber : {} from contact : {}", phoneNumberId, contactId);
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.findOne(phoneNumberId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_NUMBER_BY_ID.getMessage() + contactId));
        if (!phoneNumberDTO.getContactId().equals(contactId))
            throw new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId);
        phoneNumberService.delete(phoneNumberId);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public EmailDTO updateEmail(Long contactId, Long id, EmailDTO emailDTO) {
        log.debug("REST request to update Email : {}, {}", id, emailDTO);
        emailDTO.setId(id);
        emailDTO.setContactId(contactId);
        EmailDTO emailDB = emailService
                .findOne(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_EMAIL_BY_ID.getMessage() + id));
        if (!emailDB.getContactId().equals(contactId))
            throw new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId);
        return emailService.save(emailDTO);
    }

    @CacheEvict(cacheNames = "contact-by-id", key = "#contactId")
    public PhoneNumberDTO updatePhoneNumber(Long contactId, Long id, PhoneNumberDTO phoneNumberDTO) {
        log.debug("REST request to update PhoneNumber : {}, {}", id, phoneNumberDTO);
        phoneNumberDTO.setId(id);
        phoneNumberDTO.setContactId(contactId);
        PhoneNumberDTO phoneNumberDTODB = phoneNumberService
                .findOne(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_NUMBER_BY_ID.getMessage() + id));
        if (!phoneNumberDTODB.getContactId().equals(contactId))
            throw new NotFoundException(ExceptionMessagesEnum.NOT_FOUND_CONTACT_BY_ID.getMessage() + contactId);
        return phoneNumberService.save(phoneNumberDTO);
    }

    public void preparePhoneNumbers(List<PhoneNumberDTO> phoneNumberDTOS, Long contactId) {
        if (phoneNumberDTOS != null && !phoneNumberDTOS.isEmpty()) {
            phoneNumberDTOS.stream().forEach(phoneNumberDTO -> {
                phoneNumberDTO.setContactId(contactId);
                if (phoneNumberDTO.getId() != null) {
                    throw new BadRequestAlertException(ExceptionMessagesEnum.NUMBER_ID_SHOULD_BE_NULL.getMessage());
                }
            });
            phoneNumberService.saveAll(phoneNumberDTOS);
        }
    }

}
