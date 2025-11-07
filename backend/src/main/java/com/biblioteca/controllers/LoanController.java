package com.biblioteca.controllers;

import com.biblioteca.models.Loan;
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
 * LoanController
 * REST Controller for loan operations
 *
 * Endpoints:
 * - POST /api/loans/borrow - Borrow a book
 * - POST /api/loans/return/{id} - Return a book
 * - GET /api/loans/history - Get user's loan history
 * - GET /api/loans - Get all loans (Admin only)
 * - GET /api/loans/{id} - Get loan by ID
 *
 * Uses Facade pattern for simplified loan operations
 */
@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private DataService dataService;

    private final LibraryFacade libraryFacade;
    private final LibraryManager libraryManager;

    public LoanController() {
        this.libraryFacade = LibraryFacade.getInstance();
        this.libraryManager = LibraryManager.getInstance();
    }

    /**
     * Initialize loans from JSON on startup
     * This method is called after all dependencies are injected
     */
    @PostConstruct
    private void initializeLoans() {
        List<Loan> loans = dataService.loadLoans();
        for (Loan loan : loans) {
            libraryManager.addLoan(loan);
        }
    }

    /**
     * Borrow a book
     * POST /api/loans/borrow
     * Headers: Authorization: Bearer {token}
     * Body: { "bookId": 1 }
     */
    @PostMapping("/borrow")
    public ResponseEntity<Map<String, Object>> borrowBook(
            @RequestBody Map<String, Integer> request,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        int bookId = request.get("bookId");

        // Use Facade to borrow book
        Loan loan = libraryFacade.borrowBook(token, bookId);

        if (loan == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Unable to borrow book. Book may not be available."));
        }

        // Save to JSON
        dataService.saveLoans(libraryManager.getAllLoans());
        dataService.saveBooks(libraryManager.getAllBooks());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Book borrowed successfully");
        response.put("loan", loanToMap(loan));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Return a book
     * POST /api/loans/return/{id}
     * Headers: Authorization: Bearer {token}
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<Map<String, Object>> returnBook(
            @PathVariable int id,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        // Use Facade to return book
        boolean success = libraryFacade.returnBook(token, id);

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Unable to return book. Loan may not exist or already returned."));
        }

        // Save to JSON
        dataService.saveLoans(libraryManager.getAllLoans());
        dataService.saveBooks(libraryManager.getAllBooks());

        Loan loan = libraryManager.getLoanById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Book returned successfully");
        response.put("loan", loanToMap(loan));

        return ResponseEntity.ok(response);
    }

    /**
     * Get user's loan history
     * GET /api/loans/history
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getUserLoanHistory(
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        List<Loan> loans = libraryFacade.getUserLoanHistory(token);

        if (loans.isEmpty() && !libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        List<Map<String, Object>> loanList = loans.stream()
                .map(this::loanToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("loans", loanList);
        response.put("count", loanList.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Get all loans (Admin only)
     * GET /api/loans
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLoans(
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

        List<Loan> loans = libraryManager.getAllLoans();

        List<Map<String, Object>> loanList = loans.stream()
                .map(this::loanToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("loans", loanList);
        response.put("count", loanList.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Get loan by ID
     * GET /api/loans/{id}
     * Headers: Authorization: Bearer {token}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLoanById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        if (!libraryFacade.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        Loan loan = libraryManager.getLoanById(id);

        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Loan not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("loan", loanToMap(loan));

        return ResponseEntity.ok(response);
    }

    /**
     * Convert Loan to Map for JSON response
     */
    private Map<String, Object> loanToMap(Loan loan) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", loan.getId());
        map.put("username", loan.getUsername());
        map.put("bookId", loan.getBookId());
        map.put("bookTitle", loan.getBookTitle());
        map.put("loanDate", loan.getLoanDate().toString());
        map.put("dueDate", loan.getDueDate().toString());
        map.put("returned", loan.isReturned());
        map.put("returnDate", loan.getReturnDate() != null ? loan.getReturnDate().toString() : null);
        map.put("overdue", loan.isOverdue());
        map.put("daysUntilDue", loan.getDaysUntilDue());
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
