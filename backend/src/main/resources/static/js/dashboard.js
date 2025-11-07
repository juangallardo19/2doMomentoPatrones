/**
 * User Dashboard
 * Manages user interface and book operations
 */

// Check authentication
if (!isAuthenticated()) {
    window.location.href = '/index.html';
}

const user = getCurrentUser();

// Initialize dashboard
document.addEventListener('DOMContentLoaded', () => {
    initDashboard();
    setupEventListeners();
    loadBooks();
});

/**
 * Initialize dashboard
 */
function initDashboard() {
    document.getElementById('userInfo').textContent = `Welcome, ${user.fullName}!`;
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

    // Category filter
    document.getElementById('categoryFilter').addEventListener('change', filterByCategory);

    // Modal close
    document.querySelector('.close').addEventListener('click', closeModal);
    window.addEventListener('click', (e) => {
        if (e.target.id === 'bookModal') closeModal();
    });
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
    if (tabName === 'myLoans') {
        loadMyLoans();
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
        <p class="status ${statusClass}">${statusText}</p>
        <div class="actions">
            <button class="btn btn-primary btn-small" onclick="viewBookDetails(${book.id})">
                View Details
            </button>
            ${book.available && user.role !== 'GUEST' ?
                `<button class="btn btn-success btn-small" onclick="borrowBook(${book.id})">
                    Borrow
                </button>` : ''
            }
        </div>
    `;

    return card;
}

/**
 * View book details
 */
async function viewBookDetails(bookId) {
    try {
        const data = await API.get(`/books/${bookId}`);

        if (data.success) {
            showBookModal(data.book);
        }
    } catch (error) {
        console.error('Error loading book details:', error);
        alert('Error loading book details');
    }
}

/**
 * Show book modal
 */
function showBookModal(book) {
    const modal = document.getElementById('bookModal');
    const details = document.getElementById('bookDetails');

    const statusClass = book.available ? 'available' : 'unavailable';
    const statusText = book.available ? '✓ Available' : '✗ Not Available';

    details.innerHTML = `
        <div style="text-align: center; font-size: 4rem; margin-bottom: 20px;">
            ${getBookIcon(book.bookType)}
        </div>
        <h2>${book.title}</h2>
        <p><strong>Author:</strong> ${book.author}</p>
        <p><strong>ISBN:</strong> ${book.isbn}</p>
        <p><strong>Category:</strong> ${book.category}</p>
        <p><strong>Type:</strong> <span class="book-type ${book.bookType}">${book.bookType}</span></p>
        <p><strong>Status:</strong> <span class="status ${statusClass}">${statusText}</span></p>
        <p><strong>Access Method:</strong> ${book.accessMethod}</p>
        <hr style="margin: 20px 0;">
        ${book.available && user.role !== 'GUEST' ?
            `<button class="btn btn-success" onclick="borrowBook(${book.id}); closeModal();">
                Borrow This Book
            </button>` : ''
        }
    `;

    modal.classList.add('active');
}

/**
 * Close modal
 */
function closeModal() {
    document.getElementById('bookModal').classList.remove('active');
}

/**
 * Borrow book
 */
async function borrowBook(bookId) {
    if (user.role === 'GUEST') {
        alert('Guests cannot borrow books. Please login as a regular user.');
        return;
    }

    try {
        const data = await API.post('/loans/borrow', { bookId });

        if (data.success) {
            alert('Book borrowed successfully!');
            loadBooks(); // Reload books to update availability
        } else {
            alert(data.message || 'Failed to borrow book');
        }
    } catch (error) {
        console.error('Error borrowing book:', error);
        alert('Error borrowing book');
    }
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
            grid.innerHTML = '<div class="empty-state"><h3>No books found</h3><p>Try a different search term</p></div>';
        }
    } catch (error) {
        console.error('Error searching books:', error);
        grid.innerHTML = '<div class="empty-state"><h3>Error searching books</h3></div>';
    }
}

/**
 * Filter by category
 */
async function filterByCategory() {
    const category = document.getElementById('categoryFilter').value;

    if (!category) {
        loadBooks();
        return;
    }

    const grid = document.getElementById('booksGrid');
    grid.innerHTML = '<div class="loading">Loading...</div>';

    try {
        const data = await API.get(`/books/category/${encodeURIComponent(category)}`);

        if (data.success && data.books.length > 0) {
            displayBooks(data.books);
        } else {
            grid.innerHTML = '<div class="empty-state"><h3>No books in this category</h3></div>';
        }
    } catch (error) {
        console.error('Error filtering books:', error);
        grid.innerHTML = '<div class="empty-state"><h3>Error filtering books</h3></div>';
    }
}

/**
 * Load user's loans
 */
async function loadMyLoans() {
    const container = document.getElementById('loansTable');
    container.innerHTML = '<div class="loading">Loading your loans...</div>';

    try {
        const data = await API.get('/loans/history');

        if (data.success && data.loans.length > 0) {
            displayLoans(data.loans);
        } else {
            container.innerHTML = '<div class="empty-state"><h3>No loan history</h3><p>You haven\'t borrowed any books yet</p></div>';
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
                    <th>Book</th>
                    <th>Loan Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
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

        const returnButton = !loan.returned
            ? `<button class="btn btn-primary btn-small" onclick="returnBook(${loan.id})">Return</button>`
            : '';

        html += `
            <tr>
                <td><strong>${loan.bookTitle}</strong></td>
                <td>${formatDate(loan.loanDate)}</td>
                <td>${formatDate(loan.dueDate)}</td>
                <td>${statusBadge}</td>
                <td>${returnButton}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

/**
 * Return book
 */
async function returnBook(loanId) {
    if (!confirm('Are you sure you want to return this book?')) {
        return;
    }

    try {
        const data = await API.post(`/loans/return/${loanId}`, {});

        if (data.success) {
            alert('Book returned successfully!');
            loadMyLoans(); // Reload loans
            if (document.querySelector('[data-tab="catalog"]').classList.contains('active')) {
                loadBooks(); // Reload books if on catalog tab
            }
        } else {
            alert(data.message || 'Failed to return book');
        }
    } catch (error) {
        console.error('Error returning book:', error);
        alert('Error returning book');
    }
}
