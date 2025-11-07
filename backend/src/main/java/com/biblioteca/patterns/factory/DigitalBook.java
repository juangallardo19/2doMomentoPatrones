package com.biblioteca.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Producto Concreto: DigitalBook
 *
 * Representa un libro digital en formato PDF
 * Implementa los métodos abstractos de Book con comportamiento específico
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
            "📱 Libro Digital\n" +
            "   Título: %s\n" +
            "   Autor: %s\n" +
            "   ISBN: %s\n" +
            "   Categoría: %s\n" +
            "   Formato: %s\n" +
            "   Tamaño: %.2f MB",
            title, author, isbn, category, fileFormat, fileSizeMB
        );
    }

    @Override
    public String getAccessMethod() {
        return "Descarga directa en formato " + fileFormat + " - Compatible con lectores digitales";
    }

    // Getters y Setters específicos
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
