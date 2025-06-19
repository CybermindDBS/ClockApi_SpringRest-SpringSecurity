package com.example.springrest_refresher.restclient.clockwebapp.security;

import com.example.springrest_refresher.restclient.clockwebapp.model.entity.MyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails, Serializable {
    private MyUser user;

    public MyUserDetails(MyUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


    // Must override these methods for session management to work in security config.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyUserDetails)) return false;
        return user.getUsername().equals(((MyUserDetails) o).user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUsername());
    }
}
