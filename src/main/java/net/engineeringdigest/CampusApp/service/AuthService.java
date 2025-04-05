package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.entity.UserRole;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public boolean createUser(User user) {
        // Validate required fields
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Enrollment number and password are required.");
        }

        // Check if user already exists
        User existingUser = userRepository.findByEnrollmentno(user.getUsername());
        if (existingUser != null) {
            return false; // User with the same enrollment number already exists
        }

        // Initialize default values for other fields
        user.setRole(UserRole.student); // Default role
        user.setEmail(user.getUsername() + "@bennett.edu.in"); // Generate email
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        user.setEnrollmentno(user.getUsername());
        // Initialize nullable fields to null or empty collections

        user.setFullName(null); // Nullable string field
        user.setDegree(null); // Nullable string field
        user.setYearOfStudy(null); // Nullable integer field
        user.setGithub(null); // Nullable string field
        user.setLinkedin(null); // Nullable string field
        user.setPortfolio(null); // Nullable string field
        user.setSkills(new ArrayList<>()); // Empty list for skills
        user.setProjectNames(new ArrayList<>()); // Empty list for project names
        user.setProjects(new ArrayList<>()); // Empty list for projects
        user.setJoinedProjects(new ArrayList<>()); // Empty list for joined projects

        // Save the user
        userRepository.save(user);
        return true; // User created successfully
    }

    public String login(String username, String password) {
        try {
            // Authenticate the user using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Set the authenticated user in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "Login successful";
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return "Invalid enrollment number or password.";
        } catch (DisabledException e) {
            // Handle disabled accounts
            return "Your account is disabled.";
        } catch (Exception e) {
            // Log unexpected errors
            System.err.println("Unexpected error during login: " + e.getMessage());
            return "An unexpected error occurred.";
        }
    }
}
