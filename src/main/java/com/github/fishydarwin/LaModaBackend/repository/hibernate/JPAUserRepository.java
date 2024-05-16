package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HUserWrapper;
import com.github.fishydarwin.LaModaBackend.manager.UserSessionManager;
import com.github.fishydarwin.LaModaBackend.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JPAUserRepository implements UserRepository {

    private final HUserRepository userRepository;

    public JPAUserRepository(HUserRepository autowiredRepository) {
        this.userRepository = autowiredRepository;
    }

    @Override
    public Collection<User> all() {
        return StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .map(user -> new User(user.getId(), user.getName(), user.getPasswordObfuscated(),
                                      user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean any(long id) {
        return userRepository.existsById((int) id);
    }

    @Override
    public User byId(long id) {
        Optional<HUserWrapper> userMaybe = userRepository.findById((int) id);
        if (userMaybe.isEmpty()) return null;
        HUserWrapper user = userMaybe.get();
        return new User(user.getId(), user.getName(), user.getPasswordObfuscated(),
                        user.getEmail(), user.getRole());
    }

    @Override
    public long idByDetails(String email, String passwordObfuscated) {
        HUserWrapper user = userRepository.findByEmailAndPasswordObfuscated(email, passwordObfuscated);
        if (user == null) return -1;
        return user.getId();
    }

    @Override
    public String generateSession(User user) {
        return UserSessionManager.generateSession(user);
    }

    @Override
    public User bySession(String sessionId) {
        return UserSessionManager.bySession(sessionId);
    }

    @Override
    public long add(User user) {
        HUserWrapper userWrapper = new HUserWrapper(user.name(), user.passwordObfuscated(), user.email(), user.role());
        return userRepository.save(userWrapper).getId();
    }

    @Override
    public boolean anyByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
