package com.github.fishydarwin.LaModaBackend.repository.memory;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.UserRole;
import com.github.fishydarwin.LaModaBackend.repository.UserRepository;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private static long LAST_ID = 0;
    private final Map<Long, User> contents = new HashMap<>();

    private final Map<String, User> sessionToUser = new HashMap<>();
    private final Map<Long, String> userIdToSession = new HashMap<>();

    private final Set<String> usedEmails = new HashSet<>();

    public InMemoryUserRepository() {
        add(new User(
                1, "Administrator", "abcdefghijk", "admin@lamoda.ro", UserRole.ADMIN
        ));
        add(new User(
                2, "Ioana Dan", "abcdefghijk", "ioana.dan@abc.ro", UserRole.USER
        ));
        add(new User(
                3, "Alina Buzău", "abcdefghijk", "alii.buzbuz@abc.ro", UserRole.USER
        ));
        add(new User(
                4, "Selena Komeiji", "abcdefghijk", "selekk12@abc.ro", UserRole.MODERATOR
        ));
    }

    @Override
    public Collection<User> all() {
        return contents.values();
    }

    @Override
    public boolean any(long id) {
        return contents.containsKey(id);
    }

    @Override
    public User byId(long id) {
        System.out.println("\"Unimplemented operation for in-memory repository!\"");
        return null;
    }

    @Override
    public User byEmail(String email) {
        return null;
    }

    @Override
    public long idByDetails(String email, String passwordObfuscated) {
        for (Map.Entry<Long, User> entry : contents.entrySet())
            if (entry.getValue().email().equals(email) &&
                    entry.getValue().passwordObfuscated().equals(passwordObfuscated))
                return entry.getKey();
        return -1;
    }

    @Override
    public String generateSession(User user) {
        String uuid = UUID.randomUUID().toString();
        if (userIdToSession.containsKey(user.id()))
            sessionToUser.remove(userIdToSession.get(user.id()));
        sessionToUser.put(uuid, user);
        userIdToSession.put(user.id(), uuid);
        return uuid;
    }

    @Override
    public User bySession(String sessionId) {
        if (sessionToUser.containsKey(sessionId)) return sessionToUser.get(sessionId);
        return null;
    }

    @Override
    public long add(User user) {
        User added = new User(++LAST_ID, user.name(), user.passwordObfuscated(), user.email(), user.role());
        contents.put(LAST_ID, added);
        usedEmails.add(added.email());
        return LAST_ID;
    }

    @Override
    public boolean anyByEmail(String email) {
        return usedEmails.contains(email);
    }

    @Override
    public boolean updateUserRole(long id, UserRole role) {
        System.out.println("\"Unimplemented operation for in-memory repository!\"");
        return false;
    }
}
