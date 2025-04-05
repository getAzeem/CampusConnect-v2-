package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Club;
import net.engineeringdigest.CampusApp.entity.Post;
import net.engineeringdigest.CampusApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends MongoRepository<Club, ObjectId> {
    Club findByCname(String cname);
    Club findByCid(String cid);




}
