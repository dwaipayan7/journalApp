package com.dwaipayan.journalApp.service;

import com.dwaipayan.journalApp.entity.User;
import com.dwaipayan.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
