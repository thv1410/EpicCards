package project.code.models;

import jakarta.validation.constraints.NotBlank;

// This class represents a flashcard entity with its properties.
public class Flashcard {
    private int flashcardID;
    @NotBlank(message = "Term cannot be blank")
    private String term;
    @NotBlank(message = "Definition cannot be blank")
    private String definition;
    private int categoryID;
    
    public Flashcard(int flashcardID, String term, String definition, int categoryID) {
        this.flashcardID = flashcardID;
        this.term = term;
        this.definition = definition;
        this.categoryID = categoryID;
    }

    public int getFlashcardID() {
        return flashcardID;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public int getCategoryID() {
        return categoryID;
    }
}