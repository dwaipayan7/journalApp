package com.dwaipayan.journalApp.controller;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController

@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    JournalEntryService journalEntryService;

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public ResponseEntity<?> getAll(){

        List<JournalEntry> all = journalEntryService.getAll();

        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){


        try {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry);
            return  new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        }catch (Exception e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){

        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);

        if (journalEntry.isPresent()){
            return  new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId){

        journalEntryService.deleteById(myId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable("id") String id,
                                          @RequestBody JournalEntry myEntry) {
        // Convert String id to ObjectId if necessary (depends on your setup)
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ObjectId format: " + id);
        }

        // Fetch the old journal entry by id
        JournalEntry old = journalEntryService.findById(objectId).orElse(null);
        if (old == null) {
            throw new RuntimeException("Journal entry not found with id: " + id);
        }

        // Update fields if they are provided
        old.setTitle(
                myEntry.getTitle() != null && !myEntry.getTitle().trim().isEmpty()
                        ? myEntry.getTitle()
                        : old.getTitle()
        );
        old.setContent(
                myEntry.getContent() != null && !myEntry.getContent().trim().isEmpty()
                        ? myEntry.getContent()
                        : old.getContent()
        );

        // Save the updated entry
        journalEntryService.saveEntry(old);

        if (old != null){
            return new ResponseEntity<>(old, HttpStatus.OK);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
