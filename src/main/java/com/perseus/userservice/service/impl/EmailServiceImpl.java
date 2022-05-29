package com.perseus.userservice.service.impl;

import com.perseus.userservice.domain.Email;
import com.perseus.userservice.repository.EmailRepository;
import com.perseus.userservice.service.EmailService;
import com.perseus.userservice.service.dto.EmailDTO;
import com.perseus.userservice.mapper.EmailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final EmailRepository emailRepository;

    private final EmailMapper emailMapper;

    public EmailServiceImpl(EmailRepository emailRepository, EmailMapper emailMapper) {
        this.emailRepository = emailRepository;
        this.emailMapper = emailMapper;
    }

    @Override
    public EmailDTO save(EmailDTO emailDTO) {
        log.debug("Request to save Email : {}", emailDTO);
        Email email = emailMapper.toEntity(emailDTO);
        email = emailRepository.save(email);
        return emailMapper.toDto(email);
    }

    @Override
    public void saveAll(List<EmailDTO> emailDTOs) {
        log.debug("Request to save list of  Email by size  : {}", emailDTOs.size());
        List<Email> emails = emailDTOs.stream().map(emailMapper::toEntity).collect(Collectors.toList());
        emailRepository.saveAll(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailDTO> findOne(Long id) {
        log.debug("Request to get Email : {}", id);
        return emailRepository.findById(id).map(emailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Email : {}", id);
        emailRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailDTO> findAll() {
        log.debug("Request to get all Emails");
        return emailRepository.findAll().stream().map(emailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
