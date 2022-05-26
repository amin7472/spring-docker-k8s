package com.perseus.userservice.repository;

import com.perseus.userservice.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Email entity.
 */
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {}
