package com.biblioteca.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Clase base abstracta Book
 *
 * Propósito: Definir la interfaz común para todos los tipos de libros
 * que serán creados por el Factory.
 *
 * Parte del patrón Factory Method donde:
 * - Esta clase es el "Producto" abstracto
 * - Las subclases (DigitalBook, AudioBook, EBook) son "Productos Concretos"
 * - BookFactory es el "Creador"
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
     * Constructor base
     */
    public Book(String title, String author, String isbn, String category, String bookType) {
        this.id = 0; // Será asignado por LibraryManager
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.available = true;
        this.bookType = bookType;
    }

    /**
     * Método abstracto - Cada tipo de libro implementa su propia descripción
     * Parte del patrón Factory Method
     */
    public abstract String getBookDetails();

    /**
     * Método abstracto - Comportamiento específico de cada tipo de libro
     */
    public abstract String getAccessMethod();

    // Getters y Setters
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
