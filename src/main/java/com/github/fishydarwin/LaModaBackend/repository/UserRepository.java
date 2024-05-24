package com.github.fishydarwin.LaModaBackend.repository;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.UserRole;

import java.util.Collection;

public interface UserRepository {

    Collection<User> all();
    boolean any(long id);
    User byId(long id);
    User byEmail(String email);
    long idByDetails(String email, String passwordObfuscated);

    String generateSession(User user);
    User bySession(String sessionId);

    long add(User user);
    boolean anyByEmail(String email);

    boolean updateUserRole(long id, UserRole role);

}
