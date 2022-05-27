package com.perseus.userservice.service.impl;

import com.perseus.userservice.domain.Contact;
import com.perseus.userservice.repository.ContactRepository;
import com.perseus.userservice.service.ContactService;
import com.perseus.userservice.service.dto.ContactDTO;
import com.perseus.userservice.mapper.ContactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    public ContactDTO save(ContactDTO contactDTO) {
        log.debug("Request to save user : {}", contactDTO);
        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);
        return contactMapper.toDto(contact);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all userS");
        return contactRepository.findAll(pageable).map(contactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDTO> findOne(Long id) {
        log.debug("Request to get user : {}", id);
        return contactRepository.findById(id).map(contactMapper::toDto);
    }

    @Override
    public List<ContactDTO> findByName(String name,String lastName) {

        Contact contact = new Contact();
        contact.setFirstName(name);
        contact.setLastName(lastName);

        Example<Contact> example = Example.of(contact);

        return contactRepository.findAll(example).stream().map(contactMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete user : {}", id);
        contactRepository.deleteById(id);
    }
}
