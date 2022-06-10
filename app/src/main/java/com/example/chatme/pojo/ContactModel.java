package com.example.chatme.pojo;

import java.io.Serializable;

public class ContactModel implements Serializable {
    private String id , image ,name , nickname , about , num , token;

    public ContactModel(){


    }
    public ContactModel(String id , String image, String name, String nickname , String num, String about , String token) {
        this.id = id;
        this.image= image;
        this.name = name;
        this.nickname = nickname;
        this.num = num;
        this.about = about;
        this.token = token;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
