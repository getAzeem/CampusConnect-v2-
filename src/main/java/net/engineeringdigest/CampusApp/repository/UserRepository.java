package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String ownerUsername);
    User findByEmail(String Email);

    User findByEnrollmentno(String enrollmentno);
    User deleteByEnrollmentno(String enrollmentno);

    Optional<User> findById(String id);


}

