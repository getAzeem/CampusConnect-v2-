package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.Project;
import net.engineeringdigest.CampusApp.repository.ProjectRepository;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserService userService;



    // Save a new project and associate it with the owner
    @Transactional
    public void saveProject(Project project, String ownerUsername) {
        try {
            // Find the owner user by username
            User owner = userRepository.findByUsername(ownerUsername);

            if (owner == null) {
                throw new RuntimeException("User with username " + ownerUsername + " not found.");
            }

            // Set the owner (ObjectId) of the project
            project.setOwner(owner.getFullName());

            if (project.getContributornames() == null) {
                project.setContributornames(new ArrayList<>());
            }
            if (!project.getContributornames().contains(project.getPname())) {
                project.getContributornames().add(project.getPname());
            }

            // Save the project in the database
            projectRepository.save(project);

            if (owner.getProjectNames() == null) {
                owner.setProjectNames(new ArrayList<>());
            }
            if (!owner.getProjectNames().contains(project.getPname())) {
                owner.getProjectNames().add(project.getPname());
            }

// Add the project ID to the user's projects list
            if (owner.getProjects() == null) {
                owner.setProjects(new ArrayList<>());
            }
            if (!owner.getProjects().contains(project.getPid())) {
                owner.getProjects().add(project.getPid());
            }

            // Save the updated user
            userRepository.save(owner);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the project", e);
        }
    }

    // Get all projects from the database
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    // Find projects by a specific skill
    public List<Project> findBySkills(String skill) {
        return projectRepository.findBySkillsContaining(skill);
    }

    // Find projects by a specific field
    public List<Project> findByPfield(String pfield) {
        return projectRepository.findByPfieldContaining(pfield);
    }

    public Project findbyPid(String projectid){
        return projectRepository.findByPid(projectid);
    }








    @Transactional
    public boolean removeProject(String ownerUsername, String projectName) {
        User user = userRepository.findByUsername(ownerUsername);
        if (user == null) {
            throw new RuntimeException("User not found: " + ownerUsername);
        }

        if (user.getProjectNames() == null || !user.getProjectNames().contains(projectName)) {
            throw new RuntimeException("Project not found in user's list: " + projectName);
        }

        Project project = projectRepository.findByPname(projectName);
        if (project == null) {
            throw new RuntimeException("Project not found: " + projectName);
        }

        if (!project.getOwner().equals(user.getUid())) {
            throw new RuntimeException("User does not own the project: " + projectName);
        }

        projectRepository.delete(project);
        user.getProjectNames().remove(projectName);
        user.getProjects().remove(project.getPid());
        userRepository.save(user);

        return true;
    }




    //Overloaded Method
    @Transactional
    public void removeUserFromProject(String joinedProject, String username) {
        // Find the project by its ID
        Project project = projectRepository.findByPname(joinedProject);
        if (project != null) {
            // Find the user by username
            User user = userRepository.findByUsername(username);
            if (user != null) {
                // Remove the user's ObjectId from the contributor list
                if (project.getContributor() != null) {
                    project.getContributor().remove(user.getUid());
                    projectRepository.save(project);
                }

                // Remove the project ID from the user's joinedProjects list
                if (user.getJoinedProjects() != null) {
                    user.getJoinedProjects().remove(joinedProject);
                    userRepository.save(user);
                }
            }
        }
    }}
