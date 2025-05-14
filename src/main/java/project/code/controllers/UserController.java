package project.code.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import project.code.models.User;
import project.code.services.UserService;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public String register(@Valid @RequestBody User user) {
        int result = userService.insertUser(user.getUsername(), user.getPassword());
        if (result > 0) {
            System.out.println("User registered successfully: " + user.getUsername());
            return "User registered successfully!";
        } else if (result == 0) {
            System.out.println("Username already exists: " + user.getUsername());
            return "Username already exists!";
        } else {
            System.out.println("Failed to register user: " + user.getUsername());
            return "Failed to register user.";
        }
    }

    // Login an existing user and return the user to the frontend
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        User result = userService.authenticate(user.getUsername(), user.getPassword());
        Map<String, String> response = new HashMap<>();

        if (result != null) {
            response.put("message", "success");
            response.put("username", result.getUsername());
            response.put("userID", String.valueOf(result.getUserID()));
            System.out.println(
                    "User successfully logged in: " + result.getUsername() + " with ID: " + result.getUserID());
        } else {
            response.put("message", "fail");
        }

        return response;
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("Fetching all users...");
        return userService.getAllUsers();
    }

    // Get user by username
    @GetMapping("/getUser")
    public User getUserByUsername(@RequestParam String username) {
        System.out.println("Fetching user by username : " + username);
        return userService.getUserByUsername(username);
    }

    // Get user's statistics
    @GetMapping("/getUserStatistics")
    public List<Integer> getUserStatistics(@RequestParam int userID) {
        return userService.getUserStatistics(userID);
    }

    @GetMapping("/getAllOtherUsers")
    public List<User> getAllOtherUsers(@RequestParam int userID) {
        return userService.getAllUsersExcept(userID);
    } // returns uses except the one with given userID

}
