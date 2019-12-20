package sampleAPIs.restserver.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import petstore.v4.a1.pet.TaskUUID;

public interface TaskRepository extends MongoRepository<TaskUUID, String> {
  public TaskUUID findById(Long id);
}
