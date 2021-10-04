package com.rozsa.security.filters;

import com.rozsa.business.api.UserBusiness;
import com.rozsa.repository.model.User;
import com.rozsa.security.CustomUserDetails;
import com.rozsa.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserBusiness userBusiness;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        final String token = getToken(req);
        String username = null;
        String jwt = null;
        if (token != null) {
            jwt = token;
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            username = jwtUtil.extractUsername(jwt);
        }

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        User user = userBusiness.findByLogin(username);

        if (!jwtUtil.validateToken(jwt, user)) {
            log.debug("Invalid authentication token: {}", jwt);
            return;
        }

        UserDetails userDetails = new CustomUserDetails(user, new ArrayList<>());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(req, res);
    }


    public String getToken(HttpServletRequest req) {
        if (!jwtUtil.isCookieTokenEnabled()) {
            return req.getHeader("Authorization");
        }

        final String cookieName = jwtUtil.getCookieTokenName();

        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }

        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .orElse(null);

        if (cookie == null) {
            return null;
        }

        return cookie.getValue();
    }
}
