package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Digital Library Application
 * Library management system with design pattern implementation:
 * - Singleton: AuthenticationManager, LibraryManager
 * - Factory Method: BookFactory
 * - Facade: LibraryFacade
 */
@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("📚 DIGITAL LIBRARY - System Started");
        System.out.println("===========================================");
        System.out.println("🌐 Server: http://localhost:8080");
        System.out.println("📖 Implemented Patterns:");
        System.out.println("   - Singleton");
        System.out.println("   - Factory Method");
        System.out.println("   - Facade");
        System.out.println("===========================================\n");
    }
}
