package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String uid;



    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;

    private String pfpurl;

    private String enrollmentno;
    @NonNull
    @Indexed(unique = true)
    private String email;

    private String fullName;



    private String degree;
    private String yearOfStudy;

    private String github;


    private String linkedin;
    private String portfolio;

    private List<String> skills;



    private List<String> projectNames;



    private List<String> projects;

    private String clubid;// if the user is admin of any club...the id of the club



    private List<String> joinedProjects;
    private List<String> joinedProjectnames;

    private UserRole role;

}
