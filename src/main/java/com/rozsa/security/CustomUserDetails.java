package com.rozsa.security;

import com.rozsa.repository.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final User user;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getLogin(), "", authorities);
        this.user = user;
    }
}
