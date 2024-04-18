package com.github.fishydarwin.LaModaBackend.domain.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HUserWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String passwordObfuscated;
    private String email;
    private UserRole role;

    public HUserWrapper() {}

    public HUserWrapper(String name, String passwordObfuscated, String email, UserRole role) {
        this.name = name;
        this.passwordObfuscated = passwordObfuscated;
        this.email = email;
        this.role = role;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordObfuscated() {
        return passwordObfuscated;
    }

    public void setPasswordObfuscated(String passwordObfuscated) {
        this.passwordObfuscated = passwordObfuscated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
