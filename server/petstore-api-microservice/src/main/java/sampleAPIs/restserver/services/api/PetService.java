package sampleAPIs.restserver.services.api;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import petstore.v4.a1.pet.Pet;
import petstore.v4.a1.pet.PetApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface PetService {
  PetApiResponse.OneOfDataWrapper petById(Long id);
  PetApiResponse.OneOfDataWrapper create(Pet id);
  PetApiResponse.OneOfDataWrapper upload(Long id, HttpServletRequest req);
  GridFsResource download(Long id);
}
