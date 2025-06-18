package com.example.ecom_user_server.Model;

import jakarta.persistence.*;


@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String role;


    public User(){

    }
    public User(Long id, String username,String password, String name, String role){
        this.id=id;
        this.username=username;
        this.password=password;
        this.name=name;
        this.role=role;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long u_id){
        this.id=u_id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role) {
        this.role=role;
    }
}
