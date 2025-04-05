package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Announcement;
import net.engineeringdigest.CampusApp.entity.Club;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnouncementRepository extends MongoRepository<Announcement, ObjectId> {
    Announcement findByAid(String aid);
    Announcement findByAname(String aname);
}
