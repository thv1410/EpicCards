package project.code.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Create a Java class to read usernames from a CSV file and generate SQL insert statements for data.sql
public class DataConfig {

    public static void main(String[] args) {
        String csvFile = "name.csv";
        String outputFile = "data.sql";
        String line;
        String csvSplitBy = ",";

        try (
                BufferedReader br = new BufferedReader(new FileReader(csvFile));
                FileWriter writer = new FileWriter(outputFile)) {
            int count = 0;

            while ((line = br.readLine()) != null && count < 1200) {
                String[] parts = line.split(csvSplitBy);
                if (parts.length < 2)
                    continue;

                String username = parts[0].trim();

                // Create INSERT SQL statement with a placeholder password
                String insertSQL = String.format(
                        "INSERT INTO user (username, password) VALUES ('%s', 'hashed password');%n",
                        username);

                writer.write(insertSQL);
                count++;
            }

            System.out.println("data.sql created with " + count + " user entries.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
