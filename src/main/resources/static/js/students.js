document.addEventListener("DOMContentLoaded", async () => {
    const classroomID = new URLSearchParams(window.location.search).get("classroomID");
    const studentList = document.getElementById("studentList");
    const classroomTitle = document.getElementById("classroomTitle");
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
  
    // Fetch the classroom info to get its name
    async function fetchClassroom() {
      const res = await fetch(`http://localhost:8080/getClassroom?classroomID=${classroomID}`);
      return await res.json();
    }
  
    // Fetch enrolled students
    async function fetchStudents() {
      const res = await fetch(`http://localhost:8080/getAllStudentsInClassroom?classroomID=${classroomID}`);
      return await res.json();
    }
  
    // Set classroom title
    const classroom = await fetchClassroom();
    classroomTitle.textContent = `Classroom: ${classroom.name}`;
  
    // Populate students
    const students = await fetchStudents();
    students.forEach(s => {
      const li = document.createElement("li");

      const link = document.createElement("a");
      link.href = `sets.html?userID=${s.userID}`;
      link.textContent = s.username;
    
      li.appendChild(link);
      studentList.appendChild(li);
      });
      
  });
  