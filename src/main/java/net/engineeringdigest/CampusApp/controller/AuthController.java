package net.engineeringdigest.CampusApp.controller;


import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.service.AuthService;
import net.engineeringdigest.CampusApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            boolean isCreated = authService.createUser(user);
            if (isCreated) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this enrollment number already exists.");
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes (optional)
            System.err.println("An error occurred while creating the user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        // Delegate login logic to the service layer
        String result = authService.login(username, password);

        // Map the result to appropriate HTTP status codes
        if (result.equals("Login successful")) {
            return ResponseEntity.ok(result);
        } else if (result.contains("Invalid")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        } else if (result.contains("disabled")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
