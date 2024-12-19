package com.dwaipayan.journalApp.service;

import com.dwaipayan.journalApp.entity.JournalEntry;
import com.dwaipayan.journalApp.entity.User;
import com.dwaipayan.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String username) {
        User user = userService.findByUserName(username);
        
        // If user doesn't exist, create a new one
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword("defaultPassword"); // You might want to handle this differently
        }
        
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        
        if (user.getJournalEntries() == null) {
            user.setJournalEntries(new ArrayList<>());
        }
        
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
    }

    public List<JournalEntry> getAll(){
       return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return  journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        journalEntryRepository.deleteById(id);
    }

}



//controller --> service --> repository