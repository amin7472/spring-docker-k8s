package com.perseus.userservice.rest.v1;

import com.perseus.userservice.exception.BadRequestAlertException;
import com.perseus.userservice.exception.NotFoundException;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.EmailService;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public ContactDTO createNewContract(ContactDTO contactDTO) {
        log.debug("REST request to save contract : {}", contactDTO);
        if (contactDTO.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID");
        }
        contactDTO = contactService.save(contactDTO);

        prepareEmails(contactDTO.getEmails(), contactDTO.getId());
        preparePhoneNumbers(contactDTO.getPhoneNumbers(), contactDTO.getId());
        return contactDTO;
    }

    public ContactDTO getContract(Long id) {
        log.debug("REST request to get contract : {}", id);
        Optional<ContactDTO> optionalContact = contactService.findOne(id);
        return optionalContact.orElseThrow(() -> new NotFoundException("Not found contact by id : " + id));
    }

    public List<ContactDTO> getContract(String name, String lastName) {
        log.debug("REST request to get contract : {}", name);
        return contactService.findByName(name, lastName);
    }


    public void deleteContract(Long id) {
        log.debug("REST request to delete contract : {}", id);
        if (!contactService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found");
        }
        contactService.delete(id);
    }


    public void prepareEmails(List<EmailDTO> emailDTO, Long contractId) {
        if (emailDTO != null && !emailDTO.isEmpty()) {
            emailDTO.stream().forEach(email -> {
                email.setContactId(contractId);
                if (email.getId() != null) {
                    throw new BadRequestAlertException("A new email cannot already have an ID");
                }
            });
            emailService.saveAll(emailDTO);
        }
    }

    public PhoneNumberDTO addNewPhoneNumber(PhoneNumberDTO phoneNumberDTO, Long contactId) {
        log.debug("REST request to add PhoneNumber to contact: {}", contactId);
        contactService.findOne(contactId)
                .orElseThrow(() -> new NotFoundException("Not found contact by id : " + contactId));
        if (phoneNumberDTO.getId() != null) {
            throw new BadRequestAlertException("A new phoneNumber cannot already have an ID");
        }
        phoneNumberDTO.setContactId(contactId);
        return phoneNumberService.save(phoneNumberDTO);
    }

    public EmailDTO addNewEmail(EmailDTO emailDTO, Long contactId) {
        log.debug("REST request to add Email to contact : {}", contactId);
        contactService.findOne(contactId)
                .orElseThrow(() -> new NotFoundException("Not found contact by id : " + contactId));
        if (emailDTO.getId() != null) {
            throw new BadRequestAlertException("A new email cannot already have an ID");
        }
        emailDTO.setContactId(contactId);
        return emailService.save(emailDTO);
    }

    public void deleteEmail(Long emailId, Long contactId) {
        log.debug("REST request to delete Email : {} from contact : {}", emailId, contactId);
        EmailDTO emailDTO = emailService.findOne(emailId)
                .orElseThrow(() -> new NotFoundException("Not found number by id : " + contactId));
        if (!emailDTO.getContactId().equals(contactId))
            throw new NotFoundException("Not found contact by id : " + contactId);
        emailService.delete(emailId);
    }

    public void deletePhoneNumber(Long phoneNumberId, Long contactId) {
        log.debug("REST request to delete PhoneNumber : {} from contact : {}", phoneNumberId, contactId);
        PhoneNumberDTO phoneNumberDTO = phoneNumberService.findOne(phoneNumberId)
                .orElseThrow(() -> new NotFoundException("Not found number by id : " + contactId));
        if (!phoneNumberDTO.getContactId().equals(contactId))
            throw new NotFoundException("Not found contact by id : " + contactId);
        phoneNumberService.delete(phoneNumberId);
    }


    public EmailDTO updateEmail(Long contactId, Long id, EmailDTO emailDTO) {
        log.debug("REST request to update Email : {}, {}", id, emailDTO);
        emailDTO.setId(id);
        emailDTO.setContactId(contactId);
        if (emailDTO.getId() == null) {
            throw new BadRequestAlertException("Id cannot be null");
        }
        if (!Objects.equals(id, emailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID");
        }
        EmailDTO EmailDB = emailService
                .findOne(id)
                .orElseThrow(() -> new BadRequestAlertException("Not found email by id : " + id));
        if (!EmailDB.getContactId().equals(contactId))
            throw new NotFoundException("Not found contact by id : " + contactId);
        return emailService.save(emailDTO);
    }

    public PhoneNumberDTO updatePhoneNumber(Long contactId, Long id, PhoneNumberDTO phoneNumberDTO) {
        log.debug("REST request to update PhoneNumber : {}, {}", id, phoneNumberDTO);
        phoneNumberDTO.setId(id);
        phoneNumberDTO.setContactId(contactId);
        if (phoneNumberDTO.getId() == null) {
            throw new BadRequestAlertException("Id cannot be null");
        }
        if (!Objects.equals(id, phoneNumberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID");
        }
        PhoneNumberDTO phoneNumberDTODB = phoneNumberService
                .findOne(id)
                .orElseThrow(() -> new BadRequestAlertException("Not found number by id : " + id));
        if (!phoneNumberDTODB.getContactId().equals(contactId))
            throw new NotFoundException("Not found contact by id : " + contactId);
        return phoneNumberService.save(phoneNumberDTO);
    }

    public void preparePhoneNumbers(List<PhoneNumberDTO> phoneNumberDTOS, Long contractId) {
        if (phoneNumberDTOS != null && !phoneNumberDTOS.isEmpty()) {
            phoneNumberDTOS.stream().forEach(phoneNumberDTO -> {
                phoneNumberDTO.setContactId(contractId);
                if (phoneNumberDTO.getId() != null) {
                    throw new BadRequestAlertException("A new email cannot already have an ID");
                }
            });
            phoneNumberService.saveAll(phoneNumberDTOS);
        }

    }

}
