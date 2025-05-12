package com.week5.SecurityApp.SecurityApplication.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The type Model mapper config.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Get model mapper.
     *
     * @return the model mapper
     */
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }


    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
