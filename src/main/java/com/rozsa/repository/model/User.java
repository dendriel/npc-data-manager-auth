package com.rozsa.repository.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column(unique = true)
    @NotBlank
    private String login;

    @Column
    @NotBlank
    private String password;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Column(columnDefinition = "boolean default true")
    @NotNull
    private Boolean active;

    @Column(columnDefinition = "boolean default false")
    @NotNull
    private Boolean service;

    public boolean isActive() {
        return active;
    }

    public boolean isService() {
        return service;
    }

    public boolean isPlainUser() {
        return !service;
    }

    public boolean isInactive() {
        return !isActive();
    }
}
