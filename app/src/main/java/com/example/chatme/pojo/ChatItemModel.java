package com.example.chatme.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;



import java.io.Serializable;

import java.util.List;

@Entity(tableName = "myTable")
public class ChatItemModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    private ContactModel contactModel;
    @ColumnInfo
    private String imageUrl;
    @ColumnInfo
    private String name ;
    @ColumnInfo
    private String lastmessage;
    @ColumnInfo
    private String time ;
    @ColumnInfo
    private List<MessageModel2> messageModel2ArrayList;

    @Ignore
    public ChatItemModel(){


    }

    public ChatItemModel(ContactModel contactModel,String imageUrl, String name, String lastmessage, String time,List<MessageModel2> messageModel2ArrayList) {
        this.contactModel = contactModel;
        this.imageUrl = imageUrl;
        this.name = name;
        this.lastmessage = lastmessage;
        this.time = time;
        this.messageModel2ArrayList = messageModel2ArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public List<MessageModel2> getMessageModel2ArrayList() {
        return messageModel2ArrayList;
    }

    public void setMessageModel2ArrayList(List<MessageModel2> messageModel2ArrayList) {
        this.messageModel2ArrayList = messageModel2ArrayList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
