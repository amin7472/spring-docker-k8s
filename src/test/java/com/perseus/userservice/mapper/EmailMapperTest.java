package com.perseus.userservice.mapper;

import org.junit.jupiter.api.BeforeEach;

class EmailMapperTest {

    private EmailMapper emailMapper;

    @BeforeEach
    public void setUp() {
        emailMapper = new EmailMapperImpl();
    }
}
