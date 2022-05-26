package com.perseus.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserI.
 */
@Entity
@Table(name = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @OneToMany(mappedBy = "contactId", cascade = CascadeType.REMOVE)
    private Set<Email> emails = new HashSet<>();

    @OneToMany(mappedBy = "contactId", cascade = CascadeType.REMOVE)
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();


    public Long getId() {
        return this.id;
    }

    public Contact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Contact firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Contact lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Email> getEmails() {
        return this.emails;
    }

    public void setEmails(Set<Email> emails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setContactId(null));
        }
        if (emails != null) {
            emails.forEach(i -> i.setContactId(this.id));
        }
        this.emails = emails;
    }

    public Contact emails(Set<Email> emails) {
        this.setEmails(emails);
        return this;
    }

    public Contact addEmail(Email email) {
        this.emails.add(email);
        email.setContactId(this.id);
        return this;
    }

    public Contact removeEmail(Email email) {
        this.emails.remove(email);
        email.setContactId(null);
        return this;
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        if (this.phoneNumbers != null) {
            this.phoneNumbers.forEach(i -> i.setContactId(null));
        }
        if (phoneNumbers != null) {
            phoneNumbers.forEach(i -> i.setContactId(this.id));
        }
        this.phoneNumbers = phoneNumbers;
    }

    public Contact phoneNumbers(Set<PhoneNumber> phoneNumbers) {
        this.setPhoneNumbers(phoneNumbers);
        return this;
    }

    public Contact addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
        phoneNumber.setContactId(this.id);
        return this;
    }

    public Contact removePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.remove(phoneNumber);
        phoneNumber.setContactId(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserI{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + "'" +
                ", lastName='" + getLastName() + "'" +
                "}";
    }
}
