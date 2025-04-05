package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.ProjectJoinRequest;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.ProjectJoinRequestRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import net.engineeringdigest.CampusApp.service.ProjectJoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/join-request")
public class ProjectJoinRequestController {

    @Autowired
    private ProjectJoinRequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProjectJoinRequestRepository requestRepository;


    // ✅ Send a join request
    @PostMapping("/send")
    public String sendJoinRequest(
            @RequestParam String projectName,
            @RequestParam String linkedin,
            @RequestParam String github,
            @RequestParam String email,
            @RequestParam String portfolio,
            @RequestParam String description
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) return "User not found";

        try {
            requestService.createJoinRequest(
                    projectName,
                    user.getUsername(),
                    user.getFullName(),
                    email,
                    github,
                    linkedin,
                    portfolio,
                    description,
                    user.getSkills()
            );
            return "Join request sent successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ Get all requests sent by current user
    @GetMapping("/my-requests")
    public List<ProjectJoinRequest> getMyJoinRequests() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return requestRepository.findAllByRequesterUsername(username);
    }



    // ✅ Get all join requests for project owner
    @GetMapping("/get-for-owner")
    public List<ProjectJoinRequest> getRequestsForOwner() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return requestService.getRequestsForProjectOwner(username);
    }

    // ✅ Accept a request
    @PostMapping("/accept")
    public String acceptRequest(
            @RequestParam String requesterId,
            @RequestParam String projectName
    ) {
        try {
            requestService.acceptRequest(requesterId, projectName);
            return "Request accepted successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ Deny a request
    @PostMapping("/deny")
    public String denyRequest(
            @RequestParam String requesterId,
            @RequestParam String projectName
    ) {
        try {
            requestService.denyRequest(requesterId, projectName);
            return "Request denied";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
