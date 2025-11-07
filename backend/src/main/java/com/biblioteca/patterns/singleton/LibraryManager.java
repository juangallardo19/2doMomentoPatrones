package com.biblioteca.patterns.singleton;

import com.biblioteca.models.Book;
import com.biblioteca.models.Loan;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PATRÓN SINGLETON - LibraryManager
 *
 * Propósito: Garantizar una única instancia del gestor de la biblioteca
 * que centralice todas las operaciones y datos.
 *
 * Características:
 * - Constructor privado
 * - Instancia única estática
 * - Thread-safe
 *
 * Responsabilidades:
 * - Gestionar el catálogo de libros
 * - Gestionar préstamos
 * - Operaciones de búsqueda
 */
public class LibraryManager {

    // Instancia única del Singleton
    private static LibraryManager instance;

    // Catálogo de libros
    private List<Book> books;

    // Registro de préstamos
    private List<Loan> loans;

    // Contador para IDs auto-incrementales
    private int nextBookId;
    private int nextLoanId;

    /**
     * Constructor privado - Previene instanciación externa
     * Característica clave del patrón Singleton
     */
    private LibraryManager() {
        this.books = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.nextBookId = 1;
        this.nextLoanId = 1;
        System.out.println("📚 LibraryManager (Singleton) inicializado");
    }

    /**
     * Método estático para obtener la única instancia
     * Implementación thread-safe con sincronización
     *
     * @return La única instancia de LibraryManager
     */
    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    /**
     * Agrega un libro al catálogo
     *
     * @param book Libro a agregar
     */
    public void addBook(Book book) {
        if (book.getId() == 0) {
            book.setId(nextBookId++);
        } else {
            // Si el libro ya tiene ID, actualizamos el contador
            if (book.getId() >= nextBookId) {
                nextBookId = book.getId() + 1;
            }
        }
        books.add(book);
        System.out.println("➕ Libro agregado: " + book.getTitle() + " (ID: " + book.getId() + ")");
    }

    /**
     * Obtiene todos los libros del catálogo
     *
     * @return Lista de todos los libros
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Busca un libro por ID
     *
     * @param id ID del libro
     * @return Libro encontrado o null
     */
    public Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca libros por título (búsqueda parcial)
     *
     * @param title Título a buscar
     * @return Lista de libros que coinciden
     */
    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca libros por categoría
     *
     * @param category Categoría a buscar
     * @return Lista de libros de esa categoría
     */
    public List<Book> getBooksByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Agrega un préstamo al registro
     *
     * @param loan Préstamo a agregar
     */
    public void addLoan(Loan loan) {
        if (loan.getId() == 0) {
            loan.setId(nextLoanId++);
        } else {
            if (loan.getId() >= nextLoanId) {
                nextLoanId = loan.getId() + 1;
            }
        }
        loans.add(loan);
        System.out.println("📖 Préstamo registrado: ID " + loan.getId());
    }

    /**
     * Obtiene todos los préstamos
     *
     * @return Lista de todos los préstamos
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Obtiene préstamos de un usuario específico
     *
     * @param username Nombre de usuario
     * @return Lista de préstamos del usuario
     */
    public List<Loan> getLoansByUser(String username) {
        return loans.stream()
                .filter(loan -> loan.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * Busca un préstamo por ID
     *
     * @param id ID del préstamo
     * @return Préstamo encontrado o null
     */
    public Loan getLoanById(int id) {
        return loans.stream()
                .filter(loan -> loan.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene estadísticas de la biblioteca
     *
     * @return String con estadísticas
     */
    public String getStatistics() {
        long activeLoans = loans.stream().filter(loan -> !loan.isReturned()).count();
        return String.format(
            "📊 Estadísticas:\n" +
            "   - Libros en catálogo: %d\n" +
            "   - Préstamos totales: %d\n" +
            "   - Préstamos activos: %d",
            books.size(), loans.size(), activeLoans
        );
    }

    /**
     * Reinicia la biblioteca (útil para testing)
     */
    public void reset() {
        books.clear();
        loans.clear();
        nextBookId = 1;
        nextLoanId = 1;
        System.out.println("🔄 LibraryManager reiniciado");
    }
}
