package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Biblioteca Digital
 * Sistema de gestión de biblioteca con implementación de patrones de diseño:
 * - Singleton: AuthenticationManager, LibraryManager
 * - Factory Method: BookFactory
 * - Facade: LibraryFacade
 */
@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("📚 BIBLIOTECA DIGITAL - Sistema Iniciado");
        System.out.println("===========================================");
        System.out.println("🌐 Servidor: http://localhost:8080");
        System.out.println("📖 Patrones implementados:");
        System.out.println("   - Singleton");
        System.out.println("   - Factory Method");
        System.out.println("   - Facade");
        System.out.println("===========================================\n");
    }
}
