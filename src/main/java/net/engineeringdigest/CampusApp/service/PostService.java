package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.entity.*;
import net.engineeringdigest.CampusApp.repository.ClubRepository;
import net.engineeringdigest.CampusApp.repository.MemberRepository;
import net.engineeringdigest.CampusApp.repository.PostRepository;
import net.engineeringdigest.CampusApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ClubRepository clubRepository;

    public Post createPost(Post post, String username) {
        // Step 1: Find user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        // Step 2: Find member by user's UID
        Member member = memberRepository.findByuid(user.getUid());
        if (member == null) {
            throw new RuntimeException("Member not found for the user");
        }

        // Step 3: Check if member has PACCESS role
        if (member.getRole() != MemberRole.PACCESS) {
            throw new RuntimeException("You do not have permission to create a post");
        }

        // Step 4: Set mid, cid, and manme in post
        post.setMid(member.getMid());
        post.setCid(member.getCid());
        post.setManme(member.getMname());


        // Set current date and time



        // Step 5: Save post
        Post savedPost = postRepository.save(post);

        // Step 6: Update Club’s pid list
        Club club = clubRepository.findByCid(member.getCid());
        if (club == null) {
            throw new RuntimeException("Club not found with cid: " + member.getCid());
        }

        List<String> pidList = club.getPid();
        pidList.add(savedPost.getPid()); // Add new post ID
        club.setPid(pidList);

        // Save updated club
        clubRepository.save(club);

        return savedPost;
    }

    public Post getPostById(String pheading) {
        Post post = postRepository.findByPheading(pheading);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByClubName(String cname) {
        Club club = clubRepository.findByCname(cname); // resolves cname to club object
        if (club == null) {
            throw new RuntimeException("Club not found with name: " + cname);
        }
        return postRepository.findByCid(club.getCid()); // fetch posts using cid
    }



    public String deletePostById(String pheading, String username) {
        // Step 1: Get user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Step 2: Get member by user UID
        Member member = memberRepository.findByuid(user.getUid());
        if (member == null) {
            throw new RuntimeException("You are not a registered member");
        }

        // Step 3: Check if member has PACCESS role
        if (member.getRole() != MemberRole.PACCESS) {
            throw new RuntimeException("You do not have permission to delete posts");
        }

        // Step 4: Get the post by heading
        Post post = postRepository.findByPheading(pheading);
        if (post == null) {
            return "Post not found";
        }

        // Step 5: Ensure the post belongs to the same club
        if (!post.getCid().equals(member.getCid())) {
            return "You can only delete posts from your own club";
        }

        // Step 6: Remove post ID from club’s pid list
        Club club = clubRepository.findByCid(member.getCid());
        if (club != null) {
            List<String> pidList = club.getPid();
            pidList.remove(post.getPid());
            club.setPid(pidList);
            clubRepository.save(club);
        }

        // Step 7: Delete the post
        postRepository.delete(post);

        return "Post deleted successfully";
    }

    }



