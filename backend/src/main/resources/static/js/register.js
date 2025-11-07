/**
 * Registration Handler
 * Manages user registration functionality
 */

// Check if already authenticated
const token = localStorage.getItem('token');
if (token) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.role === 'ADMIN') {
        window.location.href = '/admin.html';
    } else if (user) {
        window.location.href = '/dashboard.html';
    }
}

// Registration Form Handler
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const fullName = document.getElementById('fullName').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    const errorMessage = document.getElementById('errorMessage');
    const successMessage = document.getElementById('successMessage');

    // Hide previous messages
    errorMessage.style.display = 'none';
    successMessage.style.display = 'none';

    // Validate passwords match
    if (password !== confirmPassword) {
        errorMessage.textContent = 'Passwords do not match';
        errorMessage.style.display = 'block';
        return;
    }

    // Validate password length
    if (password.length < 6) {
        errorMessage.textContent = 'Password must be at least 6 characters';
        errorMessage.style.display = 'block';
        return;
    }

    // Validate username format
    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
    if (!usernameRegex.test(username)) {
        errorMessage.textContent = 'Username must be 3-20 characters (letters, numbers, underscores only)';
        errorMessage.style.display = 'block';
        return;
    }

    // Show loading state
    const submitButton = e.target.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<span>Creating Account...</span><span class="btn-icon">⏳</span>';
    submitButton.disabled = true;

    try {
        // Call registration API
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username,
                password,
                email,
                fullName
            })
        });

        const data = await response.json();

        if (data.success) {
            // Show success message
            successMessage.textContent = 'Account created successfully! Redirecting to login...';
            successMessage.style.display = 'block';

            // Clear form
            e.target.reset();

            // Redirect to login after 2 seconds
            setTimeout(() => {
                window.location.href = '/index.html';
            }, 2000);
        } else {
            // Show error message
            errorMessage.textContent = data.message || 'Registration failed';
            errorMessage.style.display = 'block';
            submitButton.innerHTML = originalText;
            submitButton.disabled = false;
        }
    } catch (error) {
        console.error('Registration error:', error);
        errorMessage.textContent = 'An error occurred. Please try again.';
        errorMessage.style.display = 'block';
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
    }
});

// Add real-time password match validation
document.getElementById('confirmPassword').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const confirmPassword = this.value;

    if (confirmPassword && password !== confirmPassword) {
        this.setCustomValidity('Passwords do not match');
    } else {
        this.setCustomValidity('');
    }
});
