document.addEventListener("DOMContentLoaded", async () => {
  const categoryID = new URLSearchParams(window.location.search).get(
    "categoryID"
  );
  const container = document.getElementById("flashcard");
  const front = container.querySelector(".card-front");
  const back = container.querySelector(".card-back");
  const deleteBtn = document.getElementById("delete");
  const createBtn = document.getElementById("createFlashcardBtn");
  const termInput = document.getElementById("newSetTerm");
  const definitionInput = document.getElementById("newSetDefinition");
  let flashcards = [];
  let index = 0;
  const logoutLink = document.getElementById("logoutLink");

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

  async function fetchFlashcards() {
    const res = await fetch(
      `http://localhost:8080/getAllFlashcardsFromCategory?categoryID=${categoryID}`
    );
    return await res.json();
  }

  function renderCard(card) {
    front.textContent = card.term;
    back.textContent = card.definition;
  }

  document.getElementById("next").addEventListener("click", () => {
    index = (index + 1) % flashcards.length;
    renderCard(flashcards[index]);
  });

  document.getElementById("prev").addEventListener("click", () => {
    index = (index - 1 + flashcards.length) % flashcards.length;
    renderCard(flashcards[index]);
  });

  flashcards = await fetchFlashcards();
  if (flashcards.length > 0) renderCard(flashcards[0]);

  createBtn.addEventListener("click", async () => {
    const term = termInput.value.trim();
    const definition = definitionInput.value.trim();
    if (!term) return alert("Enter a title.");
    if (!definition) return alert("Enter a definition.");
    await fetch("http://localhost:8080/createFlashcard", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ term, definition, categoryID }),
    });
    termInput.value = "";
    definitionInput.value = "";
    flashcards = await fetchFlashcards();
    renderCard(flashcards[index]);
  });

  deleteBtn.addEventListener("click", async () => {
    if (flashcards.length === 0) return;
    const currentCard = flashcards[index];
    await fetch("http://localhost:8080/deleteFlashcard", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `flashcardID=${encodeURIComponent(currentCard.flashcardID)}`,
    });
    flashcards = await fetchFlashcards();
    if (flashcards.length === 0) {
      front.textContent = "No Cards";
      back.textContent = "";
      return;
    }
    index = Math.max(index - 1, 0);
    renderCard(flashcards[index]);
  });
});
