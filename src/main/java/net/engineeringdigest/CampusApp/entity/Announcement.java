package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Data
@Document(collection = "announcements")
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    @Id
    private String aid;
    private String uid;
    private String mid;
    private String cid;
    private String aname;
    private String deadlinedate;
    private String rurl;
    private String cname;
    private String cateogry;
}
