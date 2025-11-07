package com.biblioteca.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Producto Concreto: EBook
 *
 * Representa un libro electrónico interactivo
 * Implementa los métodos abstractos de Book con comportamiento específico
 */
public class EBook extends Book {

    private boolean hasInteractiveContent; // Contenido interactivo
    private int pageCount; // Número de páginas
    private String publisher; // Editorial

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
            "   Título: %s\n" +
            "   Autor: %s\n" +
            "   ISBN: %s\n" +
            "   Categoría: %s\n" +
            "   Editorial: %s\n" +
            "   Páginas: %d\n" +
            "   Contenido interactivo: %s",
            title, author, isbn, category, publisher, pageCount,
            hasInteractiveContent ? "Sí" : "No"
        );
    }

    @Override
    public String getAccessMethod() {
        String interactive = hasInteractiveContent ? " con contenido multimedia interactivo" : "";
        return "Lectura en línea" + interactive + " - " + pageCount + " páginas";
    }

    // Getters y Setters específicos
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
