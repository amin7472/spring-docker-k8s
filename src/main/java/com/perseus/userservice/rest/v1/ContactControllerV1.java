package com.perseus.userservice.rest.v1;


import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/contacts")
@Tag(name = "Contact Resource")
public class ContactControllerV1 {

    private final FacadeContactServiceV1 facadeContactServiceV1;

    public ContactControllerV1(FacadeContactServiceV1 facadeContactServiceV1) {
        this.facadeContactServiceV1 = facadeContactServiceV1;
    }

    @PostMapping()
    @Operation(summary = "This api use to add the new contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = " Contact saved in database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "If the contact or its child has already an ID",
                    content = @Content)
    })
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO result = facadeContactServiceV1.createNewContact(contactDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @PostMapping("/{contactId}/email")
    @Operation(summary = "This api use to add the new email to existing contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "New email add to contact",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "The email has already an ID",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The contact not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "The email couldn't be updated",
                    content = @Content)
    })
    public ResponseEntity<EmailDTO> addNewEmail(
            @PathVariable(value = "contactId") final Long contactId,
            @RequestBody EmailDTO emailDTO
    ) {
        EmailDTO result = facadeContactServiceV1.addNewEmail(emailDTO, contactId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/{contactId}/email/{emailId}")
    @Operation(summary = "This api use to update the existing email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "email has updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact or email not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "The email couldn't be updated",
                    content = @Content)
    })
    public ResponseEntity<EmailDTO> updateEmail(
            @PathVariable(value = "contactId") Long contactId,
            @PathVariable(value = "emailId") Long emailId,
            @RequestBody EmailDTO emailDTO
    ) {
        EmailDTO result = facadeContactServiceV1.updateEmail(contactId, emailId, emailDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/{contactId}/phoneNumber")
    @Operation(summary = "This api use to add the new number to existing contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "New number add to contact",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "The number has already an ID",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The contact not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "The number couldn't be updated",
                    content = @Content)
    })
    public ResponseEntity<PhoneNumberDTO> addNewPhoneNumber(
            @PathVariable(value = "contactId") Long contactId,
            @RequestBody PhoneNumberDTO phoneNumberDTO
    ) {
        PhoneNumberDTO result = facadeContactServiceV1.addNewPhoneNumber(phoneNumberDTO, contactId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{contactId}/phoneNumber/{numberId}")
    @Operation(summary = "This api use to update the existing number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "number has updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact or number not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "The email couldn't be updated",
                    content = @Content)
    })
    public ResponseEntity<PhoneNumberDTO> updatePhoneNumber(
            @PathVariable(value = "contactId") Long contactId,
            @PathVariable(value = "numberId") Long numberId,
            @RequestBody PhoneNumberDTO phoneNumberDTO
    ) {
        PhoneNumberDTO result = facadeContactServiceV1.updatePhoneNumber(contactId, numberId, phoneNumberDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{contactId}")
    @Operation(summary = "This api use to find contact by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "exist contact with id",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact not found",
                    content = @Content)
    })
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long contactId) {
        ContactDTO contactDTO = facadeContactServiceV1.getContact(contactId);
        return new ResponseEntity<>(contactDTO, HttpStatus.OK);
    }


    @GetMapping("/findByName/{name}/{lastName}")
    @Operation(summary = "This api use to find contact by name and family")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "exist contacts",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<List<ContactDTO>> getContactByName(@PathVariable String name, @PathVariable String lastName) {
        return new ResponseEntity<>(facadeContactServiceV1.getContact(name, lastName), HttpStatus.OK);
    }


    @DeleteMapping("/{contactId}")
    @Operation(summary = "This api use to delete contact by all of its child")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "contact deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        facadeContactServiceV1.deleteContact(contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{contactId}/email/{emailId}")
    @Operation(summary = "This api use to delete the custom email of contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "email deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact or email not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteContactEmail(@PathVariable Long contactId, @PathVariable Long emailId) {
        facadeContactServiceV1.deleteEmail(emailId, contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{contactId}/phoneNumber/{numberId}")
    @Operation(summary = "This api use to delete the custom number of contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "number deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The contact or number not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteContactNumber(@PathVariable Long contactId, @PathVariable Long numberId) {
        facadeContactServiceV1.deletePhoneNumber(numberId, contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
