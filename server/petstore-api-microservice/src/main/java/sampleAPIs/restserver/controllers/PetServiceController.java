package sampleAPIs.restserver.controllers;

import common.v1.a1.response.ApiResponseMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RestController;
import petstore.v4.a1.pet.*;
import sampleAPIs.restserver.services.api.PetService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@RestController
public class PetServiceController implements  PetApiControllerInterface {
  private final PetService petService;
  private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  @Autowired
  public PetServiceController(PetService petService) {
    this.petService = petService;
  }

  public MappingJacksonValue getPetById(String id, HttpServletRequest r, HttpServletResponse res) {
    PetApiResponse.OneOfDataWrapper pet = petService.petById(id);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    PetApiResponse response = new PetApiResponse();
    response.setData(pet);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  public MappingJacksonValue upload(Object s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    ImageUUIDApiResponse.OneOfDataWrapper imageResp = petService.upload(httpServletRequest);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    ImageUUIDApiResponse response = new ImageUUIDApiResponse();

    response.setData(imageResp);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  public MappingJacksonValue createPet(Pet var1, HttpServletRequest var2, HttpServletResponse var3) {

    TaskUUIDApiResponse.OneOfDataWrapper taskResp = petService.create(var1);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    TaskUUIDApiResponse response = new TaskUUIDApiResponse();

    response.setData(taskResp);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  @Override
  public MappingJacksonValue getImageById(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    return null;
  }
}

