package com.rozsa.security;

import com.rozsa.business.api.UserBusiness;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserBusiness userBusiness;

    @Override
    public UserDetails loadUserByUsername(String login) {
        com.rozsa.repository.model.User sysUser = userBusiness.findByLogin(login);

        if (sysUser == null) {
            log.info("User {} not found!", login);
            return null;
        }

        // TODO: add authority.
        return new User(sysUser.getLogin(), sysUser.getPassword(), new ArrayList<>());
    }
}
