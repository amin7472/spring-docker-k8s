package com.perseus.userservice.service.impl;


import com.perseus.userservice.domain.PhoneNumber;
import com.perseus.userservice.repository.PhoneNumberRepository;
import com.perseus.userservice.service.PhoneNumberService;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import com.perseus.userservice.mapper.PhoneNumberMapper;
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
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final Logger log = LoggerFactory.getLogger(PhoneNumberServiceImpl.class);

    private final PhoneNumberRepository phoneNumberRepository;

    private final PhoneNumberMapper phoneNumberMapper;

    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository, PhoneNumberMapper phoneNumberMapper) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.phoneNumberMapper = phoneNumberMapper;
    }

    @Override
    public PhoneNumberDTO save(PhoneNumberDTO phoneNumberDTO) {
        log.debug("Request to save PhoneNumber : {}", phoneNumberDTO);
        PhoneNumber phoneNumber = phoneNumberMapper.toEntity(phoneNumberDTO);
        phoneNumber = phoneNumberRepository.save(phoneNumber);
        return phoneNumberMapper.toDto(phoneNumber);
    }

    @Override
    public void saveAll(List<PhoneNumberDTO> phoneNumberDTO) {
        log.debug("Request to save PhoneNumber by size : {}", phoneNumberDTO.size());
        List<PhoneNumber> phoneNumber = phoneNumberDTO
                .stream()
                .map(phoneNumberMapper::toEntity)
                .collect(Collectors.toList());
        phoneNumberRepository.saveAll(phoneNumber);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PhoneNumberDTO> findOne(Long id) {
        log.debug("Request to get PhoneNumber : {}", id);
        return phoneNumberRepository.findById(id).map(phoneNumberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PhoneNumber : {}", id);
        phoneNumberRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneNumberDTO> findAll() {
        log.debug("Request to get all PhoneNumbers");
        return phoneNumberRepository.findAll().stream().map(phoneNumberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
