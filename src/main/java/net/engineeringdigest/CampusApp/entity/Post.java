package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.sql.Time;
import java.util.Date;

@Component
@Data
@Document(collection = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String pid;
    private String pheading;
    private String pdescription;
    private String pbannerurl;
    private String rurl;


    private String manme;
    private String cid;
    private String mid;

}
