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
