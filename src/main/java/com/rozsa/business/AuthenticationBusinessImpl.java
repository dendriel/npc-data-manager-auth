package com.rozsa.business;

import com.rozsa.business.api.AuthenticationBusiness;
import com.rozsa.business.api.UserBusiness;
import com.rozsa.repository.model.User;
import com.rozsa.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@AllArgsConstructor
@Component
public class AuthenticationBusinessImpl implements AuthenticationBusiness {
    private final AuthenticationManager authenticationManager;
    private final UserBusiness userBusiness;
    private final JwtUtil jwtTokenUtil;

    @Override
    public String authenticate(String username, String password) {
        User user = userBusiness.findByLogin(username);
        if (user == null || user.isInactive()) {
            return null;
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            return null;
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), new ArrayList<>());
        return jwtTokenUtil.generateToken(userDetails);
    }
}
