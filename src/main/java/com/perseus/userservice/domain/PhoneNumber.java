package com.perseus.userservice.domain;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "phone_number")
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "contact_id")
    private Long contactId;

    public Long getId() {
        return this.id;
    }

    public PhoneNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public PhoneNumber number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
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
        if (!(o instanceof PhoneNumber)) {
            return false;
        }
        return id != null && id.equals(((PhoneNumber) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
