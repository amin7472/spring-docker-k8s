package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByIdAndContactId(Long id, Long contactId);
}
