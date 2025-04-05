package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.*;
import net.engineeringdigest.CampusApp.repository.AnnouncementRepository;
import net.engineeringdigest.CampusApp.repository.ClubRepository;
import net.engineeringdigest.CampusApp.repository.MemberRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClubRepository clubRepository;

    public Announcement createAnnouncement(Announcement announcement, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Member member = memberRepository.findByuid(user.getUid());
        if (member == null) {
            throw new RuntimeException("Member not found for the user");
        }

        if (member.getRole() != MemberRole.PACCESS) {
            throw new RuntimeException("You do not have permission to create an announcement");
        }

        Club club = clubRepository.findByCid(member.getCid());
        club.setAid(new ArrayList<>());


        // set mid, cid, uid
        announcement.setMid(member.getMid());
        announcement.setCid(member.getCid());
        announcement.setUid(club.getCid());
        announcement.setCname(club.getCname());


        Announcement savedAnnouncement = announcementRepository.save(announcement);

        if (club == null) {
            throw new RuntimeException("Club not found with cid: " + member.getCid());
        }

        List<String> aidList = club.getAid();
        aidList.add(savedAnnouncement.getAid());
        club.setAid(aidList);

        clubRepository.save(club);

        return savedAnnouncement;
    }

    public Announcement getAnnouncementById(String aid) {
        if (announcementRepository.findByAid(aid) != null) {
            return announcementRepository.findByAid(aid);
        }
        return null;
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public String deleteAnnouncementByName(String aname, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }

        Member member = memberRepository.findByuid(user.getUid());
        if (member == null || member.getRole() != MemberRole.PACCESS) {
            return "Unauthorized to delete announcement";
        }

        Announcement announcement = announcementRepository.findByAname(aname);
        if (announcement == null) {
            return "Announcement not found";
        }

        if (!announcement.getCid().equals(member.getCid())) {
            return "You can only delete announcements from your own club";
        }

        Club club = clubRepository.findByCid(member.getCid());
        if (club == null) {
            throw new RuntimeException("Club not found with cid: " + member.getCid());
        }

        List<String> aidList = club.getAid();
        if (aidList != null) {
            aidList.remove(announcement.getAid());
            club.setAid(aidList);
            clubRepository.save(club);
        }

        announcementRepository.delete(announcement);

        return "Announcement deleted successfully";
    }
}
