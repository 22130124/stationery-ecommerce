package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.model.AuthProvider;
import com.tqk.stationeryecommercebackend.repository.AuthProviderRepository;
import com.tqk.stationeryecommercebackend.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthProviderRepository authProviderRepository;

    public CustomUserDetailsService(AuthProviderRepository authProviderRepository) {
        this.authProviderRepository = authProviderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthProvider provider = authProviderRepository.findByAccount_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(provider.getAccount(), provider);
    }
}

