package com.biblioteca.patterns.factory;

/**
 * PATRÓN FACTORY METHOD - Producto Concreto: AudioBook
 *
 * Representa un audiolibro
 * Implementa los métodos abstractos de Book con comportamiento específico
 */
public class AudioBook extends Book {

    private String narrator; // Narrador
    private int durationMinutes; // Duración en minutos
    private String audioFormat; // MP3, AAC, etc.

    /**
     * Constructor
     */
    public AudioBook(String title, String author, String isbn, String category,
                     String narrator, int durationMinutes, String audioFormat) {
        super(title, author, isbn, category, "AUDIO");
        this.narrator = narrator;
        this.durationMinutes = durationMinutes;
        this.audioFormat = audioFormat;
    }

    @Override
    public String getBookDetails() {
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        return String.format(
            "🎧 Audiolibro\n" +
            "   Título: %s\n" +
            "   Autor: %s\n" +
            "   ISBN: %s\n" +
            "   Categoría: %s\n" +
            "   Narrador: %s\n" +
            "   Duración: %dh %dmin\n" +
            "   Formato: %s",
            title, author, isbn, category, narrator, hours, minutes, audioFormat
        );
    }

    @Override
    public String getAccessMethod() {
        return "Streaming o descarga en formato " + audioFormat + " - Narrado por " + narrator;
    }

    // Getters y Setters específicos
    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }
}
