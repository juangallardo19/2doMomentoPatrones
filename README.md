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

## 🏗️ Complete Project Structure

```
biblioteca-digital/
├── backend/
│   ├── src/main/java/com/biblioteca/
│   │   ├── BibliotecaApplication.java        # Main application
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
│   │   ├── models/                           # Data models
│   │   │   ├── User.java
│   │   │   └── Loan.java
│   │   ├── controllers/                      # REST Controllers
│   │   │   ├── AuthController.java
│   │   │   ├── BookController.java
│   │   │   └── LoanController.java
│   │   └── services/                         # Services
│   │       └── DataService.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── data/                             # JSON data files
│   │   │   ├── users.json
│   │   │   ├── books.json
│   │   │   └── loans.json
│   │   └── static/                           # Frontend
│   │       ├── index.html
│   │       ├── dashboard.html
│   │       ├── admin.html
│   │       ├── css/
│   │       │   └── styles.css
│   │       └── js/
│   │           ├── config.js
│   │           ├── auth.js
│   │           ├── dashboard.js
│   │           └── admin.js
│   └── pom.xml
└── README.md
```

## 🚀 Technologies

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Maven**
- **Gson** (for JSON handling)

### Frontend
- **HTML5**
- **CSS3** (with responsive design)
- **JavaScript** (ES6+)

## 📋 Features

### User Features
- ✅ Login system with role-based access
- ✅ Browse book catalog
- ✅ Search books by title
- ✅ Filter books by category
- ✅ View book details
- ✅ Borrow books
- ✅ Return books
- ✅ View loan history

### Admin Features
- ✅ All user features
- ✅ Create new books (using Factory Method)
- ✅ View all loans
- ✅ View library statistics
- ✅ Manage book catalog

## 🔐 Demo Credentials

```
Admin Account:
Username: admin
Password: admin123

Regular User:
Username: john
Password: user123

Guest User:
Username: guest
Password: guest
```

## 🚀 Deployment Instructions

### Prerequisites

Before running the application, ensure you have:

1. **Java JDK 17** or higher installed
   ```bash
   java -version
   ```

2. **Maven** installed
   ```bash
   mvn -version
   ```

### Option 1: Using Maven (Recommended)

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Open your browser and go to: `http://localhost:8080`
   - You'll see the login page

### Option 2: Building JAR file

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvn clean package
   ```

3. **Run the JAR file:**
   ```bash
   java -jar target/biblioteca-digital-1.0.0.jar
   ```

4. **Access the application:**
   - Open your browser and go to: `http://localhost:8080`

### Option 3: Using IDE (IntelliJ IDEA / Eclipse)

1. **Import the project:**
   - Open your IDE
   - Import as Maven project
   - Select the `backend` folder

2. **Run the application:**
   - Find `BibliotecaApplication.java`
   - Right-click → Run

3. **Access the application:**
   - Open your browser and go to: `http://localhost:8080`

## 📡 REST API Endpoints

### Authentication
```
POST   /api/auth/login       - User login
POST   /api/auth/logout      - User logout
GET    /api/auth/validate    - Validate token
GET    /api/auth/me          - Get current user
```

### Books
```
GET    /api/books                       - Get all books
GET    /api/books/{id}                  - Get book by ID
GET    /api/books/search?q={query}      - Search books
POST   /api/books                       - Create book (Admin)
GET    /api/books/category/{category}   - Get by category
```

### Loans
```
POST   /api/loans/borrow     - Borrow a book
POST   /api/loans/return/{id} - Return a book
GET    /api/loans/history    - User loan history
GET    /api/loans            - All loans (Admin)
GET    /api/loans/{id}       - Get loan by ID
```

## 🧪 Testing the Application

1. **Login as Admin:**
   - Username: `admin`
   - Password: `admin123`
   - You'll be redirected to the admin panel

2. **Test Admin Features:**
   - View all books
   - Create a new book (Factory Method pattern in action)
   - View all loans
   - Check library statistics

3. **Login as Regular User:**
   - Logout from admin
   - Login with: `john` / `user123`
   - Browse catalog
   - Borrow a book
   - View your loans
   - Return a book

## 📊 Data Files

The application uses JSON files for data persistence located in:
```
backend/src/main/resources/data/
```

- `users.json` - User accounts
- `books.json` - Book catalog
- `loans.json` - Loan records

## 🎨 Frontend Pages

1. **index.html** - Login page with credentials and design patterns info
2. **dashboard.html** - User dashboard with catalog and loans
3. **admin.html** - Admin panel with management features

## 🛠️ Troubleshooting

### Port Already in Use
If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

### Maven Download Issues
If Maven can't download dependencies, try:
```bash
mvn clean install -U
```

### Data Not Loading
Ensure JSON files are in the correct location:
```
backend/src/main/resources/data/
```

## 🎓 Academic Learning Objectives

This project demonstrates:

1. **Singleton Pattern**: Ensuring single instances of critical managers
2. **Factory Method Pattern**: Creating different book types dynamically
3. **Facade Pattern**: Simplifying complex subsystem operations
4. **REST API Design**: Building RESTful web services
5. **MVC Architecture**: Separating concerns (Models, Views, Controllers)
6. **Authentication**: Token-based authentication system
7. **Role-Based Access Control**: Different permissions for users
8. **Responsive Web Design**: Mobile-friendly interface

## 📝 Additional Notes

- All code and documentation is in **English**
- The application uses **in-memory data** (JSON files)
- **No database** is required
- Perfect for **educational purposes** and **demonstrations**

## 🎯 Future Enhancements

Possible improvements:
- Database integration (MySQL, PostgreSQL)
- User registration
- Email notifications
- Book ratings and reviews
- Advanced search filters
- File upload for book covers
- Export reports (PDF, Excel)

---

## 🚀 Quick Start Commands

```bash
# Clone the repository
git clone <repository-url>

# Navigate to backend
cd backend

# Run the application
mvn spring-boot:run

# Open browser
http://localhost:8080
```

---

**Developed with ❤️ using Design Patterns**

**Academic Project - Digital Library System**
