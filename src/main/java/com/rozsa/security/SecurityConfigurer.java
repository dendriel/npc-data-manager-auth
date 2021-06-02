package com.rozsa.security;

import com.rozsa.security.filters.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final DbUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PreFlightFilter preFlightFilter;
    private final HealthcheckFilter healthcheckFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(preFlightFilter, ExceptionHandlerFilter.class);
        http.addFilterBefore(jwtRequestFilter, PreFlightFilter.class);
        http.addFilterBefore(healthcheckFilter, JwtRequestFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt includes the salt inside the resulting string https://en.wikipedia.org/wiki/Bcrypt
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
