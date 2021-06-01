package com.rozsa.security.filters;

import com.rozsa.exception.InternalServerErrorException;
import com.rozsa.exception.NpcDataManagerAuthException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(req, res);

        } catch(Exception e) {
            if (e instanceof NpcDataManagerAuthException) {
                throw e;
            }
            // Obviously, we won't want to forward everything in production.
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
