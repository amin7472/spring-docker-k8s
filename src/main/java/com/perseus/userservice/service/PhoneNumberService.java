package com.perseus.userservice.service;


import com.perseus.userservice.service.dto.PhoneNumberDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.perseus.userservice.domain.PhoneNumber}.
 */
public interface PhoneNumberService {
    /**
     * Save a phoneNumber.
     *
     * @param phoneNumberDTO the entity to save.
     * @return the persisted entity.
     */
    PhoneNumberDTO save(PhoneNumberDTO phoneNumberDTO);

    /**
     * Save all a phoneNumber.
     *
     * @param phoneNumberDTO the entity to save.
     * @return the persisted entity.
     */
    void saveAll(List<PhoneNumberDTO> phoneNumberDTO);

    /**
     * Get the "id" phoneNumber.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PhoneNumberDTO> findOne(Long id);

    /**
     * Delete the "id" phoneNumber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
