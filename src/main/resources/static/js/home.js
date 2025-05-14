document.addEventListener("DOMContentLoaded", () => {
    const logoutLink = document.getElementById("logoutLink");
    const welcomeMsg = document.getElementById("welcomeMsg");
  
    const username = localStorage.getItem("username");
  
    if (username) {
      if (logoutLink) logoutLink.style.display = "inline";
      if (welcomeMsg) welcomeMsg.textContent = `Welcome to FlashcardsWeb, ${username}`;
      fetchStatistics();
    }
  
    logoutLink?.addEventListener("click", () => {
      localStorage.removeItem("username");
      localStorage.removeItem("userID");
      window.location.href = "login.html";
    });
  
    async function fetchStatistics() {
      const userID = localStorage.getItem("userID");
      if (!userID) return;
  
      try {
        const res = await fetch(`http://localhost:8080/getUserStatistics?userID=${userID}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
  
        if (Array.isArray(data) && data.length === 4) {
          document.getElementById("flashcardsCreated").textContent = `Number of categories created: ${data[0]}`;
          document.getElementById("categoriesCreated").textContent = `Number of flashcards created: ${data[1]}`;
          document.getElementById("classroomsCreated").textContent = `Number of classrooms created: ${data[2]}`;
          document.getElementById("classroomsJoined").textContent = `Number of classrooms joined: ${data[3]}`;
        }
      } catch (err) {
        console.error("Failed to load statistics:", err);
      }
    }
  });
  