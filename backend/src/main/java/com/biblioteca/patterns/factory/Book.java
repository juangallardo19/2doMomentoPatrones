package com.biblioteca.patterns.factory;

/**
 * FACTORY METHOD PATTERN - Abstract base class Book
 *
 * Purpose: Define the common interface for all book types
 * that will be created by the Factory.
 *
 * Part of the Factory Method pattern where:
 * - This class is the abstract "Product"
 * - The subclasses (DigitalBook, AudioBook, EBook) are "Concrete Products"
 * - BookFactory is the "Creator"
 */
public abstract class Book {

    protected int id;
    protected String title;
    protected String author;
    protected String isbn;
    protected String category;
    protected boolean available;
    protected String bookType; // Digital, Audio, EBook

    /**
     * Base constructor
     */
    public Book(String title, String author, String isbn, String category, String bookType) {
        this.id = 0; // Will be assigned by LibraryManager
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.available = true;
        this.bookType = bookType;
    }

    /**
     * Abstract method - Each book type implements its own description
     * Part of the Factory Method pattern
     */
    public abstract String getBookDetails();

    /**
     * Abstract method - Specific behavior for each book type
     */
    public abstract String getAccessMethod();

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (ISBN: %s)", bookType, title, author, isbn);
    }
}
