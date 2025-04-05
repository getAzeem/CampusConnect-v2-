package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Component
@Data
@Document(collection = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    private String eid;
    private String uid;
    private String mid;
    private String cid;
     // if  there is a post regarding this has been uploaded by a **member** thas has **paccess** role;

    private String ename;
    private String pvenue;
    private Date date; // only if you're storing as string
    private String rurl;
}
