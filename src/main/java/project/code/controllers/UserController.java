package project.code.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import project.code.models.User;
import project.code.services.UserService;
import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Redirect to the login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Redirect to the home page
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    // Redirect to the register page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Redirect to the user page
    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

    // Register a new user
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        int result = userService.insertUser(username, password);

        if (result > 0) {
            System.out.println("User registered successfully: " + username);
            return "redirect:/login?registered";
        } else if (result == 0) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        } else {
            model.addAttribute("error", "Failed to register user.");
            return "register";
        }
    }

    // Get all users
    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        System.out.println("Fetching all users...");
        return userService.getAllUsers();
    }

    // Get user by username
    @GetMapping("/getUser")
    @ResponseBody
    public User getUserByUsername(@RequestParam String username) {
        System.out.println("Fetching user by username : " + username);
        return userService.getUserByUsername(username);
    }

    // Get user's statistics
    @GetMapping("/getUserStatistics")
    @ResponseBody
    public List<Integer> getUserStatistics(@RequestParam int userID) {
        return userService.getUserStatistics(userID);
    }

    // returns uses except the one with given userID
    @GetMapping("/getAllOtherUsers")
    @ResponseBody
    public List<User> getAllOtherUsers(@RequestParam int userID) {
        return userService.getAllUsersExcept(userID);
    }

    // Get the logged-in user's information
    @GetMapping("/user-info")
    @ResponseBody
    public Map<String, Object> getUserInfo(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        return Map.of(
                "username", user.getUsername(),
                "userID", user.getUserID());
    }

}
