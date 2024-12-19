package com.dwaipayan.journalApp.controller;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.entity.User;
import com.dwaipayan.journalApp.service.JournalEntryService;
import com.dwaipayan.journalApp.service.UserService;
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

    @Autowired
    private UserService userService;

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping("{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username){

        User user = userService.findByUserName(username);
        List<JournalEntry> all = user.getJournalEntries();

        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,
                                                    @PathVariable String username) {
        if (myEntry.getTitle() == null || myEntry.getTitle().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            if (username == null || username.trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    @DeleteMapping("id/{username}/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId,@PathVariable String username){

        journalEntryService.deleteById(myId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{username}/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable("id") String id,
                                               @RequestBody JournalEntry myEntry,
                                               @PathVariable String username) {
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

        if (old != null) {
            return new ResponseEntity<>(old, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
