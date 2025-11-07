package com.biblioteca.controllers;

import com.biblioteca.models.User;
import com.biblioteca.patterns.facade.LibraryFacade;
import com.biblioteca.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AuthController
 * REST Controller for authentication operations
 *
 * Endpoints:
 * - POST /api/auth/login - User login
 * - POST /api/auth/logout - User logout
 * - GET /api/auth/validate - Validate token
 * - GET /api/auth/me - Get current user info
 *
 * Uses LibraryFacade pattern for simplified operations
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private DataService dataService;

    private final LibraryFacade libraryFacade;

    public AuthController() {
        this.libraryFacade = LibraryFacade.getInstance();
    }

    /**
     * Login endpoint
     * POST /api/auth/login
     * Body: { "username": "admin", "password": "admin123" }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Load users and validate
        List<User> users = dataService.loadUsers();
        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Use Facade to login
        String token = libraryFacade.loginUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", token);
        response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "fullName", user.getFullName()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint
     * POST /api/auth/logout
     * Headers: Authorization: Bearer {token}
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);

        boolean success = libraryFacade.logoutUser(token);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Logged out successfully" : "Invalid token");

        return ResponseEntity.ok(response);
    }

    /**
     * Validate token endpoint
     * GET /api/auth/validate
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        boolean isValid = libraryFacade.isAuthenticated(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            User user = libraryFacade.getCurrentUser(token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "role", user.getRole(),
                    "fullName", user.getFullName()
            ));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get current user info
     * GET /api/auth/me
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        User user = libraryFacade.getCurrentUser(token);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "fullName", user.getFullName()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Register new user
     * POST /api/auth/register
     * Body: { "username": "...", "password": "...", "email": "...", "fullName": "..." }
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        String email = userData.get("email");
        String fullName = userData.get("fullName");

        // Validate required fields
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "All fields are required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Load existing users
        List<User> users = dataService.loadUsers();

        // Check if username already exists
        boolean usernameExists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if (usernameExists) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // Check if email already exists
        boolean emailExists = users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));

        if (emailExists) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email already registered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // Create new user with USER role (not ADMIN)
        int newId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;

        User newUser = new User(newId, username, password, email, "USER", fullName);
        users.add(newUser);

        // Save to JSON
        dataService.saveUsers(users);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Registration successful");
        response.put("user", Map.of(
                "id", newUser.getId(),
                "username", newUser.getUsername(),
                "email", newUser.getEmail(),
                "role", newUser.getRole(),
                "fullName", newUser.getFullName()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Extract token from Authorization header
     * Format: "Bearer {token}"
     */
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}
