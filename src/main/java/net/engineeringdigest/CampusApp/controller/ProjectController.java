package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.Project;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.service.ProjectService;
import net.engineeringdigest.CampusApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/project")
public class ProjectController {

    // Service dependencies
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // ==================== Project Creation ====================
    @PostMapping("/create-project")
    public ResponseEntity<String> addProject(@RequestBody Project project) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String ownerUsername = authentication.getName();
            projectService.saveProject(project, ownerUsername);
            return new ResponseEntity<>("Project added successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    // ==================== Fetch Project Names ====================
    @GetMapping("/getprojectnames")
    public ResponseEntity<List<String>> getAllProjectNames() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String ownerUsername = authentication.getName();
            List<String> projectNames = userService.getUserByUsername(ownerUsername).getProjectNames();
            return new ResponseEntity<>(projectNames, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    // ==================== Find Projects by User Skills ====================
    @GetMapping("/skill")
    public ResponseEntity<List<Project>> findProjectBySkill() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String ownerUsername = authentication.getName();

            // Fetch the user by username
            User user = userService.getUserByUsername(ownerUsername);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
            }

            // Fetch user skills
            List<String> userSkills = user.getSkills();
            if (userSkills == null || userSkills.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No skills found for the user
            }

            // Find projects matching user skills
            Set<Project> uniqueProjects = new HashSet<>(); // Use a Set to avoid duplicates
            for (String skill : userSkills) {
                List<Project> projectsForSkill = projectService.findBySkills(skill);
                if (projectsForSkill != null && !projectsForSkill.isEmpty()) {
                    uniqueProjects.addAll(projectsForSkill); // Add projects to the set
                }
            }

            if (uniqueProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No projects found for any skill
            }

            // Convert the Set back to a List for the response
            List<Project> projects = new ArrayList<>(uniqueProjects);
            return new ResponseEntity<>(projects, HttpStatus.OK); // Return matching projects
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle unexpected errors
        }
    }




    // ==================== Delete Project ====================
    @DeleteMapping("/delete-project")
    public ResponseEntity<String> removeProject(@RequestBody String projectName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerUsername = authentication.getName();
        boolean isDeleted = projectService.removeProject(ownerUsername, projectName);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Project deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something occurred while deleting the project");
        }
    }

    @GetMapping("/all")
    public List<Project> getallproject(){
        return projectService.getAll();
    }
}

