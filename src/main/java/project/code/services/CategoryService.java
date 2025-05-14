package project.code.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import project.code.models.Category;

@Repository
public class CategoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Object that maps the rows of the database to individual Category objects
    private static final RowMapper<Category> CATEGORY_MAPPER = new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Category(rs.getInt("categoryID"), rs.getString("title"), rs.getInt("userID"));
        }
    };

    public int insertCategory(String title, int userID) {
        try {
            // SQL query to insert category into the database
            String sql = "INSERT INTO category (title, userID) VALUES (?, ?)";
            return jdbcTemplate.update(sql, title, userID);
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    public boolean deleteCategory(int categoryID) {
        try {
            jdbcTemplate.update("DELETE FROM flashcard WHERE categoryID = ?", categoryID);
            return jdbcTemplate.update("DELETE FROM category WHERE categoryID = ?", categoryID) > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    

    public Category getCategory(int categoryID) {
        try {
            // SQL query to get category from the database matching categoryID
            String sql = "SELECT * FROM category WHERE categoryID = ?";
            Category category = jdbcTemplate.queryForObject(sql, CATEGORY_MAPPER, categoryID);
            return category;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Category> getAllCategories(int userID, String title) {
        try {
            // SQL query to get categories from the database matching userID and titles
            String sql = "SELECT * FROM category WHERE userID = ? AND title LIKE ?";
            List<Category> categories = jdbcTemplate.query(sql, CATEGORY_MAPPER, userID, "%" + title + "%");
            return categories;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Category> getAllCategoriesFromUser(int userID) {
        try {
            // SQL query to get categories from the database matching userID
            String sql = "SELECT * FROM category WHERE userID = ?";
            List<Category> categories = jdbcTemplate.query(sql, CATEGORY_MAPPER, userID);
            return categories;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Category> getAllCategoriesFromTitle(String title) {
        try {
            // SQL query to get categories from the database matching title
            String sql = "SELECT * FROM category WHERE title LIKE ?";
            List<Category> categories = jdbcTemplate.query(sql, CATEGORY_MAPPER, "%" + title + "%");
            return categories;
        } catch (Exception e) {
            // handle exception
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
