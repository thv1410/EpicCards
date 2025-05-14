document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm");
  const logoutLink = document.getElementById("logoutLink");
  const loginLink = document.getElementById("loginLink");
  const registerLink = document.getElementById("registerLink");

  const username = localStorage.getItem("username");

  if (username) {
    if (logoutLink) logoutLink.style.display = "inline";
    if (loginLink) loginLink.style.display = "none";
    if (registerLink) registerLink.style.display = "none";
  }

  logoutLink?.addEventListener("click", () => {
    localStorage.removeItem("username");
    localStorage.removeItem("userID");
    window.location.href = "login.html";
  });

  loginForm?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const usernameInput = document.getElementById("username").value.trim();
    const passwordInput = document.getElementById("password").value.trim();

    try {
      const res = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: usernameInput, password: passwordInput }),
      });

      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();

      if (data.message === "success") {
        localStorage.setItem("username", data.username);
        localStorage.setItem("userID", data.userID);
        window.location.href = "index.html";
      } else {
        alert("Login failed. Check credentials.");
      }
    } catch (err) {
      console.error("Login error:", err);
      alert("Server error. Please try again later.");
    }
  });
});
