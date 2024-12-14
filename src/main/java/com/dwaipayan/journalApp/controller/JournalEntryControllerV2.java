package com.dwaipayan.journalApp.controller;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        return null;

    }


    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry){

        journalEntryService.saveEntry(myEntry);
        return true;

    }

    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable Long myId){
        return null;
    }

    @DeleteMapping("id/{myId}")
    public JournalEntry deleteById(@PathVariable Long myId){
        return null;
    }

    @PutMapping("id/")
    public JournalEntry updateJournalById(@PathVariable Long myId, @RequestBody JournalEntry myEntry){

        return null;

    }

}
