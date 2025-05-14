package project.code.models;

// This class represents the enrollment of a student in a classroom.
public class Enrolled {
    private int classroomID;
    private int userID;

    public Enrolled(int classroomID, int userID) {
        this.classroomID = classroomID;
        this.userID = userID;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public int getUserID() {
        return userID;
    }
}
