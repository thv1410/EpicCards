document.addEventListener("DOMContentLoaded", async () => {
  const userList = document.getElementById("userList");
  const searchInput = document.getElementById("userSearch");
  const logoutLink = document.getElementById("logoutLink");

  // Show logout button if user is logged in
  if (localStorage.getItem("username")) {
    logoutLink.style.display = "inline";
  }
  
  // Logout behavior
  logoutLink?.addEventListener("click", () => {
    localStorage.removeItem("username");
    localStorage.removeItem("userID");
    window.location.href = "login";
  });

  async function fetchUsers() {
    const res = await fetch("http://localhost:8080/users");
    const users = await res.json();
    return users;
  }

  function renderUsers(users) {
    userList.innerHTML = "";
    users.forEach(user => {
      const li = document.createElement("li");
      li.textContent = user.username;
      li.addEventListener("click", () => {
        window.location.href = `category?userID=${user.userID}`;
      });
      userList.appendChild(li);
    });
  }

  const allUsers = await fetchUsers();
  renderUsers(allUsers);

  searchInput.addEventListener("input", () => {
    const filtered = allUsers.filter(u =>
      u.username.toLowerCase().includes(searchInput.value.toLowerCase())
    );
    renderUsers(filtered);
  });
});
