package com.dwaipayan.journalApp.controller;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController

@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    JournalEntryService journalEntryService;

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll(){

        return journalEntryService.getAll();

    }


    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){

        myEntry.setDate(LocalDateTime.now());

        journalEntryService.saveEntry(myEntry);
        return myEntry;

    }

    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId){


        return journalEntryService.findById(myId).orElse(null);
    }

    @DeleteMapping("id/{myId}")
    public boolean deleteById(@PathVariable ObjectId myId){

        journalEntryService.deleteById(myId);
        return true;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalById(@PathVariable("id") String id,
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

        return old;
    }


}
