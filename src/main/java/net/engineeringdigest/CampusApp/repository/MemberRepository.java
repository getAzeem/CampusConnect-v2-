package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Club;
import net.engineeringdigest.CampusApp.entity.Member;
import net.engineeringdigest.CampusApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, ObjectId> {
    Member findByuid(String uid);
    Member findBymname(String name);
}
