package com.eventos.recuerdos.eventify_project.securityconfig;

import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurar el mapeo personalizado para UserAccount -> UserDTO
        modelMapper.typeMap(UserAccount.class, UserDTO.class).addMappings(mapper -> {
            mapper.map(UserAccount::getUsernameField, UserDTO::setUsername);
        });

        return modelMapper;
    }
}



