package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmailRepositoryUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    EmailRepository repository;


    @Test
    public void should_find_no_email_if_repository_is_empty() {
        Iterable emails = repository.findAll();
        assertThat(emails).isEmpty();
    }

    @Test
    public void should_store_a_email() {
        Email email = new Email();
        email.setMail("Amin@yahoo.com");
        email = repository.save(email);
        assertThat(email).hasFieldOrPropertyWithValue("mail", "Amin@yahoo.com");
    }

    @Test
    public void should_find_all_email() {
        Email email1 = new Email();
        email1.setMail("Amin1@yahoo.com");
        entityManager.persist(email1);

        Email email2 = new Email();
        email2.setMail("Amin2@yahoo.com");
        entityManager.persist(email2);

        Email email3 = new Email();
        email3.setMail("Amin3@yahoo.com");
        entityManager.persist(email3);

        Iterable emails = repository.findAll();

        assertThat(emails).hasSize(3).contains(email1, email2, email3);
    }

    @Test
    public void should_find_email_by_id() {

        Email email1 = new Email();
        email1.setMail("Amin1@yahoo.com");
        entityManager.persist(email1);

        Email email2 = new Email();
        email2.setMail("Amin2@yahoo.com");
        entityManager.persist(email2);

        Email foundEmail = repository.findById(email2.getId()).get();
        assertThat(foundEmail).isEqualTo(email2);
    }


    @Test
    public void should_update_email_by_id() {
        Email email1 = new Email();
        email1.setMail("Amin1@yahoo.com");
        entityManager.persist(email1);

        Email email2 = new Email();
        email2.setMail("Amin2@yahoo.com");
        entityManager.persist(email2);


        Email updatedEm = new Email();
        updatedEm.setMail("updated Amin2@yahoo.com");

        Email email = repository.findById(email2.getId()).get();
        email.setMail(updatedEm.getMail());
        repository.save(email);
        Email checkEm = repository.findById(email2.getId()).get();

        assertThat(checkEm.getId()).isEqualTo(email2.getId());
        assertThat(checkEm.getMail()).isEqualTo(updatedEm.getMail());
    }

    @Test
    public void should_delete_email_by_id() {
        Email email1 = new Email();
        email1.setMail("Amin1@yahoo.com");
        entityManager.persist(email1);

        Email email2 = new Email();
        email2.setMail("Amin2@yahoo.com");
        entityManager.persist(email2);

        Email email3 = new Email();
        email3.setMail("Amin3@yahoo.com");
        entityManager.persist(email3);


        repository.deleteById(email2.getId());
        Iterable emails = repository.findAll();
        assertThat(emails).hasSize(2).contains(email1, email3);
    }

    @Test
    public void should_delete_all_email() {
        Email email1 = new Email();
        email1.setMail("Amin1@yahoo.com");
        entityManager.persist(email1);

        Email email2 = new Email();
        email2.setMail("Amin2@yahoo.com");
        entityManager.persist(email2);

        Email email3 = new Email();
        email3.setMail("Amin3@yahoo.com");
        entityManager.persist(email3);
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
}
