package com.biblioteca.patterns.factory;

/**
 * FACTORY METHOD PATTERN - Concrete Product: DigitalBook
 *
 * Represents a digital book in PDF format
 * Implements Book's abstract methods with specific behavior
 */
public class DigitalBook extends Book {

    private String fileFormat; // PDF, EPUB, MOBI
    private double fileSizeMB;

    /**
     * Constructor
     */
    public DigitalBook(String title, String author, String isbn, String category,
                       String fileFormat, double fileSizeMB) {
        super(title, author, isbn, category, "DIGITAL");
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
    }

    @Override
    public String getBookDetails() {
        return String.format(
            "📱 Digital Book\n" +
            "   Title: %s\n" +
            "   Author: %s\n" +
            "   ISBN: %s\n" +
            "   Category: %s\n" +
            "   Format: %s\n" +
            "   Size: %.2f MB",
            title, author, isbn, category, fileFormat, fileSizeMB
        );
    }

    @Override
    public String getAccessMethod() {
        return "Direct download in " + fileFormat + " format - Compatible with digital readers";
    }

    // Specific Getters and Setters
    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }
}
