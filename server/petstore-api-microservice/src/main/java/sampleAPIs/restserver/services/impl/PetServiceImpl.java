package sampleAPIs.restserver.services.impl;

import common.v1.r0.a1.config.Message;
import common.v1.r0.a1.response.ApiResponseMetadata;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import petstore.v4.r1.a1.pet.Pet;
import petstore.v4.r1.a1.pet.PetApiResponse;
import petstore.v4.r1.a1.pet.TaskUUIDApiResponse;
import sampleAPIs.restserver.repositories.PetRepository;
import sampleAPIs.restserver.services.api.PetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetServiceImpl implements PetService {
  @Autowired
  private PetRepository petRepository;

  public PetApiResponse.OneOfDataWrapper petById(String id) {
    PetApiResponse.OneOfDataWrapper apiResp = new PetApiResponse.OneOfDataWrapper();

    var pet = petRepository.findById(id);
    if (pet.isPresent()) {
      apiResp.setValue(pet.get());
    } else {
      List<Message> messages = new ArrayList<>();
      Message message = new Message();
      message.setCode("400");
      message.setMessage("Could not find the Pet for ID " + id);
      messages.add(message);
      apiResp.setValue(messages);
    }
    return apiResp;
  }

  public TaskUUIDApiResponse.OneOfDataWrapper create(Pet pet) {

    TaskUUIDApiResponse.OneOfDataWrapper taskResp = new TaskUUIDApiResponse.OneOfDataWrapper();

    if (null == pet.getName() || null == pet.getPhotoUrls()) {
      List<Message> messages = new ArrayList<>();
      Message message = new Message();
      message.setCode("400");
      message.setMessage("Missing name or photoUrls");
      messages.add(message);
      taskResp.setValue(messages);
    } else {
      UUID uuid = createPetInDB(pet);
      if (null == uuid) {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setCode("500");
        message.setMessage("Error while saving to MongoDB");
        messages.add(message);
        taskResp.setValue(messages);
      } else {
        taskResp.setValue(uuid.toString());
      }
    }

    return taskResp;
  }

  private UUID createPetInDB(Pet pet) {
    UUID uuid = UUID.randomUUID();

    pet.setId(uuid.toString());
    try {
      petRepository.save(pet);
    } catch (Exception e) {
      System.out.printf("Exception:%s\n", e);
      return null;
    }
    return uuid;
  }
}
