package com.biblioteca.models;

/**
 * User Model
 * Represents a user in the digital library system
 *
 * User Types:
 * - ADMIN: Full access to all features
 * - USER: Regular user with borrowing privileges
 * - GUEST: Limited read-only access
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ADMIN, USER, GUEST
    private String fullName;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Full constructor
     */
    public User(int id, String username, String password, String email, String role, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    /**
     * Constructor without ID (for new users)
     */
    public User(String username, String password, String email, String role, String fullName) {
        this(0, username, password, email, role, fullName);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Check if user is regular user
     */
    public boolean isRegularUser() {
        return "USER".equalsIgnoreCase(role);
    }

    /**
     * Check if user is guest
     */
    public boolean isGuest() {
        return "GUEST".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', role='%s', fullName='%s'}",
                id, username, role, fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return id * 31 + username.hashCode();
    }
}
