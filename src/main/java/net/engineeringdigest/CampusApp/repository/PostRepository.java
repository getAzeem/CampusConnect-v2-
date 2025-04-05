package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Member;
import net.engineeringdigest.CampusApp.entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, ObjectId> {

    List<Post> findByCid(String cid);

    Post findByPheading(String pheading);


}
