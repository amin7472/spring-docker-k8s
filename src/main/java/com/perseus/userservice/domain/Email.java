package com.perseus.userservice.domain;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "email")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "contact_id")
    private Long contactId;

    public Long getId() {
        return this.id;
    }

    public Email id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return this.mail;
    }

    public Email mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long userId) {
        this.contactId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        return id != null && id.equals(((Email) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Email{" +
            "id=" + getId() +
            ", mail='" + getMail() + "'" +
            "}";
    }
}
