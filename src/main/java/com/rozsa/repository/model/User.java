package com.rozsa.repository.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    public boolean isActive() {
        return active != null ? active : false;
    }

    public boolean isInactive() {
        return !isActive();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='####" +
                ", email='" + email + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
