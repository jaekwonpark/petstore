package sampleAPIs.restserver.controllers;

import com.mongodb.WriteResult;
import common.v1.r0.a1.config.Message;
import common.v1.r0.a1.response.ApiResponseMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import petstore.v4.r1.a1.pet.Pet;
import petstore.v4.r1.a1.pet.PetApiControllerInterface;
import petstore.v4.r1.a1.pet.PetApiResponse;
import petstore.v4.r1.a1.pet.TaskUUIDApiResponse;
import sampleAPIs.restserver.repositories.PetRepository;
import sampleAPIs.restserver.repositories.TaskRepository;
import sampleAPIs.restserver.services.api.PetService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class PetServiceController implements PetApiControllerInterface {
  private final PetService petService;

  @Autowired
  public PetServiceController(PetService petService) {
    this.petService = petService;
  }

  public ResponseEntity<PetApiResponse> getPetById(String id, HttpServletRequest r, HttpServletResponse res) {
    PetApiResponse.OneOfDataWrapper pet = petService.petById(id);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    PetApiResponse response = new PetApiResponse();
    response.setData(pet);
    response.setMetadata(metadata);
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<TaskUUIDApiResponse> createPet(Pet var1, HttpServletRequest var2, HttpServletResponse var3) {

    TaskUUIDApiResponse.OneOfDataWrapper taskResp = petService.create(var1);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    TaskUUIDApiResponse response = new TaskUUIDApiResponse();

    response.setData(taskResp);
    response.setMetadata(metadata);
    return ResponseEntity.ok(response);
  }
}

