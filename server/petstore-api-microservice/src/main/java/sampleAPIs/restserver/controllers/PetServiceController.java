package sampleAPIs.restserver.controllers;

import common.v1.a1.response.ApiResponseMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import petstore.v4.a1.pet.*;
import sampleAPIs.restserver.services.api.PetService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
public class PetServiceController implements  PetApiControllerInterface {
  private final PetService petService;

  @Autowired
  public PetServiceController(PetService petService) {
    this.petService = petService;
  }

  public MappingJacksonValue getPetById(Long id, HttpServletRequest r, HttpServletResponse res) {
    PetApiResponse.OneOfDataWrapper pet = petService.petById(id);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    PetApiResponse response = new PetApiResponse();
    response.setData(pet);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  public MappingJacksonValue uploadFile(Object s, Long id, String metaData, Map<String, String> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    PetApiResponse.OneOfDataWrapper uploadResp = petService.upload(id, httpServletRequest);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    PetApiResponse response = new PetApiResponse();

    response.setData(uploadResp);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  public MappingJacksonValue addPet(Pet var1, HttpServletRequest var2, HttpServletResponse var3) {

    PetApiResponse.OneOfDataWrapper petResp = petService.create(var1);
    ApiResponseMetadata metadata = new ApiResponseMetadata();
    PetApiResponse response = new PetApiResponse();

    response.setData(petResp);
    response.setMetadata(metadata);
    MappingJacksonValue mapping = new MappingJacksonValue(response);
    return mapping;
  }

  public void getImageById(Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    GridFsResource resource = petService.download(id);
    try {
      IOUtils.copy(resource.getInputStream(), httpServletResponse.getOutputStream());
    }
    catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}

