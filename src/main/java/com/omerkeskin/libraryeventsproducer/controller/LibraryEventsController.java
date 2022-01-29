package com.omerkeskin.libraryeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omerkeskin.libraryeventsproducer.domain.LibraryEvent;
import com.omerkeskin.libraryeventsproducer.domain.LibraryEventType;
import com.omerkeskin.libraryeventsproducer.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    private LibraryEventProducer libraryEventProducer;

    @PostMapping("v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
       // invoke kafka producer
        log.info("Before postLibraryEvent ");
        //libraryEventProducer.sendLibraryEvent(libraryEvent);
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        libraryEventProducer.sendLibraryEventApproach2(libraryEvent);
        //SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSync(libraryEvent);
        //log.info("Send result {} ", sendResult.toString());
        log.info("After postLibraryEvent ");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }


    @PutMapping("v1/libraryevent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        if(libraryEvent.getLibraryEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }

        // invoke kafka producer
        log.info("Before putLibraryEvent ");
        //libraryEventProducer.sendLibraryEvent(libraryEvent);
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        libraryEventProducer.sendLibraryEventApproach2(libraryEvent);
        //SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSync(libraryEvent);
        //log.info("Send result {} ", sendResult.toString());
        log.info("After putLibraryEvent ");
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

}
