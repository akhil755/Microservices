package com.example.ecom_user_server.service;

import com.example.ecom_user_server.Model.User;
import com.example.ecom_user_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not found"));
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User updateUserById(Long id, User user) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            User originalUser = byId.get();
            if (Objects.nonNull(user.getName())&&!"".equalsIgnoreCase(user.getName())){
                originalUser.setName(user.getName());
            }
            return userRepository.save(originalUser);
        }
        return null;
    }
}
