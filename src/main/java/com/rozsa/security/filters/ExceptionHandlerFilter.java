package com.rozsa.security.filters;

import com.rozsa.exception.InternalServerErrorException;
import com.rozsa.exception.NpcDataManagerAuthException;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


@Component
public class ExceptionHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
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
