package project.code.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import project.code.models.Flashcard;

@Repository
public class FlashcardService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Object that maps the rows of the database to individual Flashcard objects
    private static final RowMapper<Flashcard> FLASHCARD_MAPPER = new RowMapper<Flashcard>() {
        @Override
        public Flashcard mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Flashcard(rs.getInt("flashcardID"), rs.getString("term"), rs.getString("definition"), rs.getInt("categoryID"));
        }
    };

    public int insertFlashcard(String term, String definition, int categoryID) {
        try {
            // SQL query to insert flashcard into the database
            String sql = "INSERT INTO flashcard (term, definition, categoryID) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, term, definition, categoryID);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    public boolean deleteFlashcard(int flashcardID) {
        try {
            // SQL query to delete flashcard from the database
            String sql = "DELETE FROM flashcard WHERE flashcardID = ?";
            return jdbcTemplate.update(sql, flashcardID) > 0;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Flashcard getFlashcard(int flashcardID) {
        try {
            // SQL query to get flashcard from the database matching flashcardID
            String sql = "SELECT * FROM flashcard WHERE flashcardID = ?";
            Flashcard flashcard = jdbcTemplate.queryForObject(sql, FLASHCARD_MAPPER, flashcardID);
            return flashcard;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Flashcard> getAllFlashcards(int categoryID, String term) {
        try {
            // SQL query to get all flashcards from the database matching categoryID and term
            String sql = "SELECT * FROM flashcard WHERE categoryID = ? AND term LIKE ?";
            List<Flashcard> flashcards = jdbcTemplate.query(sql, FLASHCARD_MAPPER, categoryID, "%" + term + "%");
            return flashcards;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Flashcard> getAllFlashcardsFromCategory(int categoryID) {
        try {
            // SQL query to get all flashcards from the database matching categoryID
            String sql = "SELECT * FROM flashcard WHERE categoryID = ?";
            List<Flashcard> flashcards = jdbcTemplate.query(sql, FLASHCARD_MAPPER, categoryID);
            return flashcards;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}