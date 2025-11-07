package com.biblioteca.services;

import com.biblioteca.models.User;
import com.biblioteca.models.Loan;
import com.biblioteca.patterns.factory.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataService
 * Service for handling JSON data persistence
 *
 * Responsibilities:
 * - Load initial data from JSON files
 * - Save data to JSON files
 * - Handle JSON serialization/deserialization with Gson
 * - Support for LocalDate and polymorphic Book types
 */
@Service
public class DataService {

    private final Gson gson;
    private final String dataPath = "data/";

    /**
     * Constructor
     * Initializes Gson with custom adapters for LocalDate and Book polymorphism
     */
    public DataService() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Book.class, new BookAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Load users from JSON file
     */
    public List<User> loadUsers() {
        try {
            String filePath = getResourcePath("users.json");
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(json, listType);
            System.out.println("✅ Loaded " + users.size() + " users from JSON");
            return users;
        } catch (Exception e) {
            System.out.println("⚠️ Error loading users: " + e.getMessage());
            return getDefaultUsers();
        }
    }

    /**
     * Load books from JSON file
     */
    public List<Book> loadBooks() {
        try {
            String filePath = getResourcePath("books.json");
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
            List<Book> books = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject bookObj = element.getAsJsonObject();
                String bookType = bookObj.get("bookType").getAsString();

                Map<String, Object> params = BookFactory.createParams();
                params.put("title", bookObj.get("title").getAsString());
                params.put("author", bookObj.get("author").getAsString());
                params.put("isbn", bookObj.get("isbn").getAsString());
                params.put("category", bookObj.get("category").getAsString());

                // Type-specific parameters
                if ("DIGITAL".equals(bookType)) {
                    params.put("fileFormat", bookObj.has("fileFormat") ? bookObj.get("fileFormat").getAsString() : "PDF");
                    params.put("fileSizeMB", bookObj.has("fileSizeMB") ? bookObj.get("fileSizeMB").getAsDouble() : 10.0);
                } else if ("AUDIO".equals(bookType)) {
                    params.put("narrator", bookObj.has("narrator") ? bookObj.get("narrator").getAsString() : "Unknown");
                    params.put("durationMinutes", bookObj.has("durationMinutes") ? bookObj.get("durationMinutes").getAsInt() : 300);
                    params.put("audioFormat", bookObj.has("audioFormat") ? bookObj.get("audioFormat").getAsString() : "MP3");
                } else if ("EBOOK".equals(bookType)) {
                    params.put("hasInteractiveContent", bookObj.has("hasInteractiveContent") ? bookObj.get("hasInteractiveContent").getAsBoolean() : false);
                    params.put("pageCount", bookObj.has("pageCount") ? bookObj.get("pageCount").getAsInt() : 200);
                    params.put("publisher", bookObj.has("publisher") ? bookObj.get("publisher").getAsString() : "Unknown");
                }

                Book book = BookFactory.createBook(bookType, params);
                if (bookObj.has("id")) {
                    book.setId(bookObj.get("id").getAsInt());
                }
                if (bookObj.has("available")) {
                    book.setAvailable(bookObj.get("available").getAsBoolean());
                }
                books.add(book);
            }

            System.out.println("✅ Loaded " + books.size() + " books from JSON");
            return books;
        } catch (Exception e) {
            System.out.println("⚠️ Error loading books: " + e.getMessage());
            return getDefaultBooks();
        }
    }

    /**
     * Load loans from JSON file
     */
    public List<Loan> loadLoans() {
        try {
            String filePath = getResourcePath("loans.json");
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            Type listType = new TypeToken<List<Loan>>() {}.getType();
            List<Loan> loans = gson.fromJson(json, listType);
            System.out.println("✅ Loaded " + loans.size() + " loans from JSON");
            return loans != null ? loans : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("⚠️ Error loading loans: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Save users to JSON file
     */
    public void saveUsers(List<User> users) {
        try {
            String json = gson.toJson(users);
            String filePath = getResourcePath("users.json");
            Files.write(Paths.get(filePath), json.getBytes());
            System.out.println("💾 Saved " + users.size() + " users to JSON");
        } catch (Exception e) {
            System.err.println("❌ Error saving users: " + e.getMessage());
        }
    }

    /**
     * Save books to JSON file
     */
    public void saveBooks(List<Book> books) {
        try {
            JsonArray jsonArray = new JsonArray();
            for (Book book : books) {
                JsonObject bookObj = new JsonObject();
                bookObj.addProperty("id", book.getId());
                bookObj.addProperty("title", book.getTitle());
                bookObj.addProperty("author", book.getAuthor());
                bookObj.addProperty("isbn", book.getIsbn());
                bookObj.addProperty("category", book.getCategory());
                bookObj.addProperty("available", book.isAvailable());
                bookObj.addProperty("bookType", book.getBookType());

                // Add type-specific fields
                if (book instanceof DigitalBook) {
                    DigitalBook db = (DigitalBook) book;
                    bookObj.addProperty("fileFormat", db.getFileFormat());
                    bookObj.addProperty("fileSizeMB", db.getFileSizeMB());
                } else if (book instanceof AudioBook) {
                    AudioBook ab = (AudioBook) book;
                    bookObj.addProperty("narrator", ab.getNarrator());
                    bookObj.addProperty("durationMinutes", ab.getDurationMinutes());
                    bookObj.addProperty("audioFormat", ab.getAudioFormat());
                } else if (book instanceof EBook) {
                    EBook eb = (EBook) book;
                    bookObj.addProperty("hasInteractiveContent", eb.isHasInteractiveContent());
                    bookObj.addProperty("pageCount", eb.getPageCount());
                    bookObj.addProperty("publisher", eb.getPublisher());
                }

                jsonArray.add(bookObj);
            }

            String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray);
            String filePath = getResourcePath("books.json");
            Files.write(Paths.get(filePath), json.getBytes());
            System.out.println("💾 Saved " + books.size() + " books to JSON");
        } catch (Exception e) {
            System.err.println("❌ Error saving books: " + e.getMessage());
        }
    }

    /**
     * Save loans to JSON file
     */
    public void saveLoans(List<Loan> loans) {
        try {
            String json = gson.toJson(loans);
            String filePath = getResourcePath("loans.json");
            Files.write(Paths.get(filePath), json.getBytes());
            System.out.println("💾 Saved " + loans.size() + " loans to JSON");
        } catch (Exception e) {
            System.err.println("❌ Error saving loans: " + e.getMessage());
        }
    }

    /**
     * Get resource file path
     */
    private String getResourcePath(String filename) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(dataPath + filename);
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            // If file doesn't exist in resources, create in current directory
            String path = "backend/src/main/resources/" + dataPath + filename;
            File file = new File(path);
            file.getParentFile().mkdirs();
            return file.getAbsolutePath();
        }
    }

    /**
     * Get default users if file doesn't exist
     */
    private List<User> getDefaultUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "admin", "admin123", "admin@biblioteca.com", "ADMIN", "Administrator"));
        users.add(new User(2, "john", "user123", "john@email.com", "USER", "John Doe"));
        users.add(new User(3, "guest", "guest", "guest@email.com", "GUEST", "Guest User"));
        return users;
    }

    /**
     * Get default books if file doesn't exist
     */
    private List<Book> getDefaultBooks() {
        List<Book> books = new ArrayList<>();

        Map<String, Object> params1 = BookFactory.createParams();
        params1.put("title", "Clean Code");
        params1.put("author", "Robert C. Martin");
        params1.put("isbn", "978-0132350884");
        params1.put("category", "Programming");
        params1.put("fileFormat", "PDF");
        params1.put("fileSizeMB", 15.5);
        Book book1 = BookFactory.createBook("DIGITAL", params1);
        book1.setId(1);
        books.add(book1);

        return books;
    }

    /**
     * Custom LocalDate adapter for Gson
     */
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString());
        }
    }

    /**
     * Custom Book adapter for polymorphic deserialization
     */
    private static class BookAdapter implements JsonDeserializer<Book> {
        @Override
        public Book deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            JsonObject jsonObject = json.getAsJsonObject();
            String bookType = jsonObject.get("bookType").getAsString();

            if ("DIGITAL".equals(bookType)) {
                return context.deserialize(json, DigitalBook.class);
            } else if ("AUDIO".equals(bookType)) {
                return context.deserialize(json, AudioBook.class);
            } else if ("EBOOK".equals(bookType)) {
                return context.deserialize(json, EBook.class);
            }

            throw new JsonParseException("Unknown book type: " + bookType);
        }
    }
}
