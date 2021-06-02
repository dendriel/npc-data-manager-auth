package com.rozsa.controller;

import com.rozsa.business.api.AuthenticationBusiness;
import com.rozsa.dto.AuthenticationRequestDto;
import com.rozsa.dto.AuthenticationResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@AllArgsConstructor
@RestController
public class AuthenticationController {
    private final AuthenticationBusiness authenticationBusiness;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        String jwt = authenticationBusiness.authenticate(request.getUsername(), request.getPassword());
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
    }


    @GetMapping("/validate")
    public ResponseEntity<?> isTokenValid(HttpServletResponse res) {

        UserDetails details = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        res.setHeader("x-ndm-username", details.getUsername());

        details.getAuthorities().forEach(a -> {
            res.setHeader("x-ndm-authorities", a.getAuthority());
        });

        return ResponseEntity.status(HttpStatus.OK).body("{ \"autheticated\": true }");
    }
}
