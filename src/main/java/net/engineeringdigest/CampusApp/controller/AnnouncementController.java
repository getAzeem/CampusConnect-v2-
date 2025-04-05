package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.Announcement;
import net.engineeringdigest.CampusApp.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
@CrossOrigin(origins = "http://localhost:5173")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;

    // Create announcement
    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(@RequestBody Announcement announcement) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Announcement createdAnnouncement = announcementService.createAnnouncement(announcement, username);
            return ResponseEntity.ok(createdAnnouncement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get announcement by ID
    @GetMapping("/get")
    public ResponseEntity<?> getAnnouncementById(@RequestParam String aid) {
        try {
            Announcement announcement = announcementService.getAnnouncementById(aid);
            return ResponseEntity.ok(announcement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all announcements
    @GetMapping("/all")
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    // Delete announcement by name
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAnnouncement(@RequestParam String aname, @RequestParam String username) {
        String result = announcementService.deleteAnnouncementByName(aname, username);
        if (result.equals("Announcement deleted successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
