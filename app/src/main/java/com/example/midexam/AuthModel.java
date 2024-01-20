package com.example.midexam;

public class AuthModel {
    private int id;
    private String name;
    private String email;
    private String password;

    public AuthModel(Integer id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId(){
        return  id;
    }

    public String getName(){
        return name;
    }

    public  String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
