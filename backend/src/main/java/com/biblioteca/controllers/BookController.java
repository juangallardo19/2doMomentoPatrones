package com.biblioteca.controllers;

import com.biblioteca.patterns.factory.Book;
import com.biblioteca.patterns.factory.BookFactory;
import com.biblioteca.patterns.facade.LibraryFacade;
import com.biblioteca.patterns.singleton.LibraryManager;
import com.biblioteca.services.DataService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BookController
 * REST Controller for book operations
 *
 * Endpoints:
 * - GET /api/books - Get all books
 * - GET /api/books/{id} - Get book by ID
 * - GET /api/books/search - Search books by title
 * - POST /api/books - Create new book (Admin only)
 * - GET /api/books/category/{category} - Get books by category
 *
 * Uses Factory Method pattern for book creation
 * Uses Facade pattern for simplified operations
 */
@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private DataService dataService;

    private final LibraryFacade libraryFacade;
    private final LibraryManager libraryManager;

    public BookController() {
        this.libraryFacade = LibraryFacade.getInstance();
        this.libraryManager = LibraryManager.getInstance();
    }

    /**
     * Initialize books from JSON on startup
     * This method is called after all dependencies are injected
     */
    @PostConstruct
    private void initializeBooks() {
        List<Book> books = dataService.loadBooks();
        for (Book book : books) {
            libraryManager.addBook(book);
        }
    }

    /**
     * Get all books
     * GET /api/books
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBooks(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);

        List<Book> books = libraryFacade.getCatalog(token);

        if (books.isEmpty() && !libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        List<Map<String, Object>> bookList = books.stream()
                .map(this::bookToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("books", bookList);
        response.put("count", bookList.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Get book by ID
     * GET /api/books/{id}
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        Book book = libraryManager.getBookById(id);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Book not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("book", bookToMap(book));

        return ResponseEntity.ok(response);
    }

    /**
     * Search books by title
     * GET /api/books/search?q=query
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(
            @RequestParam("q") String query,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        List<Book> books = libraryFacade.searchBooks(token, query);

        if (books.isEmpty() && !libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        List<Map<String, Object>> bookList = books.stream()
                .map(this::bookToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("books", bookList);
        response.put("count", bookList.size());
        response.put("query", query);

        return ResponseEntity.ok(response);
    }

    /**
     * Create new book (Admin only)
     * POST /api/books
     * Headers: Authorization: Bearer {token}
     * Body: { "bookType": "DIGITAL", "title": "...", "author": "...", ... }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(
            @RequestBody Map<String, Object> bookData,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        // Check if user is admin
        if (!"ADMIN".equals(libraryFacade.getCurrentUser(token).getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Admin access required"));
        }

        try {
            String bookType = (String) bookData.get("bookType");
            Book book = libraryFacade.createAndAddBook(bookType, bookData);

            // Save to JSON
            dataService.saveBooks(libraryManager.getAllBooks());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book created successfully");
            response.put("book", bookToMap(book));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Error creating book: " + e.getMessage()));
        }
    }

    /**
     * Get books by category
     * GET /api/books/category/{category}
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getBooksByCategory(
            @PathVariable String category,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        List<Book> books = libraryManager.getBooksByCategory(category);

        List<Map<String, Object>> bookList = books.stream()
                .map(this::bookToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("books", bookList);
        response.put("category", category);
        response.put("count", bookList.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Convert Book to Map for JSON response
     */
    private Map<String, Object> bookToMap(Book book) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", book.getId());
        map.put("title", book.getTitle());
        map.put("author", book.getAuthor());
        map.put("isbn", book.getIsbn());
        map.put("category", book.getCategory());
        map.put("available", book.isAvailable());
        map.put("bookType", book.getBookType());
        map.put("accessMethod", book.getAccessMethod());
        return map;
    }

    /**
     * Extract token from Authorization header
     */
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}
