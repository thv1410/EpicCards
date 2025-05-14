package project.code.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.code.models.Classroom;
import java.util.List;
import project.code.models.User;

@Repository
public class ClassroomService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // insert new classroom
    public int insertClassroom(String name, int teacherID) {
        try {
            // SQL query to check if the classroom name already exists
            String checkSql = "SELECT COUNT(*) FROM classroom WHERE name = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, name);
            if (count != null && count > 0) {
                // classroom name already exists
                return 0;
            }
            // SQL query to insert classroom into the database
            String sql = "INSERT INTO classroom (name, teacherID) VALUES (?, ?)";
            return jdbcTemplate.update(sql, name, teacherID);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

    }

    // delete classroom
    public boolean deleteClassroom(int classroomID) {
        try {
            // SQL query to check if the classroom exists
            String checkSql = "SELECT COUNT(*) FROM classroom WHERE classroomID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, classroomID);
            if (count == null || count == 0) {
                // classroom does not exist
                return false;
            }
            // SQL query to delete all users enrolled in class
            String studentsSQL = "DELETE FROM enrolled WHERE classroomID = ?";
            jdbcTemplate.update(studentsSQL, classroomID);
            // SQL query to delete classroom from the database
            String sql = "DELETE FROM classroom WHERE classroomID = ?";
            return jdbcTemplate.update(sql, classroomID) > 0;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    // join classroom
    public int joinClassroom(int classroomID, int studentID) {
        try {
            // SQL query to check if the student is already in the classroom
            String checkSql = "SELECT COUNT(*) FROM enrolled WHERE classroomID = ? AND studentID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, classroomID, studentID);
            if (count != null && count > 0) {
                // student is already in the classroom
                return 0;
            }
            // SQL query to insert student into the classroom
            String sql = "INSERT INTO enrolled (classroomID, studentID) VALUES (?, ?)";
            return jdbcTemplate.update(sql, classroomID, studentID);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

    }

    // leave classroom
    public boolean leaveClassroom(int classroomID, int studentID) {
        try {
            String checkSql = "SELECT COUNT(*) FROM enrolled WHERE classroomID = ? AND studentID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, classroomID, studentID);
            if (count == null || count == 0) {
                return false;
            }
    
            String sql = "DELETE FROM enrolled WHERE classroomID = ? AND studentID = ?";
            return jdbcTemplate.update(sql, classroomID, studentID) > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    

    // get classroom
    public Classroom getClassroom(int classroomID) {
        try {
            String sql = "SELECT * FROM classroom WHERE classroomID = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Classroom(
                    rs.getInt("classroomID"),
                    rs.getString("name"),
                    rs.getInt("teacherID")), classroomID);
        } catch (Exception e) {
            System.out.println("Error fetching classroom: " + e.getMessage());
            return null;
        }
    }

    // get all students in classroom
    public List<User> getAllStudentsInClassroom(int classroomID) {
        String sql = """
                    SELECT u.userID, u.username, u.password
                    FROM enrolled e
                    JOIN user u ON e.studentID = u.userID
                    WHERE e.classroomID = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("userID"),
                rs.getString("username"),
                rs.getString("password")), classroomID);
    }

    // get all classrooms
    public List<Classroom> getAllClassrooms(int userID) {
        try {
            String sql = "SELECT * FROM classroom WHERE teacherID != ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Classroom(
                    rs.getInt("classroomID"),
                    rs.getString("name"),
                    rs.getInt("teacherID")), userID);
        } catch (Exception e) {
            System.out.println("Error fetching classrooms: " + e.getMessage());
            return List.of();
        }
    }

    // get all classrooms for a user
    public List<Classroom> getAllClassroomsForUser(int userID) {
        try {
            String sql = """
                    SELECT DISTINCT c.classroomID, c.name, c.teacherID
                    FROM classroom c
                    LEFT JOIN enrolled e ON c.classroomID = e.classroomID
                    WHERE c.teacherID = ? OR e.studentID = ?
                    """;

            return jdbcTemplate.query(sql, (rs, rowNum) -> new Classroom(
                    rs.getInt("classroomID"),
                    rs.getString("name"),
                    rs.getInt("teacherID")), userID, userID);

        } catch (Exception e) {
            System.out.println("Error fetching classrooms: " + e.getMessage());
            return List.of(); // return empty list instead of null
        }
    }

}
