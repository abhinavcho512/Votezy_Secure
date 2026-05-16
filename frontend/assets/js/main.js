document.addEventListener("DOMContentLoaded", () => {
    setupNavbar();
    checkAuth();
});

// Highlight active navbar link
function setupNavbar() {
    let currentPage = window.location.pathname.split("/").pop();
    let navLinks = document.querySelectorAll(".nav-link");

    navLinks.forEach((link) => {
        if (link.getAttribute("href") === currentPage) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
}

// ✅ Check if user is logged in
function checkAuth() {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    const currentPage = window.location.pathname.split("/").pop();

    // Pages that don't need login
    const publicPages = ["index.html", "login.html", "register.html", ""];

    if (!token && !publicPages.includes(currentPage)) {
        window.location.href = "login.html";
        return;
    }

    // ✅ ADMIN only pages - redirect USER away
    const adminPages = ["voters.html", "candidates.html", "results.html"];
    if (token && role === "ROLE_USER" && adminPages.includes(currentPage)) {
        Swal.fire("Access Denied", "This page is for admins only!", "error")
        .then(() => {
            window.location.href = "voting.html";
        });
        return;
    }

    // ✅ If logged in, show logout button in navbar
    if (token) {
        addLogoutButton();
    }
}

// ✅ Add logout button to navbar
function addLogoutButton() {
    const navList = document.querySelector(".navbar-nav");
    if (!navList) return;

    const role = localStorage.getItem("role");
    const email = localStorage.getItem("email");

    navList.innerHTML += `
        <li class="nav-item">
            <span class="nav-link text-white">${email} (${role === "ROLE_ADMIN" ? "Admin" : "User"})</span>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white fw-bold" href="#" onclick="logout()">Logout</a>
        </li>
    `;
}

// ✅ Logout function
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("email");
    window.location.href = "login.html";
}

// ✅ Get token from localStorage
function getToken() {
    return localStorage.getItem("token");
}

// ✅ Get auth headers
function getAuthHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}

// Generic API function for GET requests
function fetchData(url, callback) {
    fetch(url, {
        headers: getAuthHeaders()
    })
    .then((res) => res.json())
    .then((data) => callback(data))
    .catch((error) => console.error("Error fetching data:", error));
}

// Function to show a SweetAlert notification
function showAlert(title, message, type = "info") {
    Swal.fire({
        title: title,
        text: message,
        icon: type,
        confirmButtonColor: "#d9534f",
    });
}

// Show toast notification
function showToast(message, type = "info") {
    Swal.fire({
        toast: true,
        position: "top-end",
        icon: type,
        title: message,
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
    });
}