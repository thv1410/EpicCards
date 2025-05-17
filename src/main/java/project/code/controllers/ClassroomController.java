package project.code.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import project.code.services.ClassroomService;
import project.code.models.Classroom;
import project.code.models.Enrolled;
import java.util.List;
import project.code.models.User;

@RestController
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    // Redirect to the classroom page
    @GetMapping("/classroom")
    public String classroomPage() {
        return "classroom";
    }

    // Redirect to the student page
    @GetMapping("/student")
    public String studentPage() {
        return "student";
    }

    // Handle classroom creation
    @PostMapping("/createClassroom")
    public String createClassroom(@Valid @RequestBody Classroom classroom) {
        String name = classroom.getName();
        int teacherID = classroom.getTeacherID();
        int result = classroomService.insertClassroom(name, teacherID);
        if (result > 0) {
            System.out.println("Classroom " + name + " created successfully with ID: " + result);
            return "Classroom created successfully!";
        } else if (result == 0) {
            System.out.println("Classroom name " + name + " already exists.");
            return "Classroom name already exists!";
        } else {
            System.out.println("Failed to create classroom " + name + ".");
            return "Failed to create classroom.";
        }
    }

    // Handle classroom deletion
    @PostMapping("/removeClassroom")
    public String removeClassroom(@RequestParam int classroomID) {
        boolean result = classroomService.deleteClassroom(classroomID);
        if (result) {
            System.out.println("Classroom with ID " + classroomID + " deleted successfully.");
            return "Classroom deleted successfully!";
        } else {
            System.out.println("Failed to delete classroom with ID " + classroomID + ".");
            return "Failed to delete classroom.";
        }
    }

    // Handle student joining a classroom
    @PostMapping("/joinClassroom")
    public String joinClassroom(@RequestBody Enrolled enrolled) {
        int classroomID = enrolled.getClassroomID();
        int studentID = enrolled.getUserID();
        int result = classroomService.joinClassroom(classroomID, studentID);
        if (result > 0) {
            System.out.println("Student with ID " + studentID + " joined classroom with ID " + classroomID);
            return "Student joined classroom successfully!";
        } else if (result == 0) {
            System.out.println("Student with ID " + studentID + " is already in classroom with ID " + classroomID);
            return "Student is already in the classroom!";
        } else {
            System.out.println("Failed to add student with ID " + studentID + " to classroom with ID " + classroomID);
            return "Failed to join classroom.";
        }
    }

    // Handle student leaving a classroom
    @PostMapping("/leaveClassroom")
    public String leaveClassroom(@RequestBody Enrolled enrolled) {
        int classroomID = enrolled.getClassroomID();
        int studentID = enrolled.getUserID();
        boolean result = classroomService.leaveClassroom(classroomID, studentID);
        if (result) {
            System.out.println("Student with ID " + studentID + " left classroom with ID " + classroomID);
            return "Student left classroom successfully!";
        } else {
            System.out.println(
                    "Failed to remove student with ID " + studentID + " from classroom with ID " + classroomID);
            return "Failed to leave classroom.";
        }
    }

    // Handle classroom retrieval
    @GetMapping("/getClassroom")
    public Classroom getClassroom(@RequestParam int classroomID) {
        System.out.println("Fetching classroom with ID: " + classroomID);
        Classroom classroom = classroomService.getClassroom(classroomID);
        return classroom;
    }

    // Get all students in a classroom
    @GetMapping("/getAllStudentsInClassroom")
    public List<User> getAllStudentsInClassroom(@RequestParam int classroomID) {
        System.out.println("Fetching all students in classroom with ID: " + classroomID);
        List<User> students = classroomService.getAllStudentsInClassroom(classroomID);
        return students;
    }

    // Get all classrooms for a user
    @GetMapping("/getAllClassroomsForUser")
    public List<Classroom> getAllClassroomsForUser(@RequestParam int userID) {
        System.out.println("Fetching all classrooms for user with ID: " + userID);
        List<Classroom> classrooms = classroomService.getAllClassroomsForUser(userID);
        return classrooms;
    }

    // Get all classrooms
    @GetMapping("/getAllClassrooms")
    public List<Classroom> getAllClassrooms(@RequestParam int userID) {
        System.out.println("Fetching all classrooms");
        List<Classroom> classrooms = classroomService.getAllClassrooms(userID);
        return classrooms;
    }
}
