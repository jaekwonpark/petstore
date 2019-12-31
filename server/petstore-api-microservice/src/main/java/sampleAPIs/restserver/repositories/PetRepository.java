package sampleAPIs.restserver.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import petstore.v4.a1.pet.Pet;

import java.util.Optional;

public interface PetRepository extends MongoRepository<Pet, Long> {
  @Override
  Optional<Pet> findById(Long s);
}
