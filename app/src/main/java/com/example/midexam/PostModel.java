package com.example.midexam;

public class PostModel {
    private int id;
    private String content;
    private long authId;


    public PostModel(Integer id, String content,long authId){
        this.id = id;
        this.content = content;
        this.authId = authId;
    }

    public Integer getId(){
        return  id;
    }

    public String getContent(){
        return  content;
    }

    public Long getAuthId(){
        return authId;
    }
}
