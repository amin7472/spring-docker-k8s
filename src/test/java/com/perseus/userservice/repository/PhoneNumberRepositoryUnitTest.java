package com.perseus.userservice.repository;

import com.perseus.userservice.domain.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PhoneNumberRepositoryUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    PhoneNumberRepository repository;

    @Test
    public void should_find_no_phone_if_repository_is_empty() {
        Iterable phoneNumbers = repository.findAll();
        assertThat(phoneNumbers).isEmpty();
    }

    @Test
    public void should_store_a_phone() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber("123");
        phoneNumber = repository.save(phoneNumber);
        assertThat(phoneNumber).hasFieldOrPropertyWithValue("number", "123");
    }

    @Test
    public void should_find_all_phone() {
        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setNumber("1231");
        entityManager.persist(phoneNumber1);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("1232");
        entityManager.persist(phoneNumber2);

        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setNumber("1233");
        entityManager.persist(phoneNumber3);

        Iterable phoneNumbers = repository.findAll();

        assertThat(phoneNumbers).hasSize(3).contains(phoneNumber1, phoneNumber2, phoneNumber3);
    }

    @Test
    public void should_find_phone_by_id() {

        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setNumber("1231");
        entityManager.persist(phoneNumber1);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("1232");
        entityManager.persist(phoneNumber2);

        PhoneNumber foundPhoneNumber = repository.findById(phoneNumber2.getId()).get();
        assertThat(foundPhoneNumber).isEqualTo(phoneNumber2);
    }


    @Test
    public void should_update_phone_by_id() {
        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setNumber("1231");
        entityManager.persist(phoneNumber1);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("1232");
        entityManager.persist(phoneNumber2);


        PhoneNumber updatedEm = new PhoneNumber();
        updatedEm.setNumber("updated 1232");

        PhoneNumber phone = repository.findById(phoneNumber2.getId()).get();
        phone.setNumber(updatedEm.getNumber());
        repository.save(phone);
        PhoneNumber checkEm = repository.findById(phoneNumber2.getId()).get();

        assertThat(checkEm.getId()).isEqualTo(phoneNumber2.getId());
        assertThat(checkEm.getNumber()).isEqualTo(updatedEm.getNumber());
    }

    @Test
    public void should_delete_phone_by_id() {
        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setNumber("1231");
        entityManager.persist(phoneNumber1);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("1232");
        entityManager.persist(phoneNumber2);

        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setNumber("1233");
        entityManager.persist(phoneNumber3);


        repository.deleteById(phoneNumber2.getId());
        Iterable phones = repository.findAll();
        assertThat(phones).hasSize(2).contains(phoneNumber1, phoneNumber3);
    }

    @Test
    public void should_delete_all_phone() {
        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setNumber("1231");
        entityManager.persist(phoneNumber1);

        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setNumber("1232");
        entityManager.persist(phoneNumber2);

        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setNumber("1233");
        entityManager.persist(phoneNumber3);
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
}
