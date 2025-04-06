package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.Project;
import net.engineeringdigest.CampusApp.entity.ProjectJoinRequest;
import net.engineeringdigest.CampusApp.repository.ProjectRepository;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.ProjectJoinRequestRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectJoinRequestService {

    @Autowired
    private ProjectJoinRequestRepository requestRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // 🚀 Create a join request using separate fields
    public void createJoinRequest(String projectName, String requesterUsername, String requesterFullName,
                                  String email, String github, String linkedIn, String portfolio,
                                  String description, List<String> skills) {

        Project project = projectRepository.findByPname(projectName);
        if (project == null) {
            throw new RuntimeException("Project not found with name: " + projectName);
        }

        User user = userRepository.findByUsername(requesterUsername);
        if (user == null) {
            throw new RuntimeException("User not found: " + requesterUsername);
        }

        ProjectJoinRequest request = new ProjectJoinRequest();
        request.setProjectName(projectName);
        request.setProjectId(project.getPid());
        request.setRequesterUsername(requesterUsername);
        request.setRequesterFullName(requesterFullName);
        request.setRequesterId(user.getUid());
        request.setEmail(email);
        request.setGithubProfile(github);
        request.setLinkedinProfile(linkedIn);
        request.setPortfolio(portfolio);
        request.setDescription(description);
        request.setSkills(skills);
        request.setOwnerofproject(project.getOwner());

        requestRepository.save(request);
    }

    public List<ProjectJoinRequest> getRequestsForProjectOwner(String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername);
        if (owner == null) {
            System.out.println("Owner not found with username: " + ownerUsername);
            return Collections.emptyList();
        }

        System.out.println("Owner found: " + owner.getUsername());
        List<ProjectJoinRequest> requests = requestRepository.findAllByOwnerofproject(owner.getUid());

        if (requests.isEmpty()) {
            System.out.println("No requests found for owner: " + ownerUsername);
        }

        return requests;
    }


    public void acceptRequest(String requesterUsername, String projectName) {
        ProjectJoinRequest request = requestRepository.findByRequesterUsernameAndProjectName(requesterUsername, projectName);
        if (request == null) throw new RuntimeException("Request not found");

        Project project = projectRepository.findByPname(projectName);
        if (project == null) throw new RuntimeException("Project not found");

        User user = userRepository.findByUsername(requesterUsername);
        if (user != null) {
            // Ensure contributor list is initialized to avoid NullPointerException
            if (project.getContributor() == null) {
                project.setContributor(new ArrayList<>());
            }

            // Ensure contributornames list is initialized
            if (project.getContributornames() == null) {
                project.setContributornames(new ArrayList<>());
            }

            // Add user to the contributor list if not already present
            if (!project.getContributor().contains(user.getUid())) {
                project.getContributor().add(user.getUid());
            }

            // Add the contributor's name to the contributornames list if not already present
            if (!project.getContributornames().contains(user.getUsername())) {
                project.getContributornames().add(user.getUsername());
            }

            // Ensure joinedProjects list is initialized for the user
            if (user.getJoinedProjects() == null) {
                user.setJoinedProjects(new ArrayList<>());
            }

            // Add project ID to the user's joined projects list if not already present
            if (!user.getJoinedProjects().contains(project.getPid())) {
                user.getJoinedProjects().add(project.getPid());
            }

            projectRepository.save(project);
            userRepository.save(user);
            requestRepository.delete(request);
        } else {
            // Handle the case where the user is not found
            System.out.println("User not found with username: " + requesterUsername);
        }
    }




    public void denyRequest(String requesterUsername, String projectName) {
        ProjectJoinRequest request = requestRepository.findByRequesterUsernameAndProjectName(requesterUsername, projectName);
        if (request == null) throw new RuntimeException("Request not found");

        requestRepository.delete(request);
    }
}
