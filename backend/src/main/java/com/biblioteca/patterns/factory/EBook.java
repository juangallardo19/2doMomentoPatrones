package com.biblioteca.patterns.factory;

/**
 * FACTORY METHOD PATTERN - Concrete Product: EBook
 *
 * Represents an interactive electronic book
 * Implements Book's abstract methods with specific behavior
 */
public class EBook extends Book {

    private boolean hasInteractiveContent; // Interactive content
    private int pageCount; // Number of pages
    private String publisher; // Publisher

    /**
     * Constructor
     */
    public EBook(String title, String author, String isbn, String category,
                 boolean hasInteractiveContent, int pageCount, String publisher) {
        super(title, author, isbn, category, "EBOOK");
        this.hasInteractiveContent = hasInteractiveContent;
        this.pageCount = pageCount;
        this.publisher = publisher;
    }

    @Override
    public String getBookDetails() {
        return String.format(
            "📚 E-Book\n" +
            "   Title: %s\n" +
            "   Author: %s\n" +
            "   ISBN: %s\n" +
            "   Category: %s\n" +
            "   Publisher: %s\n" +
            "   Pages: %d\n" +
            "   Interactive content: %s",
            title, author, isbn, category, publisher, pageCount,
            hasInteractiveContent ? "Yes" : "No"
        );
    }

    @Override
    public String getAccessMethod() {
        String interactive = hasInteractiveContent ? " with interactive multimedia content" : "";
        return "Online reading" + interactive + " - " + pageCount + " pages";
    }

    // Specific Getters and Setters
    public boolean isHasInteractiveContent() {
        return hasInteractiveContent;
    }

    public void setHasInteractiveContent(boolean hasInteractiveContent) {
        this.hasInteractiveContent = hasInteractiveContent;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
