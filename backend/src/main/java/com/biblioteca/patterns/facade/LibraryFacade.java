package com.biblioteca.patterns.facade;

import com.biblioteca.patterns.singleton.AuthenticationManager;
import com.biblioteca.patterns.singleton.LibraryManager;
import com.biblioteca.patterns.factory.Book;
import com.biblioteca.patterns.factory.BookFactory;
import com.biblioteca.models.User;
import com.biblioteca.models.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * PATRÓN FACADE - LibraryFacade
 *
 * Propósito: Proporcionar una interfaz simplificada para el sistema complejo
 * de la biblioteca, ocultando la complejidad de los subsistemas.
 *
 * Subsistemas que coordina:
 * - AuthenticationManager (Singleton) - Autenticación
 * - LibraryManager (Singleton) - Gestión de libros y préstamos
 * - BookFactory (Factory Method) - Creación de libros
 *
 * Ventajas:
 * - Simplifica el uso del sistema para los clientes
 * - Reduce las dependencias entre el código cliente y los subsistemas
 * - Proporciona un punto de entrada único para operaciones complejas
 * - Facilita el mantenimiento al centralizar la lógica
 *
 * El patrón Facade proporciona una interfaz unificada para un conjunto de
 * interfaces en un subsistema, haciendo que el subsistema sea más fácil de usar.
 */
public class LibraryFacade {

    // Instancia única del Facade
    private static LibraryFacade instance;

    // Referencias a los subsistemas (Singletons)
    private AuthenticationManager authManager;
    private LibraryManager libraryManager;

    /**
     * Constructor privado
     * Inicializa las referencias a los subsistemas
     */
    private LibraryFacade() {
        this.authManager = AuthenticationManager.getInstance();
        this.libraryManager = LibraryManager.getInstance();
        System.out.println("🎭 LibraryFacade inicializado - Patrón Facade activo");
    }

    /**
     * Obtiene la instancia única del Facade
     */
    public static synchronized LibraryFacade getInstance() {
        if (instance == null) {
            instance = new LibraryFacade();
        }
        return instance;
    }

    /**
     * OPERACIÓN FACADE: Login completo
     * Simplifica el proceso de autenticación
     *
     * @param user Usuario a autenticar
     * @return Token de sesión
     */
    public String loginUser(User user) {
        System.out.println("🎭 Facade: Procesando login para " + user.getUsername());
        return authManager.login(user);
    }

    /**
     * OPERACIÓN FACADE: Logout
     *
     * @param token Token de sesión
     * @return true si se cerró exitosamente
     */
    public boolean logoutUser(String token) {
        System.out.println("🎭 Facade: Procesando logout");
        return authManager.logout(token);
    }

    /**
     * OPERACIÓN FACADE: Crear y agregar un libro al catálogo
     * Coordina BookFactory y LibraryManager
     *
     * @param bookType Tipo de libro
     * @param params Parámetros del libro
     * @return Libro creado
     */
    public Book createAndAddBook(String bookType, Map<String, Object> params) {
        System.out.println("🎭 Facade: Creando y agregando libro tipo " + bookType);

        // Usa el Factory para crear el libro
        Book book = BookFactory.createBook(bookType, params);

        // Usa el LibraryManager para agregarlo al catálogo
        libraryManager.addBook(book);

        return book;
    }

    /**
     * OPERACIÓN FACADE: Prestar un libro
     * Coordina AuthenticationManager, LibraryManager y validaciones
     *
     * @param token Token de sesión del usuario
     * @param bookId ID del libro a prestar
     * @return Objeto Loan si fue exitoso, null si falló
     */
    public Loan borrowBook(String token, int bookId) {
        System.out.println("🎭 Facade: Procesando préstamo de libro ID " + bookId);

        // 1. Validar token
        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Token inválido");
            return null;
        }

        // 2. Obtener usuario
        User user = authManager.getUserByToken(token);

        // 3. Obtener libro
        Book book = libraryManager.getBookById(bookId);
        if (book == null) {
            System.out.println("❌ Libro no encontrado");
            return null;
        }

        // 4. Verificar disponibilidad
        if (!book.isAvailable()) {
            System.out.println("❌ Libro no disponible");
            return null;
        }

        // 5. Crear préstamo
        Loan loan = new Loan(
            0, // ID será asignado por LibraryManager
            user.getUsername(),
            bookId,
            book.getTitle(),
            LocalDate.now(),
            LocalDate.now().plusDays(14) // 14 días de préstamo
        );

        // 6. Registrar préstamo y actualizar disponibilidad
        libraryManager.addLoan(loan);
        book.setAvailable(false);

        System.out.println("✅ Préstamo exitoso: " + book.getTitle() + " para " + user.getUsername());
        return loan;
    }

    /**
     * OPERACIÓN FACADE: Devolver un libro
     *
     * @param token Token de sesión
     * @param loanId ID del préstamo
     * @return true si fue exitoso
     */
    public boolean returnBook(String token, int loanId) {
        System.out.println("🎭 Facade: Procesando devolución de préstamo ID " + loanId);

        // 1. Validar token
        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Token inválido");
            return false;
        }

        // 2. Obtener préstamo
        Loan loan = libraryManager.getLoanById(loanId);
        if (loan == null) {
            System.out.println("❌ Préstamo no encontrado");
            return false;
        }

        // 3. Verificar que no esté ya devuelto
        if (loan.isReturned()) {
            System.out.println("❌ El libro ya fue devuelto");
            return false;
        }

        // 4. Obtener libro y actualizar disponibilidad
        Book book = libraryManager.getBookById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true);
        }

        // 5. Marcar como devuelto
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        System.out.println("✅ Devolución exitosa: " + loan.getBookTitle());
        return true;
    }

    /**
     * OPERACIÓN FACADE: Búsqueda de libros con autenticación
     *
     * @param token Token de sesión
     * @param searchTerm Término de búsqueda
     * @return Lista de libros encontrados
     */
    public List<Book> searchBooks(String token, String searchTerm) {
        System.out.println("🎭 Facade: Buscando libros: " + searchTerm);

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Token inválido");
            return List.of();
        }

        return libraryManager.searchBooksByTitle(searchTerm);
    }

    /**
     * OPERACIÓN FACADE: Obtener historial de préstamos del usuario
     *
     * @param token Token de sesión
     * @return Lista de préstamos del usuario
     */
    public List<Loan> getUserLoanHistory(String token) {
        System.out.println("🎭 Facade: Obteniendo historial de préstamos");

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Token inválido");
            return List.of();
        }

        User user = authManager.getUserByToken(token);
        return libraryManager.getLoansByUser(user.getUsername());
    }

    /**
     * OPERACIÓN FACADE: Obtener catálogo completo
     *
     * @param token Token de sesión
     * @return Lista de todos los libros
     */
    public List<Book> getCatalog(String token) {
        System.out.println("🎭 Facade: Obteniendo catálogo completo");

        if (!authManager.isValidToken(token)) {
            System.out.println("❌ Token inválido");
            return List.of();
        }

        return libraryManager.getAllBooks();
    }

    /**
     * OPERACIÓN FACADE: Obtener estadísticas (solo admin)
     *
     * @param token Token de sesión
     * @return Estadísticas de la biblioteca
     */
    public String getStatistics(String token) {
        if (!authManager.isValidToken(token)) {
            return "❌ Token inválido";
        }

        User user = authManager.getUserByToken(token);
        if (!"ADMIN".equals(user.getRole())) {
            return "❌ Acceso denegado. Solo administradores.";
        }

        return libraryManager.getStatistics();
    }

    /**
     * Valida si un usuario está autenticado
     */
    public boolean isAuthenticated(String token) {
        return authManager.isValidToken(token);
    }

    /**
     * Obtiene el usuario actual
     */
    public User getCurrentUser(String token) {
        return authManager.getUserByToken(token);
    }
}
