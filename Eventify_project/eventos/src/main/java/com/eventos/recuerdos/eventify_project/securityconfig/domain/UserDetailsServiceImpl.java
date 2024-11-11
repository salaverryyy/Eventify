package com.eventos.recuerdos.eventify_project.securityconfig.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserAccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

    public UserDetailsService userDetailsService() {
        return this;
    }
}
