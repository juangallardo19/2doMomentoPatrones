/**
 * Authentication Handler
 * Manages login functionality
 */

// Redirect if already authenticated
if (isAuthenticated()) {
    const user = getCurrentUser();
    if (user.role === 'ADMIN') {
        window.location.href = '/admin.html';
    } else {
        window.location.href = '/dashboard.html';
    }
}

// Login Form Handler
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

    // Hide previous error
    errorMessage.style.display = 'none';

    try {
        // Call login API
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (data.success) {
            // Store token and user info
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(data.user));

            // Redirect based on role
            if (data.user.role === 'ADMIN') {
                window.location.href = '/admin.html';
            } else {
                window.location.href = '/dashboard.html';
            }
        } else {
            // Show error message
            errorMessage.textContent = data.message || 'Login failed';
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('Login error:', error);
        errorMessage.textContent = 'An error occurred. Please try again.';
        errorMessage.style.display = 'block';
    }
});
