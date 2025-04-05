package net.engineeringdigest.CampusApp.controller;

import net.engineeringdigest.CampusApp.entity.Post;
import net.engineeringdigest.CampusApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        // Step 1: Get username from authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            // Step 2: Call service to create post
            Post createdPost = postService.createPost(post, username);
            return ResponseEntity.ok(createdPost);
        } catch (RuntimeException e) {
            // Step 3: Handle errors
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/get")
    public ResponseEntity<?> getPostById(@RequestParam String id) {
        try {
            Post post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/club")
    public List<Post> getPostsByClubName(@RequestParam String cname) {
        return postService.getPostsByClubName(cname);  // Uses cname instead of cid
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestParam String pheading) {
        // Step 1: Get logged-in username
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            String result = postService.deletePostById(pheading, username);

            if (result.equals("Post deleted successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
