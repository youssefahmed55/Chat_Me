package com.example.chatme.pojo;



import java.io.Serializable;
import java.util.ArrayList;

public class ChatMessagesReceivedModel implements Serializable {
    private ArrayList<ChatItemModel> Chats;
    private ContactModel contactModel;


    public ChatMessagesReceivedModel(ArrayList<ChatItemModel> chats, ContactModel contactModel) {
        this.Chats = chats;
        this.contactModel = contactModel;
    }

    public ArrayList<ChatItemModel> getChats() {
        return Chats;
    }

    public void setChats(ArrayList<ChatItemModel> chats) {
        Chats = chats;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }
}
