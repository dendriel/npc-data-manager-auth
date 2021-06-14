package com.rozsa.dto;

import lombok.Data;

import java.util.List;

@Data
public class ValidateResponseDto {
    private boolean authenticated;
    private String username;
    private List<String> authorities;
}
