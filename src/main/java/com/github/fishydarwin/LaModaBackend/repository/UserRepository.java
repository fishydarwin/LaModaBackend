package com.github.fishydarwin.LaModaBackend.repository;

import com.github.fishydarwin.LaModaBackend.domain.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> all();
    boolean any(long id);
    User byId(long id);
    long idByDetails(String email, String passwordObfuscated);

    String generateSession(User user);
    User bySession(String sessionId);

    long add(User user);
    boolean anyByEmail(String email);

}
