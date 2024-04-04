package com.github.fishydarwin.LaModaBackend.domain;

public record User(long id, String name, String passwordObfuscated, String email, UserRole role) {
}
