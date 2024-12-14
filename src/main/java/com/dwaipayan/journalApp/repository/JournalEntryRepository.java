package com.dwaipayan.journalApp.repository;

import com.dwaipayan.journalApp.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String>{



}
