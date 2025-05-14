package project.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import project.code.services.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	/**
	 * This method is executed after the application context is loaded.
	 * It checks if the user table is empty and populates it with data from a CSV
	 * file.
	 * It also inserts fixed data into other tables.
	 */
	@Override
	public void run(String... args) {
		if (userService.getAllUsers().isEmpty()) {
			String csvFile = "usernames.csv";
			String line;
			String csvSplitBy = ",";
			Random random = new Random();

			String[] words = {
					"Ocean", "Tiger", "Jungle", "Shadow", "Pixel", "Solar", "Falcon", "Echo", "Nebula",
					"Blizzard", "Quartz", "Inferno", "Nova", "Comet", "Raven", "Frost", "Tornado", "Vortex",
					"Breeze", "Crimson", "Phantom", "Arctic", "Magnet", "Zephyr", "Nimbus", "Bolt", "Drift",
					"Glacier", "Cinder", "Eclipse", "Blaze", "Mirage", "Flame", "Storm", "Cascade", "Thunder",
					"Pulse", "Twilight", "Rocket", "Sonic"
			};
			String symbols = "!@#$%&*";

			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
				int count = 0;
				while ((line = br.readLine()) != null && count < 1200) {
					String[] parts = line.split(csvSplitBy);
					if (parts.length < 2)
						continue;

					String username = parts[0].trim();
					String numberPart = parts[1].trim();
					String word = words[random.nextInt(words.length)];
					char symbol = symbols.charAt(random.nextInt(symbols.length()));
					String password = word + numberPart + symbol;

					userService.insertUser(username, password);
					count++;
					System.out.println("Inserted: " + username);
				}
				System.out.println("Finished inserting " + count + " users.");
			} catch (IOException e) {
				System.err.println("Error reading file: " + e.getMessage());
			}
		} else {
			System.out.println("Users already exist in the database. Skipping insertion.");
			return;
		}
		// samples for testing
		userService.insertUser("StephenLloyd", "duck123");
		userService.insertUser("Ranni", "witch90");
		userService.insertUser("KArima", "BellPeppers10");

		// Insert fixed data into other tables using text blocks
		jdbcTemplate.update("""
				    INSERT INTO classroom (name, teacherID) VALUES
				    ('Algebra 1', 1),
				    ('Biology Basics', 2),
				    ('World History', 3),
				    ('Chemistry 101', 4),
				    ('Intro to Programming', 5),
				    ('Physics I', 6),
				    ('Literature 101', 7),
				    ('Geometry Basics', 8),
				    ('Government Studies', 9),
				    ('AP Statistics', 10)
				""");
		System.out.println("Finished inserting classrooms.");
		jdbcTemplate.update("""
				    INSERT INTO enrolled (classroomID, studentID) VALUES
				    (1, 11), (2, 12), (3, 13), (4, 14), (5, 15),
				    (6, 16), (7, 17), (8, 18), (9, 19), (10, 20)
				""");
		System.out.println("Finished inserting enrollments.");
		jdbcTemplate.update("""
				    INSERT INTO category (title, userID) VALUES
				    ('Algebra Formulas', 11), ('Cell Biology', 12),
				    ('Historical Dates', 13), ('Chemical Reactions', 14),
				    ('Java Syntax', 15), ('Forces and Motion', 16),
				    ('Shakespeare Terms', 17), ('Triangles & Angles', 18),
				    ('Constitution', 19), ('Probability Rules', 20)
				""");
		System.out.println("Finished inserting categories.");
		jdbcTemplate.update("""
				    INSERT INTO flashcard (term, definition, categoryID) VALUES
				    ('Quadratic Formula', 'Used to solve ax^2 + bx + c = 0', 1),
				    ('Point-Slope Form', 'Equation of a line given a point and slope', 1),
				    ('Mitochondria', 'Powerhouse of the cell', 2),
				    ('Nucleus', 'Control center of the cell', 2),
				    ('Treaty of Versailles', 'Ended WWI in 1919', 3),
				    ('Fall of Berlin Wall', 'Happened in 1989', 3),
				    ('Combustion', 'Chemical reaction with oxygen producing heat', 4),
				    ('Catalyst', 'Substance that speeds up a reaction', 4),
				    ('for loop', 'Loop with a fixed number of iterations', 5),
				    ('Class', 'Defines an object blueprint in Java', 5),
				    ('Newton First Law', 'Objects in motion stay in motion', 6),
				    ('Gravity', 'Force of attraction between objects', 6),
				    ('Sonnet', '14-line poem with a set rhyme scheme', 7),
				    ('Metaphor', 'Comparison without using like or as', 7),
				    ('Pythagorean Theorem', 'a^2 + b^2 = c^2', 8),
				    ('Right Angle', 'Exactly 90 degrees', 8),
				    ('Bill of Rights', 'First 10 amendments to the Constitution', 9),
				    ('Checks and Balances', 'Keeps government powers separate', 9),
				    ('Mean', 'Sum of values divided by count', 10),
				    ('Standard Deviation', 'Measure of data spread', 10)
				""");
		System.out.println("Finished inserting classroom, enrollment, categories, and flashcards.");
	}
}
