package com.perseus.userservice.repository;

import com.perseus.userservice.domain.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PhoneNumber entity.
 */
@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {}
