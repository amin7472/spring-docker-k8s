package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByFirstName(String name);
}
