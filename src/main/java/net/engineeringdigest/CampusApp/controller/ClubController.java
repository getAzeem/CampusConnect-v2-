package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.Club;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/club")
@CrossOrigin(origins = "http://localhost:5173")
public class ClubController {
    @Autowired
    ClubService clubService;





    @PostMapping("/create")
    public ResponseEntity<?> createClub(@RequestBody Club club) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Debugging: Print user roles
        System.out.println("User Roles: " + authentication.getAuthorities());



        Club savedClub = clubService.createClub(club);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClub);
    }


    @PutMapping("/promotebysuperuser")
    public ResponseEntity<String> promoteToAdminbysuperuser(@RequestParam String clubName, @RequestParam String targetUsername) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // 🔹 Get the logged-in user's ID (super user performing the action)
        String loggedInUsername = authentication.getName();

        // 🔹 Call service method to promote the user
        String response = clubService.promoteToAdmin(loggedInUsername, clubName, targetUsername);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/promote")
    public ResponseEntity<String> promoteToAdmin(@RequestParam String targetUsername) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // 🔹 Get the logged-in user's ID (super user performing the action)
        String loggedInUsername = authentication.getName();

        // 🔹 Call service method to promote the user
        String response = clubService.promoteToAdminbyAdmin(loggedInUsername, targetUsername);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/Give_Post_access")
    public ResponseEntity<String> givepostaccess(@RequestParam String targetmembername) {
        String response = clubService.givepostaccess(targetmembername);


        if (response.equals("Member does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.equals("Member already has access to post")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/addMember")
    public ResponseEntity<?> addMemberToClub(@RequestParam String targetUsername) {
        try {
            String response = clubService.addMemberToClub(targetUsername);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @GetMapping("/getclubbyname")
    public ResponseEntity<Map<String, Object>> getClubByName(@RequestParam String clubName) {
        try {
            Map<String, Object> clubDetails = clubService.getClubByname(clubName);
            return ResponseEntity.ok(clubDetails);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/getclubmembers")
    public List<Map<String, String>> getClubMembers(@RequestParam String clubName) {
        return clubService.getAllClubMembers(clubName);
    }


}
