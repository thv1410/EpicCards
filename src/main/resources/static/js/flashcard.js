document.addEventListener("DOMContentLoaded", async () => {
  const URLUserID = new URLSearchParams(window.location.search).get("userID");
  const otherUser = URLUserID != null || URLUserID == localStorage.getItem("userID");
  const userID = otherUser ? URLUserID : localStorage.getItem("userID");
  const setList = document.getElementById("setList");
  const searchInput = document.getElementById("setSearch");
  const createBtn = document.getElementById("createSetBtn");
  const titleInput = document.getElementById("newSetTitle");
  const logoutLink = document.getElementById("logoutLink");

  // Hides ability to create sets when viewing another user
  if (otherUser) {
    document.querySelector("h3").style.display = "none";
    document.getElementById("newSetTitle").style.display = "none";
    document.getElementById("createSetBtn").style.display = "none";
  }

  // Show logout button if user is logged in
  if (localStorage.getItem("username")) {
    logoutLink.style.display = "inline";
  }

  // Logout behavior
  logoutLink?.addEventListener("click", () => {
    localStorage.removeItem("username");
    localStorage.removeItem("userID");
    window.location.href = "login.html";
  });


  async function fetchCategories() {
    const res = await fetch(`http://localhost:8080/getAllCategoriesFromUser?userID=${userID}`);
    return await res.json();
  }

  function renderSets(categories) {
    setList.innerHTML = "";
    categories.forEach(cat => {
      const li = document.createElement("li");
      li.textContent = cat.title;

      const viewBtn = document.createElement("button");
      viewBtn.textContent = "Study";
      viewBtn.onclick = () => window.location.href = `study.html?categoryID=${cat.categoryID}`;

      const delBtn = document.createElement("button");
      delBtn.textContent = "Delete";
      delBtn.onclick = async () => {
        await fetch(`http://localhost:8080/deleteCategory?categoryID=${cat.categoryID}`, {
          method: "POST"
        });
        loadCategories();
      };

      li.appendChild(viewBtn);
      if (!otherUser) {
        li.appendChild(delBtn);
      }
      setList.appendChild(li);
    });
  }

  async function loadCategories() {
    const categories = await fetchCategories();
    renderSets(categories);

    searchInput.addEventListener("input", () => {
      const filtered = categories.filter(c =>
        c.title.toLowerCase().includes(searchInput.value.toLowerCase())
      );
      renderSets(filtered);
    });
  }

  createBtn.addEventListener("click", async () => {
    const title = titleInput.value.trim();
    if (!title) return alert("Enter a title.");
    await fetch("http://localhost:8080/createCategory", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, userID })
    });
    titleInput.value = "";
    loadCategories();
  });

  loadCategories();
});
