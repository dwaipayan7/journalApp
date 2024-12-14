package com.dwaipayan.journalApp.service;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

}



//controller --> service --> repository