document.addEventListener("DOMContentLoaded", async () => {
  const userID = localStorage.getItem("userID");
  const myDiv = document.getElementById("myClassrooms");
  const classroomList = document.getElementById("classroomList");
  const searchInput = document.getElementById("classroomSearch");
  const logoutLink = document.getElementById("logoutLink");
  const createBtn = document.getElementById("createClassroomBtn");
  const newClassroomInput = document.getElementById("newClassroomName");

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

  async function fetchMyClassrooms() {
    const res = await fetch(`http://localhost:8080/getAllClassroomsForUser?userID=${userID}`);
    return await res.json();
  }

  async function fetchAllClassrooms() {
    const res = await fetch(`http://localhost:8080/getAllClassrooms?userID=${userID}`);
    return await res.json();
  }

  function renderClassrooms(container, classrooms, isMyClassroom = false) {
    container.innerHTML = "";
    classrooms.forEach(c => {
      const li = document.createElement("li");
  
      // Classroom name as a link
      const link = document.createElement("a");
      link.href = `students.html?classroomID=${c.classroomID}`;
      link.textContent = c.name;
      li.appendChild(link);
  
      // Decide which button to show
      const btn = document.createElement("button");
  
      const isTeacher = parseInt(userID) === c.teacherID;
  
      if (isMyClassroom && isTeacher) {
        // Show "Remove" button if the user is the teacher
        btn.textContent = "Remove";
        btn.onclick = async () => {
          const confirmed = confirm(`Are you sure you want to delete "${c.name}"?`);
          if (!confirmed) return;
  
          const res = await fetch(`http://localhost:8080/removeClassroom?classroomID=${c.classroomID}`, {
            method: "POST"
          });
  
          const msg = await res.text();
          alert(msg);
          location.reload();
        };
      } else {
        // Show "Leave" or "Join"
        btn.textContent = isMyClassroom ? "Leave" : "Join";
        btn.onclick = async () => {
          const endpoint = isMyClassroom ? "leaveClassroom" : "joinClassroom";
          await fetch(`http://localhost:8080/${endpoint}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ classroomID: c.classroomID, userID: parseInt(userID) })
          });
          location.reload();
        };
      }
  
      li.appendChild(btn);
      container.appendChild(li);
    });
  }
  
  

  const my = await fetchMyClassrooms();
  const all = await fetchAllClassrooms();

  renderClassrooms(myDiv, my, true);
  renderClassrooms(classroomList, all);

  searchInput.addEventListener("input", () => {
    const filtered = all.filter(c =>
      c.name.toLowerCase().includes(searchInput.value.toLowerCase())
    );
    renderClassrooms(classroomList, filtered);
  });

  // Create classroom
  createBtn.addEventListener("click", async () => {
    const name = newClassroomInput.value.trim();
    if (!name) return alert("Please enter a classroom name.");

    const res = await fetch("http://localhost:8080/createClassroom", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, teacherID: parseInt(userID) })
    });

    const msg = await res.text();
    alert(msg);
    newClassroomInput.value = "";
    location.reload();
  });
});
