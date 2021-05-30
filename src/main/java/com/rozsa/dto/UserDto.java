package com.rozsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private Boolean active;
}
