package com.biblioteca.patterns.facade;

import com.biblioteca.patterns.singleton.AuthenticationManager;
import com.biblioteca.patterns.singleton.LibraryManager;
import com.biblioteca.patterns.factory.Book;
import com.biblioteca.patterns.factory.BookFactory;
import com.biblioteca.models.User;
import com.biblioteca.models.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * FACADE PATTERN - LibraryFacade
 *
 * Purpose: Provide a simplified interface for the complex library system,
 * hiding the complexity of the subsystems.
 *
 * Subsystems it coordinates:
 * - AuthenticationManager (Singleton) - Authentication
 * - LibraryManager (Singleton) - Book and loan management
 * - BookFactory (Factory Method) - Book creation
 *
 * Advantages:
 * - Simplifies system usage for clients
 * - Reduces dependencies between client code and subsystems
 * - Provides a single entry point for complex operations
 * - Facilitates maintenance by centralizing logic
 *
 * The Facade pattern provides a unified interface to a set of interfaces
 * in a subsystem, making the subsystem easier to use.
 */
public class LibraryFacade {

    // Unique Facade instance
    private static LibraryFacade instance;

    // References to subsystems (Singletons)
    private AuthenticationManager authManager;
    private LibraryManager libraryManager;

    /**
     * Private constructor
     * Initializes references to subsystems
     */
    private LibraryFacade() {
        this.authManager = AuthenticationManager.getInstance();
        this.libraryManager = LibraryManager.getInstance();
        System.out.println("🎭 LibraryFacade initialized - Facade pattern active");
    }

    /**
     * Gets the unique Facade instance
     */
    public static synchronized LibraryFacade getInstance() {
        if (instance == null) {
            instance = new LibraryFacade();
        }
        return instance;
    }

    /**
     * FACADE OPERATION: Complete login
     * Simplifies the authentication process
     *
     * @param user User to authenticate
     * @return Session token
     */
    public String loginUser(User user) {
        System.out.println("🎭 Facade: Processing login for " + user.getUsername());
        return authManager.login(user);
    }

    /**
     * FACADE OPERATION: Logout
     *
     * @param token Session token
     * @return true if closed successfully
     */
    public boolean logoutUser(String token) {
        System.out.println("🎭 Facade: Processing logout");
        return authManager.logout(token);
    }

    /**
     * FACADE OPERATION: Create and add a book to the catalog
     * Coordinates BookFactory and LibraryManager
     *
     * @param bookType Type of book
     * @param params Book parameters
     * @return Created book
     */
    public Book createAndAddBook(String bookType, Map<String, Object> params) {
        System.out.println("🎭 Facade: Creating and adding book type " + bookType);

        // Uses the Factory to create the book
        Book book = BookFactory.createBook(bookType, params);

        // Uses LibraryManager to add it to the catalog
        libraryManager.addBook(book);

        return book;
    }

    /**
     * FACADE OPERATION: Borrow a book
     * Coordinates AuthenticationManager, LibraryManager and validations
     *
     * @param token User's session token
     * @param bookId ID of the book to borrow
     * @return Loan object if successful, null if failed
     */
    public Loan borrowBook(String token, int bookId) {
        System.out.println("🎭 Facade: Processing book loan ID " + bookId);

        // 1. Validate token
        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Invalid token");
            return null;
        }

        // 2. Get user
        User user = authManager.getUserByToken(token);

        // 3. Get book
        Book book = libraryManager.getBookById(bookId);
        if (book == null) {
            System.out.println("❌ Book not found");
            return null;
        }

        // 4. Check availability
        if (!book.isAvailable()) {
            System.out.println("❌ Book not available");
            return null;
        }

        // 5. Create loan
        Loan loan = new Loan(
            0, // ID will be assigned by LibraryManager
            user.getUsername(),
            bookId,
            book.getTitle(),
            LocalDate.now(),
            LocalDate.now().plusDays(14) // 14 days loan period
        );

        // 6. Register loan and update availability
        libraryManager.addLoan(loan);
        book.setAvailable(false);

        System.out.println("✅ Successful loan: " + book.getTitle() + " for " + user.getUsername());
        return loan;
    }

    /**
     * FACADE OPERATION: Return a book
     *
     * @param token Session token
     * @param loanId Loan ID
     * @return true if successful
     */
    public boolean returnBook(String token, int loanId) {
        System.out.println("🎭 Facade: Processing book return ID " + loanId);

        // 1. Validate token
        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Invalid token");
            return false;
        }

        // 2. Get loan
        Loan loan = libraryManager.getLoanById(loanId);
        if (loan == null) {
            System.out.println("❌ Loan not found");
            return false;
        }

        // 3. Check if already returned
        if (loan.isReturned()) {
            System.out.println("❌ Book already returned");
            return false;
        }

        // 4. Get book and update availability
        Book book = libraryManager.getBookById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true);
        }

        // 5. Mark as returned
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        System.out.println("✅ Successful return: " + loan.getBookTitle());
        return true;
    }

    /**
     * FACADE OPERATION: Search books with authentication
     *
     * @param token Session token
     * @param searchTerm Search term
     * @return List of found books
     */
    public List<Book> searchBooks(String token, String searchTerm) {
        System.out.println("🎭 Facade: Searching books: " + searchTerm);

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Invalid token");
            return List.of();
        }

        return libraryManager.searchBooksByTitle(searchTerm);
    }

    /**
     * FACADE OPERATION: Get user's loan history
     *
     * @param token Session token
     * @return List of user's loans
     */
    public List<Loan> getUserLoanHistory(String token) {
        System.out.println("🎭 Facade: Getting loan history");

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Invalid token");
            return List.of();
        }

        User user = authManager.getUserByToken(token);
        return libraryManager.getLoansByUser(user.getUsername());
    }

    /**
     * FACADE OPERATION: Get complete catalog
     *
     * @param token Session token
     * @return List of all books
     */
    public List<Book> getCatalog(String token) {
        System.out.println("🎭 Facade: Getting complete catalog");

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Invalid token");
            return List.of();
        }

        return libraryManager.getAllBooks();
    }

    /**
     * FACADE OPERATION: Get statistics (admin only)
     *
     * @param token Session token
     * @return Library statistics
     */
    public String getStatistics(String token) {
        if (!authManager.isValidToken(token)) {
            return "❌ Invalid token";
        }

        User user = authManager.getUserByToken(token);
        if (!"ADMIN".equals(user.getRole())) {
            return "❌ Access denied. Administrators only.";
        }

        return libraryManager.getStatistics();
    }

    /**
     * Validates if a user is authenticated
     */
    public boolean isAuthenticated(String token) {
        return authManager.isValidToken(token);
    }

    /**
     * Gets the current user
     */
    public User getCurrentUser(String token) {
        return authManager.getUserByToken(token);
    }
}
