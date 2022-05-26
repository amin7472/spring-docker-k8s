package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the contract entity.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByFirstName(String name);
}
