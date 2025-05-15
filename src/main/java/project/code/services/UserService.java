package project.code.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import project.code.models.User;
// import org.springframework.security.core.userdetails.User as SecurityUser;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

@Repository
public class UserService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // BCryptPasswordEncoder is used to hash passwords
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // insert new user
    public int insertUser(String username, String password) {
        try {
            // SQL query to check if the username already exists
            String checkSql = "SELECT COUNT(*) FROM user WHERE username = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, username);
            if (count != null && count > 0) {
                // username already exists
                return 0;
            }
            // hash the password
            String hashedPassword = encoder.encode(password);
            // SQL query to insert user into the database
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            return jdbcTemplate.update(sql, username, hashedPassword);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

    }

    // authenticate user
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("password")), username);

            // validate entered password against stored hash
            if (user != null && encoder.matches(password, user.getPassword())) {
                return user;
            } else {
                return null; // wrong password
            }
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
            return null; // user not found or DB error
        }
    }

    // get all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("userID"),
                rs.getString("username"),
                rs.getString("password")));
    }

    // get user by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("password")), username);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null; // user not found
        }
    }

    // get user statistics
    public List<Integer> getUserStatistics(int userID) {
        String sql = "SELECT user.userID, COUNT(DISTINCT category.categoryID) AS categoriesCreated, "
                + "COUNT(DISTINCT classroom.classroomID) AS classroomsCreated, "
                + "COUNT(DISTINCT enrolled.classroomID) AS classroomsJoined, "
                + "COUNT(DISTINCT flashcard.flashcardID) AS flashcardsCreated "
                + "FROM user "
                + "LEFT JOIN category ON user.userID = category.userID "
                + "LEFT JOIN classroom ON user.userID = classroom.teacherID "
                + "LEFT JOIN enrolled ON user.userID = enrolled.studentID "
                + "LEFT JOIN category AS category_for_flashcards ON category_for_flashcards.userID = user.userID "
                + "LEFT JOIN flashcard ON flashcard.categoryID = category_for_flashcards.categoryID "
                + "WHERE user.userID = ? "
                + "GROUP BY user.userID";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                List<Integer> stats = new ArrayList<>();
                stats.add(rs.getInt("categoriesCreated"));
                stats.add(rs.getInt("flashcardsCreated"));
                stats.add(rs.getInt("classroomsCreated"));
                stats.add(rs.getInt("classroomsJoined"));
                return stats;
            }, userID);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // get all users except the current user
    public List<User> getAllUsersExcept(int excludeUserID) {
        String sql = "SELECT * FROM user WHERE userID != ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("userID"),
                rs.getString("username"),
                rs.getString("password")), excludeUserID);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String role = "ROLE_USER";

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role)));
    }

}
