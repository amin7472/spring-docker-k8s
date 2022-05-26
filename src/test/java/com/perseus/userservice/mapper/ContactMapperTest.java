package com.perseus.userservice.mapper;

import org.junit.jupiter.api.BeforeEach;

class ContactMapperTest {

    private ContactMapper contactMapper;

    @BeforeEach
    public void setUp() {
        contactMapper = new ContactMapperImpl();
    }
}
