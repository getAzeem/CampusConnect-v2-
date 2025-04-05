package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.Project;
import net.engineeringdigest.CampusApp.repository.ProjectRepository;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;



    // Update user's password
    public boolean updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true; // Password updated successfully
    }

    // Update user's email
    public boolean updateEmail(String username, String newEmail) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setEmail(newEmail);
        userRepository.save(user);
        return true; // Email updated successfully
    }

    // Update user's full name
    public boolean updateFullName(String username, String newFullName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setFullName(newFullName);
        userRepository.save(user);
        return true; // Full name updated successfully
    }

    // Update user's degree
    public boolean updateDegree(String username, String newDegree) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setDegree(newDegree);
        userRepository.save(user);
        return true; // Degree updated successfully
    }

    // Update user's year of study
    public boolean updateYearOfStudy(String username, String newYearOfStudy) {
        User user = userRepository.findByEnrollmentno(username);
        if (user == null) {
            return false; // User not found
        }
        user.setYearOfStudy(newYearOfStudy);
        userRepository.save(user);
        return true; // Year of study updated successfully
    }

    // Update user's GitHub profile
    public boolean updateGithub(String username, String newGithub) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setGithub(newGithub);
        userRepository.save(user);
        return true; // GitHub profile updated successfully
    }

    // Update user's LinkedIn profile
    public boolean updateLinkedin(String username, String newLinkedin) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setLinkedin(newLinkedin);
        userRepository.save(user);
        return true; // LinkedIn profile updated successfully
    }

    // Update user's portfolio link
    public boolean updatePortfolio(String username, String newPortfolio) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setPortfolio(newPortfolio);
        userRepository.save(user);
        return true; // Portfolio link updated successfully
    }

    // Update user's skills
    public boolean updateSkills(String username, List<String> newSkills) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setSkills(newSkills);
        userRepository.save(user);
        return true; // Skills updated successfully
    }

    public boolean updatepfp(String username, String pfpurl) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }
        user.setPfpurl(pfpurl);
        userRepository.save(user);
        return true; // Skills updated successfully
    }

    // Delete a user by enrollment number
    public boolean deleteUser(String username) {
        // Try fetching by username first
        User user = userRepository.findByUsername(username);

        // If not found, try fetching by enrollment number
        if (user == null) {
            user = userRepository.findByEnrollmentno(username);
        }

        if (user != null) {
            // Delete owned projects
            for (String projectName : user.getProjectNames()) {
                Project project = projectRepository.findByPname(projectName);
                if (project != null) {
                    projectRepository.delete(project);
                }
            }

            // Remove from joined projects
            for (String joinedProjectId : user.getJoinedProjects()) {
                projectService.removeUserFromProject(joinedProjectId, user.getUsername());
            }

            // Delete the user (by object for safety)
            userRepository.delete(user);
            return true;
        }

        return false; // User not found
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}