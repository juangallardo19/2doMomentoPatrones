/**
 * Admin Panel
 * Manages administrative functions
 */

// Check authentication and admin role
if (!isAuthenticated()) {
    window.location.href = '/index.html';
}

const user = getCurrentUser();

if (user.role !== 'ADMIN') {
    alert('Access denied. Admin privileges required.');
    window.location.href = '/dashboard.html';
}

// Initialize admin panel
document.addEventListener('DOMContentLoaded', () => {
    initAdmin();
    setupEventListeners();
    loadBooks();
});

/**
 * Initialize admin panel
 */
function initAdmin() {
    document.getElementById('userInfo').textContent = `Admin: ${user.fullName}`;
}

/**
 * Setup event listeners
 */
function setupEventListeners() {
    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', logout);

    // Tab switching
    document.querySelectorAll('.tab-button').forEach(button => {
        button.addEventListener('click', () => {
            switchTab(button.dataset.tab);
        });
    });

    // Search functionality
    document.getElementById('searchBtn').addEventListener('click', searchBooks);
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') searchBooks();
    });

    // Add book form
    document.getElementById('addBookForm').addEventListener('submit', handleAddBook);

    // Book type change
    document.getElementById('bookType').addEventListener('change', handleBookTypeChange);
}

/**
 * Switch tabs
 */
function switchTab(tabName) {
    // Update buttons
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');

    // Update content
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.getElementById(`${tabName}Tab`).classList.add('active');

    // Load data for specific tabs
    if (tabName === 'allLoans') {
        loadAllLoans();
    } else if (tabName === 'statistics') {
        loadStatistics();
    }
}

/**
 * Load all books
 */
async function loadBooks() {
    const grid = document.getElementById('booksGrid');
    grid.innerHTML = '<div class="loading">Loading books...</div>';

    try {
        const data = await API.get('/books');

        if (data.success && data.books.length > 0) {
            displayBooks(data.books);
        } else {
            grid.innerHTML = '<div class="empty-state"><h3>No books found</h3></div>';
        }
    } catch (error) {
        console.error('Error loading books:', error);
        grid.innerHTML = '<div class="empty-state"><h3>Error loading books</h3></div>';
    }
}

/**
 * Display books in grid
 */
function displayBooks(books) {
    const grid = document.getElementById('booksGrid');
    grid.innerHTML = '';

    books.forEach(book => {
        const card = createBookCard(book);
        grid.appendChild(card);
    });
}

/**
 * Create book card element
 */
function createBookCard(book) {
    const card = document.createElement('div');
    card.className = 'book-card';

    const statusClass = book.available ? 'available' : 'unavailable';
    const statusText = book.available ? '✓ Available' : '✗ Not Available';

    card.innerHTML = `
        <div class="book-icon">${getBookIcon(book.bookType)}</div>
        <h3>${book.title}</h3>
        <p class="author">by ${book.author}</p>
        <span class="category">${book.category}</span>
        <span class="book-type ${book.bookType}">${book.bookType}</span>
        <p><strong>ISBN:</strong> ${book.isbn}</p>
        <p class="status ${statusClass}">${statusText}</p>
        <p style="font-size: 0.9rem; color: #666;">${book.accessMethod}</p>
    `;

    return card;
}

/**
 * Search books
 */
async function searchBooks() {
    const query = document.getElementById('searchInput').value.trim();

    if (!query) {
        loadBooks();
        return;
    }

    const grid = document.getElementById('booksGrid');
    grid.innerHTML = '<div class="loading">Searching...</div>';

    try {
        const data = await API.get(`/books/search?q=${encodeURIComponent(query)}`);

        if (data.success && data.books.length > 0) {
            displayBooks(data.books);
        } else {
            grid.innerHTML = '<div class="empty-state"><h3>No books found</h3></div>';
        }
    } catch (error) {
        console.error('Error searching books:', error);
        grid.innerHTML = '<div class="empty-state"><h3>Error searching books</h3></div>';
    }
}

/**
 * Load all loans
 */
async function loadAllLoans() {
    const container = document.getElementById('loansTable');
    container.innerHTML = '<div class="loading">Loading all loans...</div>';

    try {
        const data = await API.get('/loans');

        if (data.success && data.loans.length > 0) {
            displayLoans(data.loans);
        } else {
            container.innerHTML = '<div class="empty-state"><h3>No loans found</h3></div>';
        }
    } catch (error) {
        console.error('Error loading loans:', error);
        container.innerHTML = '<div class="empty-state"><h3>Error loading loans</h3></div>';
    }
}

/**
 * Display loans in table
 */
function displayLoans(loans) {
    const container = document.getElementById('loansTable');

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Book</th>
                    <th>Loan Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
    `;

    loans.forEach(loan => {
        const statusBadge = loan.returned
            ? '<span class="badge badge-success">Returned</span>'
            : loan.overdue
            ? '<span class="badge badge-danger">Overdue</span>'
            : '<span class="badge badge-warning">Active</span>';

        html += `
            <tr>
                <td>${loan.id}</td>
                <td><strong>${loan.username}</strong></td>
                <td>${loan.bookTitle}</td>
                <td>${formatDate(loan.loanDate)}</td>
                <td>${formatDate(loan.dueDate)}</td>
                <td>${loan.returnDate ? formatDate(loan.returnDate) : '-'}</td>
                <td>${statusBadge}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

/**
 * Handle book type change
 */
function handleBookTypeChange() {
    const bookType = document.getElementById('bookType').value;

    // Hide all type-specific fields
    document.getElementById('digitalFields').style.display = 'none';
    document.getElementById('audioFields').style.display = 'none';
    document.getElementById('ebookFields').style.display = 'none';

    // Show relevant fields
    if (bookType === 'DIGITAL') {
        document.getElementById('digitalFields').style.display = 'block';
    } else if (bookType === 'AUDIO') {
        document.getElementById('audioFields').style.display = 'block';
    } else if (bookType === 'EBOOK') {
        document.getElementById('ebookFields').style.display = 'block';
    }
}

/**
 * Handle add book form submission
 */
async function handleAddBook(e) {
    e.preventDefault();

    const formMessage = document.getElementById('formMessage');
    formMessage.className = 'form-message';
    formMessage.style.display = 'none';

    // Collect form data
    const bookData = {
        bookType: document.getElementById('bookType').value,
        title: document.getElementById('title').value,
        author: document.getElementById('author').value,
        isbn: document.getElementById('isbn').value,
        category: document.getElementById('category').value
    };

    // Add type-specific fields
    if (bookData.bookType === 'DIGITAL') {
        bookData.fileFormat = document.getElementById('fileFormat').value;
        bookData.fileSizeMB = parseFloat(document.getElementById('fileSizeMB').value);
    } else if (bookData.bookType === 'AUDIO') {
        bookData.narrator = document.getElementById('narrator').value;
        bookData.durationMinutes = parseInt(document.getElementById('durationMinutes').value);
        bookData.audioFormat = document.getElementById('audioFormat').value;
    } else if (bookData.bookType === 'EBOOK') {
        bookData.publisher = document.getElementById('publisher').value;
        bookData.pageCount = parseInt(document.getElementById('pageCount').value);
        bookData.hasInteractiveContent = document.getElementById('hasInteractiveContent').checked;
    }

    try {
        const data = await API.post('/books', bookData);

        if (data.success) {
            formMessage.className = 'form-message success';
            formMessage.textContent = '✓ Book created successfully using Factory Method pattern!';
            formMessage.style.display = 'block';

            // Reset form
            document.getElementById('addBookForm').reset();
            handleBookTypeChange();

            // Reload books if on catalog tab
            if (document.querySelector('[data-tab="catalog"]').classList.contains('active')) {
                loadBooks();
            }
        } else {
            formMessage.className = 'form-message error';
            formMessage.textContent = '✗ ' + (data.message || 'Failed to create book');
            formMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('Error creating book:', error);
        formMessage.className = 'form-message error';
        formMessage.textContent = '✗ Error creating book: ' + error.message;
        formMessage.style.display = 'block';
    }
}

/**
 * Load statistics
 */
async function loadStatistics() {
    const container = document.getElementById('statisticsDisplay');
    container.innerHTML = '<div class="loading">Loading statistics...</div>';

    try {
        // Get books count
        const booksData = await API.get('/books');
        const booksCount = booksData.books ? booksData.books.length : 0;

        // Get loans data
        const loansData = await API.get('/loans');
        const loans = loansData.loans || [];
        const totalLoans = loans.length;
        const activeLoans = loans.filter(loan => !loan.returned).length;
        const overdueLoans = loans.filter(loan => loan.overdue && !loan.returned).length;

        // Display statistics
        container.innerHTML = `
            <div class="stat-card">
                <h3>${booksCount}</h3>
                <p>Books in Catalog</p>
            </div>
            <div class="stat-card">
                <h3>${totalLoans}</h3>
                <p>Total Loans</p>
            </div>
            <div class="stat-card">
                <h3>${activeLoans}</h3>
                <p>Active Loans</p>
            </div>
            <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                <h3>${overdueLoans}</h3>
                <p>Overdue Loans</p>
            </div>
        `;
    } catch (error) {
        console.error('Error loading statistics:', error);
        container.innerHTML = '<div class="empty-state"><h3>Error loading statistics</h3></div>';
    }
}
