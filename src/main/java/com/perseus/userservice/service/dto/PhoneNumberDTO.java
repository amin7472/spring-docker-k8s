package com.perseus.userservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.perseus.userservice.domain.PhoneNumber} entity.
 */
public class PhoneNumberDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String number;

    public PhoneNumberDTO(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public PhoneNumberDTO() {
    }

    @JsonIgnore
    private Long contactId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneNumberDTO)) {
            return false;
        }

        PhoneNumberDTO phoneNumberDTO = (PhoneNumberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phoneNumberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "PhoneNumberDTO{" +
                "id=" + getId() +
                ", number='" + getNumber() + "'" +
                ", contact=" + getContactId() +
                "}";
    }
}
