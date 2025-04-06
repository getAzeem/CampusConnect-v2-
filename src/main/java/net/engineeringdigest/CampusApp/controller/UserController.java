package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173"
public class UserController {

    @Autowired
    UserService userService;

    // Update user's password
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assuming enrollmentNo is used as the username

        boolean isUpdated = userService.updatePassword(username, newPassword);
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's email
    @PutMapping("/email")
    public ResponseEntity<String> updateEmail(@RequestBody String newEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateEmail(username, newEmail);
        if (isUpdated) {
            return ResponseEntity.ok("Email updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's full name
    @PutMapping("/full-name")
    public ResponseEntity<String> updateFullName(@RequestBody String newFullName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateFullName(username, newFullName);
        if (isUpdated) {
            return ResponseEntity.ok("Full name updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's degree
    @PutMapping("/degree")
    public ResponseEntity<String> updateDegree(@RequestBody String newDegree) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateDegree(username, newDegree);
        if (isUpdated) {
            return ResponseEntity.ok("Degree updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's year of study
    @PutMapping("/year-of-study")
    public ResponseEntity<String> updateYearOfStudy(@RequestBody String newYearOfStudy) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateYearOfStudy(username, newYearOfStudy);
        if (isUpdated) {
            return ResponseEntity.ok("Year of study updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's GitHub profile
    @PutMapping("/github")
    public ResponseEntity<String> updateGithub(@RequestBody String newGithub) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateGithub(username, newGithub);
        if (isUpdated) {
            return ResponseEntity.ok("GitHub profile updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's LinkedIn profile
    @PutMapping("/linkedin")
    public ResponseEntity<String> updateLinkedin(@RequestBody String newLinkedin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateLinkedin(username, newLinkedin);
        if (isUpdated) {
            return ResponseEntity.ok("LinkedIn profile updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's portfolio link
    @PutMapping("/portfolio")
    public ResponseEntity<String> updatePortfolio(@RequestBody String newPortfolio) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updatePortfolio(username, newPortfolio);
        if (isUpdated) {
            return ResponseEntity.ok("Portfolio link updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Update user's skills
    @PutMapping("/skills")
    public ResponseEntity<String> updateSkills(@RequestBody List<String> newSkills) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updateSkills(username, newSkills);
        if (isUpdated) {
            return ResponseEntity.ok("Skills updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping("/pfp")
    public ResponseEntity<String> updatepfp(@RequestParam String pfpurl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isUpdated = userService.updatepfp(username,pfpurl);
        if (isUpdated) {
            return ResponseEntity.ok("Skills updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Delete the currently authenticated user
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isDeleted = userService.deleteUser(username);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}