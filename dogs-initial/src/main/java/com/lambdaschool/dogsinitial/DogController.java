package com.lambdaschool.dogsinitial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/dogs")
public class DogController
{

    //Required for logging
    private static final Logger logger = LoggerFactory.getLogger(DogController.class);

    @Autowired
    RabbitTemplate rt;

    //Time stamp
    Date timeStamp = new Date();

    // localhost:8080/dogs/dogs
    @GetMapping(value = "/dogs")
    public ResponseEntity<?> getAllDogs()
    {
        logger.info("/dogs accessed");

        MessageDetail message = new MessageDetail("/dogs accessed", String.valueOf(timeStamp), null);
        rt.convertAndSend(DogsinitialApplication.QUEUE_NAME_LOW, message);

        return new ResponseEntity<>(DogsinitialApplication.ourDogList.dogList, HttpStatus.OK);
    }

    // localhost:8080/dogs/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDogDetail(@PathVariable long id)
    {
        logger.info("dogs/" + id + " accessed");

        MessageDetail message = new MessageDetail("dogs/" + id + " accessed", String.valueOf(timeStamp), String.valueOf(id));
        rt.convertAndSend(DogsinitialApplication.QUEUE_NAME_LOW, message);

        Dog rtnDog = DogsinitialApplication.ourDogList.findDog(d -> (d.getId() == id));
        return new ResponseEntity<>(rtnDog, HttpStatus.OK);
    }

    // localhost:8080/dogs/breeds/{breed}
    @GetMapping(value = "/breeds/{breed}")
    public ResponseEntity<?> getDogBreeds (@PathVariable String breed)
    {
        logger.info("/breeds/" + breed + " accessed");

        MessageDetail message = new MessageDetail("/breeds/" + breed + " accessed", String.valueOf(timeStamp), String.valueOf(breed));
        rt.convertAndSend(DogsinitialApplication.QUEUE_NAME_LOW, message);

        ArrayList<Dog> rtnDogs = DogsinitialApplication.ourDogList.
                findDogs(d -> d.getBreed().toUpperCase().equals(breed.toUpperCase()));
        return new ResponseEntity<>(rtnDogs, HttpStatus.OK);
    }
}