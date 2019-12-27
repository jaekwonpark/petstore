package sampleAPIs.restserver.controllers;

import common.v1.a1.response.ApiResponseMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import petstore.v4.a1.pet.*;
import sampleAPIs.restserver.services.api.PetService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class PetServiceController implements  PetApiControllerInterface {
  private final PetService petService;

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

  @RequestMapping(
      value = {"/petstore/v4.a1/pet/image/{uuid}"},
      produces = {"application/octet-stream"},
      method = {RequestMethod.GET}
  )
  public void getImageById(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    GridFsResource resource = petService.download(s);
    try {
      IOUtils.copy(resource.getInputStream(), httpServletResponse.getOutputStream());
    }
    catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}

