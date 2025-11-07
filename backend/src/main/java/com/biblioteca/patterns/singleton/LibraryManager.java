package com.biblioteca.patterns.singleton;

import com.biblioteca.patterns.factory.Book;
import com.biblioteca.models.Loan;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SINGLETON PATTERN - LibraryManager
 *
 * Purpose: Ensure a unique instance of the library manager
 * that centralizes all operations and data.
 *
 * Characteristics:
 * - Private constructor
 * - Unique static instance
 * - Thread-safe
 *
 * Responsibilities:
 * - Manage book catalog
 * - Manage loans
 * - Search operations
 */
public class LibraryManager {

    // Unique Singleton instance
    private static LibraryManager instance;

    // Book catalog
    private List<Book> books;

    // Loan registry
    private List<Loan> loans;

    // Counter for auto-incremental IDs
    private int nextBookId;
    private int nextLoanId;

    /**
     * Private constructor - Prevents external instantiation
     * Key characteristic of the Singleton pattern
     */
    private LibraryManager() {
        this.books = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.nextBookId = 1;
        this.nextLoanId = 1;
        System.out.println("📚 LibraryManager (Singleton) initialized");
    }

    /**
     * Static method to get the unique instance
     * Thread-safe implementation with synchronization
     *
     * @return The unique instance of LibraryManager
     */
    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    /**
     * Adds a book to the catalog
     *
     * @param book Book to add
     */
    public void addBook(Book book) {
        if (book.getId() == 0) {
            book.setId(nextBookId++);
        } else {
            // If book already has ID, update the counter
            if (book.getId() >= nextBookId) {
                nextBookId = book.getId() + 1;
            }
        }
        books.add(book);
        System.out.println("➕ Book added: " + book.getTitle() + " (ID: " + book.getId() + ")");
    }

    /**
     * Gets all books from the catalog
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Searches for a book by ID
     *
     * @param id Book ID
     * @return Found book or null
     */
    public Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches books by title (partial search)
     *
     * @param title Title to search
     * @return List of matching books
     */
    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Searches books by category
     *
     * @param category Category to search
     * @return List of books in that category
     */
    public List<Book> getBooksByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Adds a loan to the registry
     *
     * @param loan Loan to add
     */
    public void addLoan(Loan loan) {
        if (loan.getId() == 0) {
            loan.setId(nextLoanId++);
        } else {
            if (loan.getId() >= nextLoanId) {
                nextLoanId = loan.getId() + 1;
            }
        }
        loans.add(loan);
        System.out.println("📖 Loan registered: ID " + loan.getId());
    }

    /**
     * Gets all loans
     *
     * @return List of all loans
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Gets loans for a specific user
     *
     * @param username Username
     * @return List of user's loans
     */
    public List<Loan> getLoansByUser(String username) {
        return loans.stream()
                .filter(loan -> loan.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * Searches for a loan by ID
     *
     * @param id Loan ID
     * @return Found loan or null
     */
    public Loan getLoanById(int id) {
        return loans.stream()
                .filter(loan -> loan.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets library statistics
     *
     * @return String with statistics
     */
    public String getStatistics() {
        long activeLoans = loans.stream().filter(loan -> !loan.isReturned()).count();
        return String.format(
            "📊 Statistics:\n" +
            "   - Books in catalog: %d\n" +
            "   - Total loans: %d\n" +
            "   - Active loans: %d",
            books.size(), loans.size(), activeLoans
        );
    }

    /**
     * Resets the library (useful for testing)
     */
    public void reset() {
        books.clear();
        loans.clear();
        nextBookId = 1;
        nextLoanId = 1;
        System.out.println("🔄 LibraryManager reset");
    }
}
