package com.perseus.userservice.service;


import com.perseus.userservice.service.dto.EmailDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.perseus.userservice.domain.Email}.
 */
public interface EmailService {
    /**
     * Save a email.
     *
     * @param emailDTO the entity to save.
     * @return the persisted entity.
     */
    EmailDTO save(EmailDTO emailDTO);


    /**
     * Save a email.
     *
     * @param emailDTOs the list of entity to save.
     * @return the persisted entity.
     */
    void saveAll(List<EmailDTO> emailDTOs);

    /**
     * Get the "id" email.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailDTO> findOne(Long id);


    /**
     * Get the "id" email.
     *
     * @param emailId the id of the entity.
     * @param contactId the id of the entity.
     * @return the entity.
     */
    Optional<EmailDTO> findByIdAndContactId(Long emailId, Long contactId);

    /**
     * Delete the "id" email.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    /**
     * Get all the emails.
     *
     * @return the list of entities.
     */
    List<EmailDTO> findAll();
}
