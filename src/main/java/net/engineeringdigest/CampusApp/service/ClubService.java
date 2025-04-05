package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.Club;
import net.engineeringdigest.CampusApp.entity.UserRole;
import net.engineeringdigest.CampusApp.entity.MemberRole;

import net.engineeringdigest.CampusApp.entity.Member;
import net.engineeringdigest.CampusApp.entity.User;
import net.engineeringdigest.CampusApp.repository.ClubRepository;
import net.engineeringdigest.CampusApp.repository.MemberRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClubService {
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;  // No authenticated user found
        }

        String loggedInUsername = authentication.getName();
        return userRepository.findByUsername(loggedInUsername);
    }





    public Club createClub(Club club) {
        if (!StringUtils.hasText(club.getClogoUrl())) {
            club.setClogoUrl(null);
        }
        if (!StringUtils.hasText(club.getAdminid())) {
            club.setAdminid(null);
        }
        if (club.getPid() == null) {
            club.setPid(Collections.emptyList());
        }

        if(club.getMids()==null){
            club.setMids(Collections.emptyList());
        }

        if(club.getMname()==null){
            club.setMname(Collections.emptyList());
        }

        if (club.getMemberList() == null) {
            club.setMemberList(Collections.emptyList());  // Initialize the list
        }

        if (club.getEid() == null) {
            club.setEid(Collections.emptyList());  // Initialize the list
        }

        if (club.getAid() == null) {
            club.setAid(Collections.emptyList());  // Initialize the list
        }

        return clubRepository.save(club);
    }



    @Transactional
    public String promoteToAdmin(String superUsername, String clubName, String targetUsername) {
        // 1️⃣ Ensure the target user exists
        User targetUser = userRepository.findByUsername(targetUsername);
        if (targetUser == null) {
            throw new RuntimeException("User with username '" + targetUsername + "' not found");
        }

        // 2️⃣ Ensure the club exists by name
        Club club = clubRepository.findByCname(clubName);
        if (club == null) {
            throw new RuntimeException("Club with name '" + clubName + "' not found");
        }

        // 3️⃣ Ensure target user is a student before promoting
        if (!targetUser.getRole().equals(UserRole.student)) {
            throw new RuntimeException("Only a student can be promoted to admin.");
        }

        // 4️⃣ Create a new Member record for this user in the club
        Member member = new Member();
        member.setUid(targetUser.getUid());  // Set user ID
        member.setCid(club.getCid());        // Set club ID
        member.setRole(MemberRole.PACCESS);  // Assign admin role
        member.setMname(targetUser.getFullName());
        member.setMpfp(targetUser.getPfpurl());
        Member savedMember = memberRepository.save(member); // Save member

        // 5️⃣ Update the Club entity
        club.setAdminid(targetUser.getUid()); // Set new admin
        club.getMids().add(savedMember.getMid());// Add to mids list
        club.getMname().add(savedMember.getMname());
        club.getMemberList().add(targetUser);
        clubRepository.save(club); // Save club

        // 6️⃣ Update the user's role
        targetUser.setRole(UserRole.admin);
        targetUser.setClubid(club.getCid());
        userRepository.save(targetUser);

        return "User '" + targetUsername + "' is now Admin of Club '" + clubName + "'";
    }


    @Transactional
    public String promoteToAdminbyAdmin(String currentadminuname, String targetUsername) {
        User user = userRepository.findByUsername(currentadminuname);
        if (user.getRole() != UserRole.admin) {
            return "Only an admin can perform this action.";
        }

        // Ensure the target user exists
        User targetUser = userRepository.findByUsername(targetUsername);
        if (targetUser == null) {
            throw new RuntimeException("User with username '" + targetUsername + "' not found");
        }

        // Fetch the club associated with the current admin
        Club club = clubRepository.findByCid(user.getClubid());
        if (club == null) {
            throw new RuntimeException("Club not found for current admin.");
        }

        // Fetch the current admin's member record
        Member currentAdminMember = memberRepository.findByuid(user.getUid());
        if (currentAdminMember == null) {
            throw new RuntimeException("Current admin membership record not found.");
        }

        // Fetch the target user's existing membership in the club (if any)
        Member targetMember = memberRepository.findByuid(targetUser.getUid());

        if (targetMember == null) {
            // If target user is not already a member, create a new membership entry
            targetMember = new Member();
            targetMember.setUid(targetUser.getUid());
            targetMember.setCid(club.getCid());
            targetMember.setMname(targetUser.getFullName());
            targetMember.setMpfp(targetUser.getPfpurl());

        }

        // Update roles: Current admin -> Student (NONPACCESS), Target user -> Admin (PACCESS)
        currentAdminMember.setRole(MemberRole.NONPACCESS);
        targetMember.setRole(MemberRole.PACCESS);
        memberRepository.save(currentAdminMember);
        memberRepository.save(targetMember);

        // Update the club admin
        club.setAdminid(targetUser.getUid());

        // Remove the old admin from club's member lists


        // Add new admin
        club.getMids().add(targetMember.getMid());
        club.getMname().add(targetMember.getMname());
        club.getMemberList().add(targetUser);

        clubRepository.save(club);

        // Update user roles
        user.setRole(UserRole.student);  // Current admin demoted to student
        user.setClubid(null);  // Remove club association
        userRepository.save(user);

        targetUser.setRole(UserRole.admin);  // Target user promoted to admin
        targetUser.setClubid(club.getCid());  // Assign club to new admin
        userRepository.save(targetUser);

        return "User '" + targetUsername + "' is now Admin of Club '" + club.getCname() + "'";
    }





    @Transactional
    public String addMemberToClub(String targetUsername) {
        // Fetch the logged-in user
        User admin = getLoggedInUser();
        if (admin == null) {
            return "Unauthorized: No logged-in user found.";
        }

        if (admin.getRole() != UserRole.admin) {
            return "Access Denied: Only an admin can add members.";
        }

        // Fetch the club associated with the logged-in admin
        Club club = clubRepository.findByCid(admin.getClubid());
        if (club == null) {
            return "Error: Club not found for the admin.";
        }

        // Fetch the target user
        User targetUser = userRepository.findByUsername(targetUsername);
        if (targetUser == null) {
            return "User does not exist";
        }

        // Check if the user is already a member
        Member existingMember = memberRepository.findByuid(targetUser.getUid());
        if (existingMember != null && existingMember.getCid().equals(club.getCid())) {
            return "User is already a member of this club";
        }

        // Create or update the membership record
        Member targetMember = existingMember != null ? existingMember : new Member();
        targetMember.setUid(targetUser.getUid());
        targetMember.setCid(club.getCid());
        targetMember.setMname(targetUser.getFullName());
        targetMember.setRole(MemberRole.NONPACCESS);
        targetMember.setMpfp(targetUser.getPfpurl());


        // Save the new member to the club
        memberRepository.save(targetMember);

        // Update the user's club association
        targetUser.setClubid(club.getCid());
        userRepository.save(targetUser);

        club.getMids().add(targetMember.getMid());
        club.getMname().add(targetMember.getMname());
        club.getMemberList().add(targetUser);
        clubRepository.save(club);

        return "User '" + targetUsername + "' has been successfully added as a member of club '" + club.getCname() + "'";
    }



    public String givepostaccess(String targetmembername){
        User targetuser=userRepository.findByUsername(targetmembername);

        Member targetmember=memberRepository.findBymname(targetuser.getFullName());
        if(targetmember!=null) {
            if (targetmember.getRole() == MemberRole.NONPACCESS) {
                targetmember.setRole(MemberRole.PACCESS);
                memberRepository.save(targetmember);
                return targetmember.getMname() + " has been allowed to post.";
            } else {
                return "Member already has access to post";
            }
        }
        return "Member does not exist";
    }

    public Map<String, Object> getClubByname(String clubName) {
        Club club = clubRepository.findByCname(clubName);

        if (club == null) {
            throw new RuntimeException("Club not found with name: " + clubName);
        }

        // Create HashMap and put only required fields
        Map<String, Object> clubDetails = new HashMap<>();
        clubDetails.put("cname", club.getCname());
        clubDetails.put("cdiscription", club.getCdiscription());
        clubDetails.put("clogoUrl", club.getClogoUrl());
        clubDetails.put("adminid", club.getAdminid());
        clubDetails.put("pid", club.getPid());  // List of post references
        clubDetails.put("mids", club.getMids()); // List of member IDs
        clubDetails.put("mname", club.getMname()); // List of member names

        return clubDetails;
    }

    public List<Map<String, String>> getAllClubMembers(String clubName) {
        Club club = clubRepository.findByCname(clubName);
        if (club == null) {
            throw new RuntimeException("Club not found.");
        }

        return club.getMemberList().stream()
                .map(user -> {
                    Map<String, String> userInfo = new HashMap<>();
                    userInfo.put("username", user.getUsername());
                    userInfo.put("pfp", user.getPfpurl());
                    userInfo.put("fullname", user.getFullName());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("linkedin", user.getLinkedin());
                    userInfo.put("github", user.getGithub());
                    return userInfo;
                })
                .collect(Collectors.toList());
    }



}


