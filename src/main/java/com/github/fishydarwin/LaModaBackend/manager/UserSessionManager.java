package com.github.fishydarwin.LaModaBackend.manager;

import com.github.fishydarwin.LaModaBackend.domain.User;

import java.util.*;

public class UserSessionManager {

    // TODO: it's probably better to use a DB table here for less vertical scaling.
    private static final Map<String, User> sessionToUser = new HashMap<>();
    private static final Map<Long, String> userIdToSession = new HashMap<>();
    private static final Map<String, Long> sessionUseTimestamps = new HashMap<>();

    private static final long SESSION_TIMEOUT = 15 * 60 * 1000; // 15 minutes
    private static Thread sessionAutoClearTask() {
        return new Thread(() -> {
            try {
                while (true) {

                    Set<String> sessionsToClear = new HashSet<>();
                    for (String sessionId : sessionUseTimestamps.keySet()) {
                        long timeSinceLastAccess = System.currentTimeMillis() - sessionUseTimestamps.get(sessionId);
                        if (timeSinceLastAccess > SESSION_TIMEOUT) sessionsToClear.add(sessionId);
                    }
                    sessionsToClear.forEach((sessionId) -> {
                        sessionToUser.remove(userIdToSession.get(sessionToUser.get(sessionId).id()));
                        sessionToUser.remove(sessionId);
                        sessionUseTimestamps.remove(sessionId);
                    });

                    Thread.sleep(1000 * 60); // not busy waiting, just not checking constantly...
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void init() {
        sessionAutoClearTask().start();
    }

    public static String generateSession(User user) {
        String sessionId = System.currentTimeMillis() + "-" + UUID.randomUUID() + "-" + UUID.randomUUID();

        if (userIdToSession.containsKey(user.id()))
            sessionToUser.remove(userIdToSession.get(user.id()));
        sessionToUser.put(sessionId, user);
        userIdToSession.put(user.id(), sessionId);
        return sessionId;
    }

    public static User bySession(String sessionId) {
        if (sessionToUser.containsKey(sessionId)) {
            return sessionToUser.get(sessionId);
        }
        return null;
    }

}
