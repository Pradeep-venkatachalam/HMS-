// ===== TOAST =====
function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span class="toast-icon">${icons[type] || icons.info}</span><span class="toast-msg">${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.animation = 'fadeOut 0.3s ease forwards';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// ===== MODAL =====
function openModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.add('active');
}
function closeModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.remove('active');
}
function closeAllModals() {
    document.querySelectorAll('.modal-overlay').forEach(m => m.classList.remove('active'));
}

// ===== LOADING =====
function showLoading() {
    let el = document.getElementById('global-loading');
    if (!el) {
        el = document.createElement('div');
        el.id = 'global-loading';
        el.className = 'loading-overlay';
        el.innerHTML = '<div class="spinner"></div>';
        document.body.appendChild(el);
    }
    el.style.display = 'flex';
}
function hideLoading() {
    const el = document.getElementById('global-loading');
    if (el) el.style.display = 'none';
}

// ===== AUTH GUARD =====
function requireAuth() {
    if (!api.isLoggedIn()) {
        window.location.href = '../../../../../../frontend/index.html';
        return false;
    }
    return true;
}

function requireRole(...roles) {
    if (!requireAuth()) return false;
    if (!api.hasRole(...roles)) {
        showToast('Access denied for your role', 'error');
        return false;
    }
    return true;
}

// ===== SIDEBAR =====
function initSidebar() {
    const user = api.getUser();
    if (!user) return;

    const nameEl = document.getElementById('sidebar-user-name');
    const roleEl = document.getElementById('sidebar-user-role');
    const avatarEl = document.getElementById('sidebar-user-avatar');
    if (nameEl) nameEl.textContent = user.fullName || user.username;
    if (roleEl) roleEl.textContent = user.role;
    if (avatarEl) avatarEl.textContent = (user.fullName || user.username).charAt(0).toUpperCase();

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) logoutBtn.addEventListener('click', () => api.logout());

    // Set active nav item
    const currentPage = window.location.pathname.split('/').pop().replace('.html', '');
    document.querySelectorAll('.nav-item[data-page]').forEach(item => {
        if (item.dataset.page === currentPage) item.classList.add('active');
        item.addEventListener('click', () => {
            window.location.href = item.dataset.page + '.html';
        });
    });

    // Role-based nav visibility
    document.querySelectorAll('[data-roles]').forEach(el => {
        const roles = el.dataset.roles.split(',');
        if (!api.hasRole(...roles)) el.style.display = 'none';
    });
}

// ===== FORMAT HELPERS =====
function formatDate(dateStr) {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}
function formatDateTime(dtStr) {
    if (!dtStr) return '-';
    return new Date(dtStr).toLocaleString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}
function formatCurrency(amount) {
    if (amount == null) return '₹0.00';
    return '₹' + Number(amount).toFixed(2);
}
function badgeHtml(status) {
    if (!status) return '';
    return `<span class="badge badge-${status.toLowerCase().replace(' ', '_')}">${status}</span>`;
}
function escHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

// Close modal on overlay click
document.addEventListener('click', e => {
    if (e.target.classList.contains('modal-overlay')) closeAllModals();
});
