package com.example.charityManagementSystem.models;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAdminPrincipal implements UserDetails {

    private Admin userAdmin;

    public UserAdminPrincipal(Admin userAdmin) {
        this.userAdmin = userAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getPassword() {
        return userAdmin.getPassword(); 
    }

    @Override
    public String getUsername() {
        return userAdmin.getUsername(); 
    }
}
