package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.Project;
import net.engineeringdigest.CampusApp.entity.ProjectJoinRequest;
import net.engineeringdigest.CampusApp.repository.ProjectRepository;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.ProjectJoinRequestRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        requestRepository.save(request);
    }

    public List<ProjectJoinRequest> getRequestsForProjectOwner(String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername);
        return requestRepository.findAllByProjectIdIn(owner.getProjects());
    }

    public void acceptRequest(String requesterId, String projectName) {
        ProjectJoinRequest request = requestRepository.findByRequesterIdAndProjectName(requesterId, projectName);
        if (request == null) throw new RuntimeException("Request not found");

        Project project = projectRepository.findByPname(projectName);
        if (project == null) throw new RuntimeException("Project not found");

        Optional<User> userOptional = userRepository.findById(requesterId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();  // You can now safely use the User object
            // Perform actions with the user
            if (!project.getContributor().contains(user.getUid())) {
                project.getContributor().add(user.getUid());
            }
            if (!user.getJoinedProjects().contains(project.getPid())) {
                user.getJoinedProjects().add(project.getPid());
            }

            projectRepository.save(project);
            userRepository.save(user);
            requestRepository.delete(request);
        } else {
            // Handle the case where the user is not found
            System.out.println("User not found with ID: " + requesterId);
        }




    }

    public void denyRequest(String requesterId, String projectName) {
        ProjectJoinRequest request = requestRepository.findByRequesterIdAndProjectName(requesterId, projectName);
        if (request == null) throw new RuntimeException("Request not found");

        requestRepository.delete(request);
    }
}
