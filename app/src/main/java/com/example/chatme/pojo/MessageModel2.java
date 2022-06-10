package com.example.chatme.pojo;

import java.io.Serializable;


public class MessageModel2 implements Serializable {

    private String id , message , image ;
    private Long timeStampValue;
    private boolean read;

    public MessageModel2(){


    }

    public MessageModel2(String id , String message, String image, Long timeStampValue , boolean read) {
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


    public Long getTimeStampValue() {
        return timeStampValue;
    }

    public void setTimeStampValue(long timeStampValue) {
        this.timeStampValue = timeStampValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
