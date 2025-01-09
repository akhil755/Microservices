package com.example.ecom_user_server.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long u_id;
    private String name;

    public User(){

    }
    public User(Long u_id, String name){
        this.u_id=u_id;
        this.name=name;
    }

    public Long getU_id(){
        return u_id;
    }
    public void setU_id(Long u_id){
        this.u_id=u_id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
}
