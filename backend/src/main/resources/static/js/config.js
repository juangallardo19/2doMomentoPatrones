/**
 * API Configuration
 * Base URL for backend API
 */
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * API Helper Functions
 */
const API = {
    /**
     * Make authenticated API request
     */
    async request(endpoint, options = {}) {
        const token = localStorage.getItem('token');

        const config = {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : '',
                ...options.headers
            }
        };

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Request failed');
            }

            return data;
        } catch (error) {
            console.error('API Request Error:', error);
            throw error;
        }
    },

    /**
     * GET request
     */
    async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },

    /**
     * POST request
     */
    async post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    /**
     * PUT request
     */
    async put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    /**
     * DELETE request
     */
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
};

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return !!localStorage.getItem('token');
}

/**
 * Get current user from localStorage
 */
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

/**
 * Logout function
 */
async function logout() {
    try {
        await API.post('/auth/logout', {});
    } catch (error) {
        console.error('Logout error:', error);
    } finally {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/index.html';
    }
}

/**
 * Format date
 */
function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

/**
 * Get book icon by type
 */
function getBookIcon(bookType) {
    const icons = {
        'DIGITAL': '📱',
        'AUDIO': '🎧',
        'EBOOK': '📚'
    };
    return icons[bookType] || '📖';
}
