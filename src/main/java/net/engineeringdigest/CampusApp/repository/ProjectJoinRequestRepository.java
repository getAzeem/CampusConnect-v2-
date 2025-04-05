package net.engineeringdigest.CampusApp.repository;

import net.engineeringdigest.CampusApp.entity.ProjectJoinRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectJoinRequestRepository extends MongoRepository<ProjectJoinRequest, ObjectId> {

    List<ProjectJoinRequest> findByProjectId(String projectId);
    List<ProjectJoinRequest> findAllByProjectIdIn(List<String> projectIds);

    ProjectJoinRequest findByRequesterIdAndProjectName(String requesterId, String projectName);

    List<ProjectJoinRequest> findAllByRequesterUsername(String requesterUsername);

}
