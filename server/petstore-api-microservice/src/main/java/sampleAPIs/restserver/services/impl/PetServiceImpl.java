package sampleAPIs.restserver.services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import common.v1.a1.config.Message;
import lombok.var;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import petstore.v4.a1.main.*;
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
  @Autowired
  private GridFsOperations gridFsOperations;

  public PetApiResponse.OneOfDataWrapper petById(Long id) {
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

  public PetApiResponse.OneOfDataWrapper create(Pet pet) {

    PetApiResponse.OneOfDataWrapper createResp = new PetApiResponse.OneOfDataWrapper();

    if (null == pet.getName()) {
      List<Message> messages = new ArrayList<>();
      Message message = new Message();
      message.setCode("400");
      message.setMessage("Missing name of the pet");
      messages.add(message);
      createResp.setValue(messages);
    } else {
      UUID uuid = createPetInDB(pet);
      if (null == uuid) {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setCode("500");
        message.setMessage("Error while saving to MongoDB");
        messages.add(message);
        createResp.setValue(messages);
      } else {
        createResp.setValue(pet);
      }
    }

    return createResp;
  }

  public PetApiResponse.OneOfDataWrapper upload(Long petId, HttpServletRequest req) {

    PetApiResponse.OneOfDataWrapper uploadResp = new PetApiResponse.OneOfDataWrapper();

    var petOptional = petRepository.findById(petId);
    if (petOptional.isPresent()) {
      Pet pet = petOptional.get();
      DBObject metaData = new BasicDBObject();
      try {
        ObjectId mongoFileId = gridFsTemplate.store(req.getInputStream(), "myfile",
                                               req.getContentType(), metaData);
        List photoFiles = pet.getPhotoFiles();
        if (null == photoFiles) {
          photoFiles = new ArrayList<String>();
        }
        photoFiles.add(mongoFileId.toString());
        pet.setPhotoFiles(photoFiles);
        updatePetInDB(pet);
      }
      catch (Exception e) {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setCode("500");
        message.setMessage("Exception while storing to db:"+e);
        messages.add(message);
        uploadResp.setValue(messages);
      }
      uploadResp.setValue(pet);
    } else {
      List<Message> messages = new ArrayList<>();
      Message message = new Message();
      message.setCode("400");
      message.setMessage("Could not find the Pet for ID " + petId);
      messages.add(message);
      uploadResp.setValue(messages);
    }
    return uploadResp;
  }

  public GridFsResource download(Long petId) {
    var petOptional = petRepository.findById(petId);
    if (!petOptional.isPresent()) {
      return null;
    }
    Pet pet = petOptional.get();
    List<String> photoFiles = pet.getPhotoFiles();
    if (null == photoFiles) {
      return null;
    }
    // for now get the last image
    String mongoFileId = photoFiles.get(photoFiles.size()-1);
    var mongoObjectId = new ObjectId(mongoFileId);
    return gridFsOperations.getResource(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(mongoObjectId))));
  }

  private Long updatePetInDB(Pet pet) {
    Long docId = pet.getId();
    try {
      petRepository.save(pet);
    } catch (Exception e) {
      logger.severe(e.getMessage());
      return null;
    }
    return docId;
  }

  private UUID createPetInDB(Pet pet) {
    UUID uuid = UUID.randomUUID();

    pet.setId(Math.abs(uuid.getLeastSignificantBits()));
    try {
      petRepository.save(pet);
    } catch (Exception e) {
      logger.severe(e.getMessage());
      return null;
    }
    return uuid;
  }
}
