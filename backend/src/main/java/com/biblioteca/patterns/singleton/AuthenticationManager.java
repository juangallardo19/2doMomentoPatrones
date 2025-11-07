package com.biblioteca.patterns.singleton;

import com.biblioteca.models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PATRÓN SINGLETON - AuthenticationManager
 *
 * Propósito: Garantizar que solo exista una instancia del gestor de autenticación
 * en todo el sistema.
 *
 * Características:
 * - Constructor privado
 * - Instancia única estática
 * - Acceso global mediante getInstance()
 * - Thread-safe (sincronización)
 *
 * Responsabilidades:
 * - Gestionar sesiones de usuarios
 * - Login y logout
 * - Validar tokens de sesión
 */
public class AuthenticationManager {

    // Instancia única del Singleton
    private static AuthenticationManager instance;

    // Almacén de sesiones activas: token -> usuario
    private Map<String, User> activeSessions;

    // Usuario actualmente autenticado
    private User currentUser;

    /**
     * Constructor privado - Previene instanciación externa
     * Característica clave del patrón Singleton
     */
    private AuthenticationManager() {
        this.activeSessions = new HashMap<>();
        this.currentUser = null;
        System.out.println("🔐 AuthenticationManager (Singleton) inicializado");
    }

    /**
     * Método estático para obtener la única instancia
     * Implementación thread-safe con sincronización
     *
     * @return La única instancia de AuthenticationManager
     */
    public static synchronized AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    /**
     * Autentica un usuario y crea una sesión
     *
     * @param user Usuario a autenticar
     * @return Token de sesión único
     */
    public String login(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, user);
        currentUser = user;
        System.out.println("✅ Usuario autenticado: " + user.getUsername() + " (Rol: " + user.getRole() + ")");
        return token;
    }

    /**
     * Cierra la sesión de un usuario
     *
     * @param token Token de sesión a cerrar
     * @return true si se cerró exitosamente
     */
    public boolean logout(String token) {
        if (activeSessions.containsKey(token)) {
            User user = activeSessions.get(token);
            activeSessions.remove(token);
            if (currentUser != null && currentUser.equals(user)) {
                currentUser = null;
            }
            System.out.println("👋 Sesión cerrada: " + user.getUsername());
            return true;
        }
        return false;
    }

    /**
     * Valida si un token de sesión es válido
     *
     * @param token Token a validar
     * @return true si el token es válido
     */
    public boolean isValidToken(String token) {
        return activeSessions.containsKey(token);
    }

    /**
     * Obtiene el usuario asociado a un token
     *
     * @param token Token de sesión
     * @return Usuario asociado o null
     */
    public User getUserByToken(String token) {
        return activeSessions.get(token);
    }

    /**
     * Obtiene el usuario actual
     *
     * @return Usuario actual o null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Obtiene el número de sesiones activas
     *
     * @return Cantidad de sesiones activas
     */
    public int getActiveSessionsCount() {
        return activeSessions.size();
    }

    /**
     * Cierra todas las sesiones activas
     */
    public void clearAllSessions() {
        activeSessions.clear();
        currentUser = null;
        System.out.println("🧹 Todas las sesiones han sido cerradas");
    }
}
