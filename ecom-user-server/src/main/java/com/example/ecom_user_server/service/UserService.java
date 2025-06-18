package com.example.ecom_user_server.service;

import com.example.ecom_user_server.Model.User;
import com.example.ecom_user_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not found"));
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserById(Long id, User user) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            User originalUser = byId.get();
            if (Objects.nonNull(user.getName())&&!"".equalsIgnoreCase(user.getName())){
                originalUser.setName(user.getName());
            }
            return userRepository.save(originalUser);
        }else{
            throw new RuntimeException("User not found with ID : "+id);
        }
    }

    public User registerNewUser(String username, String rawPassword, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword)); // ENCODE THE PASSWORD!
        newUser.setRole(role);
        return userRepository.save(newUser);
    }
}
