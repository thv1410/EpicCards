package project.code.models;

import jakarta.validation.constraints.NotBlank;

public class Classroom {
    private int classroomID;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private int teacherID;

    public Classroom() {
    }

    public Classroom(int classroomID, String name, int teacherID) {
        this.classroomID = classroomID;
        this.name = name;
        this.teacherID = teacherID;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(int classroomID) {
        this.classroomID = classroomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }
}
