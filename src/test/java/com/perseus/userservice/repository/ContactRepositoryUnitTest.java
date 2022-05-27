package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContactRepositoryUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    ContactRepository repository;


    @Test
    public void should_find_no_contact_if_repository_is_empty() {
        Iterable contacts = repository.findAll();
        assertThat(contacts).isEmpty();
    }

    @Test
    public void should_store_a_contact() {
        Contact contact = new Contact();
        contact.setFirstName("Amin");
        contact.setLastName("Ahmadi");
        contact = repository.save(contact);
        assertThat(contact).hasFieldOrPropertyWithValue("firstName", "Amin");
        assertThat(contact).hasFieldOrPropertyWithValue("lastName", "Ahmadi");
    }

    @Test
    public void should_find_all_contact() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Amin1");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        entityManager.persist(contact2);

        Contact contact3 = new Contact();
        contact3.setFirstName("Amin3");
        contact3.setLastName("Ahmadi3");
        entityManager.persist(contact3);
        Iterable contacts = repository.findAll();

        assertThat(contacts).hasSize(3).contains(contact1, contact2, contact3);
    }

    @Test
    public void should_find_contact_by_id() {

        Contact contact1 = new Contact();
        contact1.setFirstName("Amin1");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);


        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        contact2 = entityManager.persist(contact2);

        Contact foundContact = repository.findById(contact2.getId()).get();
        assertThat(foundContact).isEqualTo(contact2);
    }

    @Test
    public void should_find_by_name_contacts() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Amin");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        entityManager.persist(contact2);

        Contact contact3 = new Contact();
        contact3.setFirstName("Amin");
        contact3.setLastName("Ahmadi3");
        entityManager.persist(contact3);

        Iterable contact = repository.findByFirstName(contact3.getFirstName());
        assertThat(contact).hasSize(2).contains(contact1, contact3);
    }


    @Test
    public void should_update_contact_by_id() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Amin1");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        entityManager.persist(contact2);


        Contact updatedTut = new Contact();
        updatedTut.setFirstName("updated Amin2");
        updatedTut.setLastName("updated Ahmadi2");

        Contact con = repository.findById(contact2.getId()).get();
        con.setFirstName(updatedTut.getFirstName());
        con.setLastName(updatedTut.getLastName());
        repository.save(con);
        Contact checkCon = repository.findById(contact2.getId()).get();

        assertThat(checkCon.getId()).isEqualTo(contact2.getId());
        assertThat(checkCon.getFirstName()).isEqualTo(updatedTut.getFirstName());
        assertThat(checkCon.getLastName()).isEqualTo(updatedTut.getLastName());
    }

    @Test
    public void should_delete_contact_by_id() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Amin1");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        entityManager.persist(contact2);

        Contact contact3 = new Contact();
        contact3.setFirstName("Amin3");
        contact3.setLastName("Ahmadi3");
        entityManager.persist(contact3);


        entityManager.persist(contact3);
        repository.deleteById(contact2.getId());
        Iterable tutorials = repository.findAll();
        assertThat(tutorials).hasSize(2).contains(contact1, contact3);
    }

    @Test
    public void should_delete_all_contact() {
        Contact contact1 = new Contact();
        contact1.setFirstName("Amin1");
        contact1.setLastName("Ahmadi1");
        entityManager.persist(contact1);

        Contact contact2 = new Contact();
        contact2.setFirstName("Amin2");
        contact2.setLastName("Ahmadi2");
        entityManager.persist(contact2);
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
}
