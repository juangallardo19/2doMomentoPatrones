package com.biblioteca.patterns.singleton;

import com.biblioteca.models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SINGLETON PATTERN - AuthenticationManager
 *
 * Purpose: Ensure only one instance of the authentication manager exists
 * throughout the entire system.
 *
 * Characteristics:
 * - Private constructor
 * - Unique static instance
 * - Global access through getInstance()
 * - Thread-safe (synchronized)
 *
 * Responsibilities:
 * - Manage user sessions
 * - Login and logout
 * - Validate session tokens
 */
public class AuthenticationManager {

    // Unique Singleton instance
    private static AuthenticationManager instance;

    // Active sessions storage: token -> user
    private Map<String, User> activeSessions;

    // Currently authenticated user
    private User currentUser;

    /**
     * Private constructor - Prevents external instantiation
     * Key characteristic of the Singleton pattern
     */
    private AuthenticationManager() {
        this.activeSessions = new HashMap<>();
        this.currentUser = null;
        System.out.println("🔐 AuthenticationManager (Singleton) initialized");
    }

    /**
     * Static method to get the unique instance
     * Thread-safe implementation with synchronization
     *
     * @return The unique instance of AuthenticationManager
     */
    public static synchronized AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    /**
     * Authenticates a user and creates a session
     *
     * @param user User to authenticate
     * @return Unique session token
     */
    public String login(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, user);
        currentUser = user;
        System.out.println("✅ User authenticated: " + user.getUsername() + " (Role: " + user.getRole() + ")");
        return token;
    }

    /**
     * Closes a user's session
     *
     * @param token Session token to close
     * @return true if closed successfully
     */
    public boolean logout(String token) {
        if (activeSessions.containsKey(token)) {
            User user = activeSessions.get(token);
            activeSessions.remove(token);
            if (currentUser != null && currentUser.equals(user)) {
                currentUser = null;
            }
            System.out.println("👋 Session closed: " + user.getUsername());
            return true;
        }
        return false;
    }

    /**
     * Validates if a session token is valid
     *
     * @param token Token to validate
     * @return true if the token is valid
     */
    public boolean isValidToken(String token) {
        return activeSessions.containsKey(token);
    }

    /**
     * Gets the user associated with a token
     *
     * @param token Session token
     * @return Associated user or null
     */
    public User getUserByToken(String token) {
        return activeSessions.get(token);
    }

    /**
     * Gets the current user
     *
     * @return Current user or null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the number of active sessions
     *
     * @return Number of active sessions
     */
    public int getActiveSessionsCount() {
        return activeSessions.size();
    }

    /**
     * Closes all active sessions
     */
    public void clearAllSessions() {
        activeSessions.clear();
        currentUser = null;
        System.out.println("🧹 All sessions have been closed");
    }
}
