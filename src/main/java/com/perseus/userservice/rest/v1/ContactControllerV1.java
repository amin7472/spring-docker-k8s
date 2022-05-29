package com.perseus.userservice.rest.v1;


import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/contacts")
public class ContactControllerV1 {

    private final FacadeContactServiceV1 facadeContactServiceV1;

    public ContactControllerV1(FacadeContactServiceV1 facadeContactServiceV1) {
        this.facadeContactServiceV1 = facadeContactServiceV1;
    }

    /**
     * {@code POST} : Create a new contact.
     *
     * @param contactDTO the contactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactDTO,
     * or with status {@code 400 (Bad Request)} if the contact has already an ID.
     */
    @PostMapping()
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO result = facadeContactServiceV1.createNewContact(contactDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * {@code POST /:id/email} : add new email to an existing contact.
     *
     * @param id       the id of the contactDTO to save.
     * @param emailDTO the contactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactDTO,
     * or with status {@code 400 (Bad Request)} if the contactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactDTO couldn't be updated.
     */
    @PostMapping("/{id}/email")
    public ResponseEntity<EmailDTO> addNewEmail(
            @PathVariable(value = "id") final Long id,
            @RequestBody EmailDTO emailDTO
    ) {
        EmailDTO result = facadeContactServiceV1.addNewEmail(emailDTO, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code PUT  /:id/email} : add new email to an existing contact.
     *
     * @param contactId the id of the contactDTO to save.
     * @param emailDTO  the contactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailDTO,
     * or with status {@code 400 (Bad Request)} if the emailId or contactId  is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailDTO couldn't be updated.
     */
    @PutMapping("/{contactId}/email/{emailId}")
    public ResponseEntity<EmailDTO> updateEmail(
            @PathVariable(value = "contactId") Long contactId,
            @PathVariable(value = "emailId") Long emailId,
            @RequestBody EmailDTO emailDTO
    ) {
        EmailDTO result = facadeContactServiceV1.updateEmail(contactId, emailId, emailDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * {@code POST  /:id/phoneNumber} : add new number to an existing contact.
     *
     * @param contactId             the id of the contactDTO that should add number to it.
     * @param phoneNumberDTO the phoneNumberDTO to add.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneNumberDTO,
     * or with status {@code 400 (Bad Request)} if the contactId is not valid or phoneNumberDTO.id is not null,
     * or with status {@code 500 (Internal Server Error)} if the phoneNumberDTO couldn't be added.
     */
    @PostMapping("/{contactId}/phoneNumber")
    public ResponseEntity<PhoneNumberDTO> addNewPhoneNumber(
            @PathVariable(value = "contactId") Long contactId,
            @RequestBody PhoneNumberDTO phoneNumberDTO
    ) {
        PhoneNumberDTO result = facadeContactServiceV1.addNewPhoneNumber(phoneNumberDTO, contactId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code POST  /:id/phoneNumber} : add new number to an existing contact.
     *
     * @param contactId      the id of the contactDTO.
     * @param numberId       the id of the phoneNumberDTO.
     * @param phoneNumberDTO the contactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneNumberDTO,
     * or with status {@code 400 (Bad Request)} if the phoneNumberDTO is not valid or contactId or numberId is not exist in db ,
     * or with status {@code 500 (Internal Server Error)} if the phoneNumberDTO couldn't be updated.
     */
    @PutMapping("/{contactId}/phoneNumber/{numberId}")
    public ResponseEntity<PhoneNumberDTO> updatePhoneNumber(
            @PathVariable(value = "contactId") Long contactId,
            @PathVariable(value = "numberId") Long numberId,
            @RequestBody PhoneNumberDTO phoneNumberDTO
    ) {
        PhoneNumberDTO result = facadeContactServiceV1.updatePhoneNumber(contactId, numberId, phoneNumberDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET  /findById/:contactId} : get the "contactId" contact.
     *
     * @param contactId the id of the contactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/findById/{contactId}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long contactId) {
        ContactDTO contactDTO = facadeContactServiceV1.getContact(contactId);
        return new ResponseEntity<>(contactDTO, HttpStatus.OK);
    }


    /**
     * {@code GET  /findByName/:name} : get the "name" contact.
     *
     * @param name the id of the contactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/findByName/{name}/{lastName}")
    public ResponseEntity<List<ContactDTO>> getContactByName(@PathVariable String name, @PathVariable String lastName) {
        return new ResponseEntity<>(facadeContactServiceV1.getContact(name, lastName), HttpStatus.OK);
    }


    /**
     * {@code DELETE  /:contactId} : delete the contact.
     *
     * @param contactId the id of the contactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        facadeContactServiceV1.deleteContact(contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * {@code DELETE  /:contactId/email/:emailId} : delete the email by contactId and emailId
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{contactId}/email/{emailId}")
    public ResponseEntity<Void> deleteContactEmail(@PathVariable Long contactId, @PathVariable Long emailId) {
        facadeContactServiceV1.deleteEmail(emailId, contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * {@code DELETE  /:contactId/phoneNumber/:numberId} : delete the number by contactId and numberId
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{contactId}/phoneNumber/{numberId}")
    public ResponseEntity<Void> deleteContactNumber(@PathVariable Long contactId, @PathVariable Long numberId) {
        facadeContactServiceV1.deletePhoneNumber(numberId, contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
