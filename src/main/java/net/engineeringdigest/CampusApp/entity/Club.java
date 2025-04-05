package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Document(collection = "clubs")
@AllArgsConstructor
@NoArgsConstructor
public class Club {


    @Id
    private String cid;
    @Indexed(unique = true)
    private String cname;
    private String cdiscription;
    private String clogoUrl;

    //foreign material
    private String adminid; //contains uid of the person that is selected as admin of club by SUPERUSER
    private List<String> pid; //contains post refrences
    private List<String> mids;
    private List<String> mname;
    private List<User> memberList;
    private List<String> eid;
    private List<String> aid;




}
