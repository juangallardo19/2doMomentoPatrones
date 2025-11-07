package com.biblioteca.patterns.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN FACTORY METHOD - BookFactory (Creador)
 *
 * Propósito: Encapsular la lógica de creación de diferentes tipos de libros
 * sin exponer la lógica de instanciación al cliente.
 *
 * Ventajas:
 * - Desacopla la creación de objetos del código que los usa
 * - Facilita agregar nuevos tipos de libros sin modificar código existente
 * - Centraliza la lógica de creación
 *
 * El patrón Factory Method define una interfaz para crear objetos,
 * pero permite que las subclases decidan qué clase instanciar.
 */
public class BookFactory {

    /**
     * Factory Method principal
     * Crea un libro basado en el tipo especificado
     *
     * @param bookType Tipo de libro (DIGITAL, AUDIO, EBOOK)
     * @param params Parámetros necesarios para crear el libro
     * @return Instancia de Book del tipo correspondiente
     * @throws IllegalArgumentException si el tipo no es válido
     */
    public static Book createBook(String bookType, Map<String, Object> params) {
        if (bookType == null || bookType.isEmpty()) {
            throw new IllegalArgumentException("El tipo de libro no puede ser nulo o vacío");
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
                    "Tipo de libro no válido: " + bookType +
                    ". Tipos válidos: DIGITAL, AUDIO, EBOOK"
                );
        }
    }

    /**
     * Factory Method específico para libros digitales
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

        System.out.println("🏭 Factory: Creando DigitalBook - " + title);
        return new DigitalBook(title, author, isbn, category, fileFormat, fileSizeMB);
    }

    /**
     * Factory Method específico para audiolibros
     */
    private static AudioBook createAudioBook(Map<String, Object> params) {
        String title = (String) params.get("title");
        String author = (String) params.get("author");
        String isbn = (String) params.get("isbn");
        String category = (String) params.get("category");
        String narrator = (String) params.getOrDefault("narrator", "Desconocido");
        int durationMinutes = params.containsKey("durationMinutes")
            ? ((Number) params.get("durationMinutes")).intValue()
            : 300;
        String audioFormat = (String) params.getOrDefault("audioFormat", "MP3");

        System.out.println("🏭 Factory: Creando AudioBook - " + title);
        return new AudioBook(title, author, isbn, category, narrator, durationMinutes, audioFormat);
    }

    /**
     * Factory Method específico para eBooks
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
        String publisher = (String) params.getOrDefault("publisher", "Desconocida");

        System.out.println("🏭 Factory: Creando EBook - " + title);
        return new EBook(title, author, isbn, category, hasInteractiveContent, pageCount, publisher);
    }

    /**
     * Método helper para crear parámetros fácilmente
     */
    public static Map<String, Object> createParams() {
        return new HashMap<>();
    }

    /**
     * Valida que los parámetros mínimos estén presentes
     */
    private static void validateRequiredParams(Map<String, Object> params) {
        String[] required = {"title", "author", "isbn", "category"};
        for (String param : required) {
            if (!params.containsKey(param) || params.get(param) == null) {
                throw new IllegalArgumentException("Parámetro requerido faltante: " + param);
            }
        }
    }
}
