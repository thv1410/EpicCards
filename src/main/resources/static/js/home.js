document.addEventListener("DOMContentLoaded", () => {
  const logoutLink = document.getElementById("logoutLink");

  fetch("/user-info")
    .then(res => res.json())
    .then(data => {
      localStorage.setItem("username", data.username);
      localStorage.setItem("userID", data.userID);

      document.getElementById("usernameSpan").textContent = data.username;
      fetchStatistics();
    })
    .catch(err => console.error("Failed to load user info", err));

  logoutLink?.addEventListener("click", () => {
    localStorage.removeItem("username");
    localStorage.removeItem("userID");
    window.location.href = "login";
  });

  async function fetchStatistics() {
    const userID = localStorage.getItem("userID");
    if (!userID) return;

    try {
      const res = await fetch(`/getUserStatistics?userID=${userID}`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();

      if (Array.isArray(data) && data.length === 4) {
        document.getElementById("categoriesCreated").textContent = `Number of categories created: ${data[0]}`;
        document.getElementById("flashcardsCreated").textContent = `Number of flashcards created: ${data[1]}`;
        document.getElementById("classroomsCreated").textContent = `Number of classrooms created: ${data[2]}`;
        document.getElementById("classroomsJoined").textContent = `Number of classrooms joined: ${data[3]}`;
      }
    } catch (err) {
      console.error("Failed to load statistics:", err);
    }
  }
});
