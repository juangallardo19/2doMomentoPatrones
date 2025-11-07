package com.biblioteca.patterns.factory;

/**
 * FACTORY METHOD PATTERN - Concrete Product: AudioBook
 *
 * Represents an audiobook
 * Implements Book's abstract methods with specific behavior
 */
public class AudioBook extends Book {

    private String narrator; // Narrator
    private int durationMinutes; // Duration in minutes
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
            "🎧 AudioBook\n" +
            "   Title: %s\n" +
            "   Author: %s\n" +
            "   ISBN: %s\n" +
            "   Category: %s\n" +
            "   Narrator: %s\n" +
            "   Duration: %dh %dmin\n" +
            "   Format: %s",
            title, author, isbn, category, narrator, hours, minutes, audioFormat
        );
    }

    @Override
    public String getAccessMethod() {
        return "Streaming or download in " + audioFormat + " format - Narrated by " + narrator;
    }

    // Specific Getters and Setters
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
