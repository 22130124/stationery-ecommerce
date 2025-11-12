package com.tqk.stationeryecommercebackend.security;

import com.tqk.stationeryecommercebackend.model.Account;
import com.tqk.stationeryecommercebackend.model.AuthProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Account account;
    private final AuthProvider authProvider;

    public CustomUserDetails(Account account, AuthProvider authProvider) {
        this.account = account;
        this.authProvider = authProvider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRole() != null
                ? List.of(new SimpleGrantedAuthority(account.getRole()))
                : List.of();
    }

    @Override
    public String getPassword() {
        return authProvider.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getIsActive();
    }

    public Account getAccount() {
        return account;
    }

    public Integer getId() {
        return account.getId();
    }
}
