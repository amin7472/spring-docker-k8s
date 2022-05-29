package com.perseus.userservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.perseus.userservice.domain.Email} entity.
 */
public class EmailDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String mail;

    @JsonIgnore
    private Long contactId;


    public EmailDTO(Long id, String mail) {
        this.mail = mail;
        this.id = id;
    }

    public EmailDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
        if (!(o instanceof EmailDTO)) {
            return false;
        }

        EmailDTO emailDTO = (EmailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailDTO{" +
                "id=" + getId() +
                ", mail='" + getMail() + "'" +
                ", contact=" + getContactId() +
                "}";
    }
}
