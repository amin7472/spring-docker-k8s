package com.perseus.userservice.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.perseus.userservice.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }
}
