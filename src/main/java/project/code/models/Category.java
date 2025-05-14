package project.code.models;

import jakarta.validation.constraints.NotBlank;

// This class represents a category entity with its properties.
public class Category {
    private int categoryID; 
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private int userID;

    public Category(int categoryID, String title, int userID) {
        this.categoryID = categoryID;
        this.title = title;
        this.userID = userID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getTitle() {
        return title;
    }

    public int getUserID() {
        return userID;
    }
}