package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Document(collection = "projectJoinRequests")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinRequest {
    @Id
    private String id;

    private String projectId;
    private String projectName;

    private String requesterId;
    private String requesterUsername;
    private String requesterFullName;

    private String email;
    private String githubProfile;
    private String linkedinProfile;
    private String portfolio;   // Optional personal site

    private String description; // What the user is bringing to the table
    private List<String> skills; // Automatically fetched from user's profile

    private String ownerofproject;
}
