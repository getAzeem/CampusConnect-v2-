package net.engineeringdigest.CampusApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Data
@Document(collection = "members")
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    private String mid;
    private String uid;
    private String cid;
    private String mname;
    private String mpfp;
    private MemberRole Role;
}
