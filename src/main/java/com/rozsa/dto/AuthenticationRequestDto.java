package com.rozsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
