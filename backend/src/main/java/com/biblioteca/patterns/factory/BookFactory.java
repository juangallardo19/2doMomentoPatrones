package com.biblioteca.patterns.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * FACTORY METHOD PATTERN - BookFactory (Creator)
 *
 * Purpose: Encapsulate the creation logic for different book types
 * without exposing instantiation logic to the client.
 *
 * Advantages:
 * - Decouples object creation from the code that uses them
 * - Makes it easy to add new book types without modifying existing code
 * - Centralizes creation logic
 *
 * The Factory Method pattern defines an interface for creating objects,
 * but lets subclasses decide which class to instantiate.
 */
public class BookFactory {

    /**
     * Main Factory Method
     * Creates a book based on the specified type
     *
     * @param bookType Type of book (DIGITAL, AUDIO, EBOOK)
     * @param params Parameters needed to create the book
     * @return Book instance of the corresponding type
     * @throws IllegalArgumentException if the type is not valid
     */
    public static Book createBook(String bookType, Map<String, Object> params) {
        if (bookType == null || bookType.isEmpty()) {
            throw new IllegalArgumentException("Book type cannot be null or empty");
        }

        switch (bookType.toUpperCase()) {
            case "DIGITAL":
                return createDigitalBook(params);

            case "AUDIO":
                return createAudioBook(params);

            case "EBOOK":
                return createEBook(params);

            default:
                throw new IllegalArgumentException(
                    "Invalid book type: " + bookType +
                    ". Valid types: DIGITAL, AUDIO, EBOOK"
                );
        }
    }

    /**
     * Specific Factory Method for digital books
     */
    private static DigitalBook createDigitalBook(Map<String, Object> params) {
        String title = (String) params.get("title");
        String author = (String) params.get("author");
        String isbn = (String) params.get("isbn");
        String category = (String) params.get("category");
        String fileFormat = (String) params.getOrDefault("fileFormat", "PDF");
        double fileSizeMB = params.containsKey("fileSizeMB")
            ? ((Number) params.get("fileSizeMB")).doubleValue()
            : 10.0;

        System.out.println("🏭 Factory: Creating DigitalBook - " + title);
        return new DigitalBook(title, author, isbn, category, fileFormat, fileSizeMB);
    }

    /**
     * Specific Factory Method for audiobooks
     */
    private static AudioBook createAudioBook(Map<String, Object> params) {
        String title = (String) params.get("title");
        String author = (String) params.get("author");
        String isbn = (String) params.get("isbn");
        String category = (String) params.get("category");
        String narrator = (String) params.getOrDefault("narrator", "Unknown");
        int durationMinutes = params.containsKey("durationMinutes")
            ? ((Number) params.get("durationMinutes")).intValue()
            : 300;
        String audioFormat = (String) params.getOrDefault("audioFormat", "MP3");

        System.out.println("🏭 Factory: Creating AudioBook - " + title);
        return new AudioBook(title, author, isbn, category, narrator, durationMinutes, audioFormat);
    }

    /**
     * Specific Factory Method for eBooks
     */
    private static EBook createEBook(Map<String, Object> params) {
        String title = (String) params.get("title");
        String author = (String) params.get("author");
        String isbn = (String) params.get("isbn");
        String category = (String) params.get("category");
        boolean hasInteractiveContent = params.containsKey("hasInteractiveContent")
            ? (Boolean) params.get("hasInteractiveContent")
            : false;
        int pageCount = params.containsKey("pageCount")
            ? ((Number) params.get("pageCount")).intValue()
            : 200;
        String publisher = (String) params.getOrDefault("publisher", "Unknown");

        System.out.println("🏭 Factory: Creating EBook - " + title);
        return new EBook(title, author, isbn, category, hasInteractiveContent, pageCount, publisher);
    }

    /**
     * Helper method to create parameters easily
     */
    public static Map<String, Object> createParams() {
        return new HashMap<>();
    }

    /**
     * Validates that minimum required parameters are present
     */
    private static void validateRequiredParams(Map<String, Object> params) {
        String[] required = {"title", "author", "isbn", "category"};
        for (String param : required) {
            if (!params.containsKey(param) || params.get(param) == null) {
                throw new IllegalArgumentException("Missing required parameter: " + param);
            }
        }
    }
}
