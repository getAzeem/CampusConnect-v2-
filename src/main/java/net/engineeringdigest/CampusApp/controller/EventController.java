package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.Event;
import net.engineeringdigest.CampusApp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    EventService eventService;

    // Create event
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Event createdEvent = eventService.createEvent(event, username);
            return ResponseEntity.ok(createdEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get event by ID
    @GetMapping("/get")
    public ResponseEntity<?> getEventById(@RequestParam String eid) {
        try {
            Event event = eventService.getEventById(eid);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all events
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Delete event by ID
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam String eid, @RequestParam String username) {
        String result = eventService.deleteEventById(eid, username);
        if (result.equals("Event deleted successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
