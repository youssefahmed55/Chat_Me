package com.example.chatme.pojo;

import java.util.Map;

public class MessageModel {

    private String id , message , image ;
    private Map<String, String> timeStampValue;
    private boolean read;

    public MessageModel(){


    }

    public MessageModel(String id , String message, String image, Map<String, String> timeStampValue ,boolean read) {
        this.id = id;
        this.message = message;
        this.image = image;
        this.timeStampValue = timeStampValue;
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }



    public Map<String, String> getTimeStampValue() {
        return timeStampValue;
    }

    public void setTimeStampValue(Map<String, String> timeStampValue) {
        this.timeStampValue = timeStampValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
