package sampleAPIs.restserver.services.api;

import petstore.v4.r1.a1.pet.Pet;
import petstore.v4.r1.a1.pet.PetApiResponse;
import petstore.v4.r1.a1.pet.TaskUUIDApiResponse;

public interface PetService {
  PetApiResponse.OneOfDataWrapper petById(String id);
  TaskUUIDApiResponse.OneOfDataWrapper create(Pet id);
}
