package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.*;
import net.engineeringdigest.CampusApp.repository.ClubRepository;
import net.engineeringdigest.CampusApp.repository.EventRepository;
import net.engineeringdigest.CampusApp.repository.MemberRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClubRepository clubRepository;

    public Event createEvent(Event event, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Member member = memberRepository.findByuid(user.getUid());
        if (member == null) {
            throw new RuntimeException("Member not found for the user");
        }

        if (member.getRole() != MemberRole.PACCESS) {
            throw new RuntimeException("You do not have permission to create an event");
        }
        Club club=clubRepository.findByCid(member.getCid());

        // set mid, cid, uid, date, and time
        event.setMid(member.getMid());
        event.setCid(member.getCid());
        event.setUid(club.getCid());
        event.setDate(new Date());

        Event savedevent= eventRepository.save(event);


        if (club == null) {
            throw new RuntimeException("Club not found with cid: " + member.getCid());
        }

        List<String> eidList = club.getEid();
        eidList.add(savedevent.getEid()); // Add new post ID
        club.setEid(eidList);

        // Save updated club
        clubRepository.save(club);

        return savedevent;


    }

    public Event getEventById(String eid) {
        if (eventRepository.findByEid(eid)!=null) {
            return eventRepository.findByEid(eid);
        }
        return null;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public String deleteEventById(String ename, String username) {
        // Step 1: Find user
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }

        // Step 2: Find member and check role
        Member member = memberRepository.findByuid(user.getUid());
        if (member == null || member.getRole() != MemberRole.PACCESS) {
            return "Unauthorized to delete event";
        }

        // Step 3: Find event
        Event event = eventRepository.findByEname(ename);
        if (event == null) {
            return "Event not found";
        }

        // Step 4: Ensure event is from same club
        if (!event.getCid().equals(member.getCid())) {
            return "You can only delete events from your own club";
        }

        // Step 5: Remove event ID from club's list
        Club club = clubRepository.findByCid(member.getCid());
        if (club == null) {
            throw new RuntimeException("Club not found with cid: " + member.getCid());
        }

        List<String> eidList = club.getEid();
        if (eidList != null) {
            eidList.remove(event.getEid());
            club.setEid(eidList);
            clubRepository.save(club);
        }

        // Step 6: Delete event
        eventRepository.delete(event); // preferred over deleteByEname

        return "Event deleted successfully";
    }

}
