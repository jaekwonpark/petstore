package sampleAPIs.restserver.services.api;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import petstore.v4.a1.pet.ImageUUIDApiResponse;
import petstore.v4.a1.pet.Pet;
import petstore.v4.a1.pet.PetApiResponse;
import petstore.v4.a1.pet.TaskUUIDApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface PetService {
  PetApiResponse.OneOfDataWrapper petById(String id);
  TaskUUIDApiResponse.OneOfDataWrapper create(Pet id);
  ImageUUIDApiResponse.OneOfDataWrapper upload(HttpServletRequest req);
  GridFsResource download(String id);
}
