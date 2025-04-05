package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, ObjectId> {
    Event findByEid(String eid);

    Event findByEname(String ename);

    void deleteByEname(String ename);
}
