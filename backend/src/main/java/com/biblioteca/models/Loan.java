package com.biblioteca.models;

import java.time.LocalDate;

/**
 * Loan Model
 * Represents a book loan transaction in the digital library system
 *
 * Tracks:
 * - Which user borrowed which book
 * - Loan dates (borrow and due dates)
 * - Return status and date
 */
public class Loan {

    private int id;
    private String username;
    private int bookId;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;

    /**
     * Default constructor
     */
    public Loan() {
        this.returned = false;
    }

    /**
     * Full constructor
     */
    public Loan(int id, String username, int bookId, String bookTitle,
                LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.username = username;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.returnDate = null;
    }

    /**
     * Constructor without ID (for new loans)
     */
    public Loan(String username, int bookId, String bookTitle,
                LocalDate loanDate, LocalDate dueDate) {
        this(0, username, bookId, bookTitle, loanDate, dueDate);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Check if loan is overdue
     */
    public boolean isOverdue() {
        if (returned) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Get days until due date (negative if overdue)
     */
    public long getDaysUntilDue() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * Get days overdue (0 if not overdue)
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("Loan{id=%d, username='%s', bookId=%d, bookTitle='%s', loanDate=%s, dueDate=%s, returned=%s}",
                id, username, bookId, bookTitle, loanDate, dueDate, returned);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return id == loan.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
