# 📚 Sistema de Biblioteca Digital

Sistema de gestión de biblioteca digital desarrollado con **Java Spring Boot** que implementa tres patrones de diseño fundamentales.

## 🎯 Patrones de Diseño Implementados

### 1. Singleton 🔒
**Ubicación:** `backend/src/main/java/com/biblioteca/patterns/singleton/`

**Clases:**
- `AuthenticationManager.java`: Gestiona la autenticación y sesiones de usuarios
- `LibraryManager.java`: Gestiona el catálogo de libros y préstamos

**Propósito:** Garantizar que solo exista una instancia única de estos gestores en todo el sistema.

**Características:**
- Constructor privado
- Instancia estática única
- Método `getInstance()` sincronizado (thread-safe)
- Gestión centralizada de datos

### 2. Factory Method 🏭
**Ubicación:** `backend/src/main/java/com/biblioteca/patterns/factory/`

**Clases:**
- `Book.java`: Clase abstracta base (Producto)
- `DigitalBook.java`: Libro digital en PDF
- `AudioBook.java`: Audiolibro
- `EBook.java`: Libro electrónico interactivo
- `BookFactory.java`: Factory que crea los libros (Creador)

**Propósito:** Encapsular la lógica de creación de diferentes tipos de libros sin exponer la lógica de instanciación.

**Ventajas:**
- Desacopla la creación de objetos
- Fácil agregar nuevos tipos de libros
- Centraliza la lógica de creación

### 3. Facade 🎭
**Ubicación:** `backend/src/main/java/com/biblioteca/patterns/facade/`

**Clase:**
- `LibraryFacade.java`: Interfaz simplificada para operaciones complejas

**Propósito:** Proporcionar una interfaz unificada y simplificada para coordinar los subsistemas (AuthenticationManager, LibraryManager, BookFactory).

**Operaciones que simplifica:**
- Login/Logout de usuarios
- Crear y agregar libros al catálogo
- Préstamo de libros (con validaciones)
- Devolución de libros
- Búsqueda de libros
- Historial de préstamos

## 🏗️ Estructura del Proyecto

```
biblioteca-digital/
├── backend/
│   ├── src/main/java/com/biblioteca/
│   │   ├── BibliotecaApplication.java        # Clase principal
│   │   ├── patterns/
│   │   │   ├── singleton/                    # Patrón Singleton
│   │   │   │   ├── AuthenticationManager.java
│   │   │   │   └── LibraryManager.java
│   │   │   ├── factory/                      # Patrón Factory Method
│   │   │   │   ├── Book.java
│   │   │   │   ├── DigitalBook.java
│   │   │   │   ├── AudioBook.java
│   │   │   │   ├── EBook.java
│   │   │   │   └── BookFactory.java
│   │   │   └── facade/                       # Patrón Facade
│   │   │       └── LibraryFacade.java
│   │   ├── models/                           # Modelos (próximo)
│   │   ├── controllers/                      # Controllers REST (próximo)
│   │   └── services/                         # Services (próximo)
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── data/                             # Archivos JSON (próximo)
│   │   └── static/                           # Frontend (próximo)
│   └── pom.xml
└── README.md
```

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.1.5**
- **Maven**
- **Gson** (para manejo de JSON)

## 📋 Próximos Pasos

1. ✅ Estructura Maven y patrones de diseño
2. ⏳ Models, Controllers y Services
3. ⏳ Archivos JSON con datos
4. ⏳ Frontend HTML/CSS/JavaScript

## 🎓 Objetivo Académico

Este proyecto es un caso de estudio que demuestra la implementación práctica de tres patrones de diseño fundamentales en un sistema real de gestión de biblioteca digital.

---
**Desarrollado con ❤️ usando patrones de diseño**
