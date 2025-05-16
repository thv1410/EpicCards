document.addEventListener("DOMContentLoaded", () => {
  const registerForm = document.getElementById("registerForm");

  registerForm?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("regUsername").value.trim();
    const password = document.getElementById("regPassword").value.trim();

    try {
      const response = await fetch("/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
      });

      const text = await response.text();
      alert(text);

      if (text.includes("successfully")) {
        window.location.href = "/login";
      }

    } catch (error) {
      console.error("Error connecting to backend:", error);
      alert("Could not reach the server. Please try again later.");
    }
  });
});
