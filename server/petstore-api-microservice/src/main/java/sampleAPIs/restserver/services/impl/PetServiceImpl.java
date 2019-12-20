package sampleAPIs.restserver.services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import common.v1.a1.config.Message;
import lombok.var;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import petstore.v4.a1.pet.*;
import sampleAPIs.restserver.repositories.PetRepository;
import sampleAPIs.restserver.services.api.PetService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

@Service
public class PetServiceImpl implements PetService {
  private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  @Autowired
  private PetRepository petRepository;
  @Autowired
  private GridFsTemplate gridFsTemplate;

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
        TaskUUID taskUuid = new TaskUUID();
        taskUuid.setId(uuid.toString());
        taskResp.setValue(taskUuid);
      }
    }

    return taskResp;
  }

  public ImageUUIDApiResponse.OneOfDataWrapper upload(HttpServletRequest req) {

    ImageUUIDApiResponse.OneOfDataWrapper imageResp = new ImageUUIDApiResponse.OneOfDataWrapper();
    try {
      DBObject metaData = new BasicDBObject();
      Enumeration<String> h = req.getParameterNames();
      while (h.hasMoreElements()) {
        System.out.println(h.nextElement());
      }

      ObjectId id = gridFsTemplate.store(req.getInputStream(), "myfile",
                                         req.getContentType(), metaData);
      ImageUUID imageUuid = new ImageUUID();
      imageUuid.setId(id.toString());
      imageResp.setValue(imageUuid);
    } catch (Exception e) {
      List<Message> messages = new ArrayList<>();
      Message message = new Message();
      message.setCode("500");
      message.setMessage("Exception while storing to db:"+e);
      messages.add(message);
      imageResp.setValue(messages);
    }
    return imageResp;
  }

  private UUID createPetInDB(Pet pet) {
    UUID uuid = UUID.randomUUID();

    pet.setId(uuid.toString());
    try {
      petRepository.save(pet);
    } catch (Exception e) {
      logger.severe(e.getMessage());
      return null;
    }
    return uuid;
  }
}
