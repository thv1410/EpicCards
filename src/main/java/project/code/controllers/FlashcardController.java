package project.code.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import project.code.models.Flashcard;
import project.code.services.FlashcardService;
import project.code.services.CategoryService;

@Controller
public class FlashcardController {
    @Autowired
    FlashcardService flashcardService;

    @Autowired
    CategoryService categoryServices;

    // Redirect to the flashcard page
    @GetMapping("/flashcard")
    public String flashcardPage() {
        return "flashcard";
    }

    // Handle flashcard creation
    @PostMapping("/createFlashcard")
    public String createFlashcard(@Valid @RequestBody Flashcard flashcard) {
        String term = flashcard.getTerm();
        String definition = flashcard.getDefinition();
        int categoryID = flashcard.getCategoryID();
        ;
        int result = flashcardService.insertFlashcard(term, definition, categoryID);
        if (result > 0) {
            return "Flashcard created successfully!";
        } else {
            return "Failed to create flashcard.";
        }
    }

    // Handle flashcard deletion
    @PostMapping("/deleteFlashcard")
    public String deleteFlashcard(@RequestParam int flashcardID) {
        boolean result = flashcardService.deleteFlashcard(flashcardID);
        if (result) {
            return "Flashcard deleted successfully!";
        } else {
            return "Failed to delete flashcard.";
        }
    }

    // Handle flashcard retrieval
    @GetMapping("/getFlashcard")
    @ResponseBody
    public Flashcard getFlashcard(@RequestParam int flashcardID) {
        Flashcard flashcard = flashcardService.getFlashcard(flashcardID);
        return flashcard;
    }

    // Handle all flashcards retrieval
    @GetMapping("/getAllFlashcards")
    @ResponseBody
    public List<Flashcard> getAllFlashcards(@RequestParam int categoryID, @RequestParam String term) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards(categoryID, term);
        return flashcards;
    }

    // Handle all flashcards retrieval
    @GetMapping("/getAllFlashcardsFromCategory")
    @ResponseBody
    public List<Flashcard> getAllFlashcardsFromCategory(@RequestParam int categoryID) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcardsFromCategory(categoryID);
        return flashcards;
    }
}
