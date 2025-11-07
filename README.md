# 📚 Digital Library System

Digital library management system developed with **Java Spring Boot** implementing three fundamental design patterns.

## 🎯 Implemented Design Patterns

### 1. Singleton 🔒
**Location:** `backend/src/main/java/com/biblioteca/patterns/singleton/`

**Classes:**
- `AuthenticationManager.java`: Manages user authentication and sessions
- `LibraryManager.java`: Manages book catalog and loans

**Purpose:** Ensure only one unique instance of these managers exists throughout the system.

**Characteristics:**
- Private constructor
- Unique static instance
- Synchronized `getInstance()` method (thread-safe)
- Centralized data management

### 2. Factory Method 🏭
**Location:** `backend/src/main/java/com/biblioteca/patterns/factory/`

**Classes:**
- `Book.java`: Abstract base class (Product)
- `DigitalBook.java`: Digital book in PDF format
- `AudioBook.java`: Audiobook
- `EBook.java`: Interactive electronic book
- `BookFactory.java`: Factory that creates books (Creator)

**Purpose:** Encapsulate the creation logic for different book types without exposing instantiation logic.

**Advantages:**
- Decouples object creation
- Easy to add new book types
- Centralizes creation logic

### 3. Facade 🎭
**Location:** `backend/src/main/java/com/biblioteca/patterns/facade/`

**Class:**
- `LibraryFacade.java`: Simplified interface for complex operations

**Purpose:** Provide a unified and simplified interface to coordinate subsystems (AuthenticationManager, LibraryManager, BookFactory).

**Operations it simplifies:**
- User Login/Logout
- Create and add books to catalog
- Book borrowing (with validations)
- Book returns
- Book search
- Loan history

## 🏗️ Project Structure

```
biblioteca-digital/
├── backend/
│   ├── src/main/java/com/biblioteca/
│   │   ├── BibliotecaApplication.java        # Main class
│   │   ├── patterns/
│   │   │   ├── singleton/                    # Singleton Pattern
│   │   │   │   ├── AuthenticationManager.java
│   │   │   │   └── LibraryManager.java
│   │   │   ├── factory/                      # Factory Method Pattern
│   │   │   │   ├── Book.java
│   │   │   │   ├── DigitalBook.java
│   │   │   │   ├── AudioBook.java
│   │   │   │   ├── EBook.java
│   │   │   │   └── BookFactory.java
│   │   │   └── facade/                       # Facade Pattern
│   │   │       └── LibraryFacade.java
│   │   ├── models/                           # Models (upcoming)
│   │   ├── controllers/                      # REST Controllers (upcoming)
│   │   └── services/                         # Services (upcoming)
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── data/                             # JSON files (upcoming)
│   │   └── static/                           # Frontend (upcoming)
│   └── pom.xml
└── README.md
```

## 🚀 Technologies

- **Java 17**
- **Spring Boot 3.1.5**
- **Maven**
- **Gson** (for JSON handling)

## 📋 Next Steps

1. ✅ Maven structure and design patterns
2. ⏳ Models, Controllers and Services
3. ⏳ JSON data files
4. ⏳ Frontend HTML/CSS/JavaScript

## 🎓 Academic Objective

This project is a case study demonstrating the practical implementation of three fundamental design patterns in a real digital library management system.

---
**Developed with ❤️ using design patterns**
