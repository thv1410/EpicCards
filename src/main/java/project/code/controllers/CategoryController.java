package project.code.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import project.code.models.Category;
import project.code.services.CategoryService;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // Redirect to the category creation page
    @GetMapping("/category")
    public String categoryPage() {
        return "category";
    }

    // Handle category creation
    @PostMapping("/createCategory")
    @ResponseBody
    public String createCategory(@Valid @RequestBody Category category) {
        String title = category.getTitle();
        int userID = category.getUserID();
        int result = categoryService.insertCategory(title, userID);
        if (result > 0) {
            return "Category created successfully!";
        } else {
            return "Failed to create category.";
        }
    }

    // Handle category deletion
    @PostMapping("/deleteCategory")
    @ResponseBody
    public String deleteCategory(@RequestParam int categoryID) {
        boolean result = categoryService.deleteCategory(categoryID);
        if (result) {
            return "Category deleted successfully!";
        } else {
            return "Failed to delete category.";
        }
    }

    // Handle category retrieval
    @GetMapping("/getCategory")
    @ResponseBody
    public Category getCategory(@RequestParam int categoryID) {
        Category category = categoryService.getCategory(categoryID);
        return category;
    }

    // Handle categories retrieval
    @GetMapping("/getAllCategories")
    @ResponseBody
    public List<Category> getAllCategories(@RequestParam int userID, @RequestParam String title) {
        List<Category> categories = categoryService.getAllCategories(userID, title);
        return categories;
    }

    // Handle categories retrieval
    @GetMapping("/getAllCategoriesFromUser")
    @ResponseBody
    public List<Category> getAllCategoriesFromUser(@RequestParam int userID) {
        List<Category> categories = categoryService.getAllCategoriesFromUser(userID);
        return categories;
    }

    // Handle categories retrieval
    @GetMapping("/getAllCategoriesFromTitle")
    @ResponseBody
    public List<Category> getAllCategoriesFromTitle(@RequestParam String title) {
        List<Category> categories = categoryService.getAllCategoriesFromTitle(title);
        return categories;
    }
}