package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {
    List<Project> findBySkillsContaining(String skill);
    List<Project> findByPfieldContaining(String pfield);

    Project findByPname(String projectName);
    Project findByPid(String projectId);

}
