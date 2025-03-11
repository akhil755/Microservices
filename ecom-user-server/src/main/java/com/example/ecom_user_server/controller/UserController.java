package com.example.ecom_user_server.controller;

import com.example.ecom_user_server.Model.User;
import com.example.ecom_user_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/test")
    public String testEndpoint(){
        return "Hello from ecom-user-server instance";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PutMapping("/user/{id}")
    public User updateUserById(@PathVariable ("id") Long id, @RequestBody User user){
        return userService.updateUserById(id,user);
    }
}
