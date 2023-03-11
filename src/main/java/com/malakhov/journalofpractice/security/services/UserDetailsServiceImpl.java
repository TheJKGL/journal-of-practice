package com.malakhov.journalofpractice.security.services;

import com.malakhov.journalofpractice.models.Credential;
import com.malakhov.journalofpractice.repositories.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    CredentialsRepository credentialsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        //return UserDetailsImpl.build(credentials.getUser());
        return User
                .withUsername(email)
                .password(credentials.getPassword())
                .authorities(credentials.getUser().getRole().name())
                .build();
    }
}
