package com.example.chatme.pojo;

import java.util.ArrayList;

public class ChatsModel {
    private ContactModel contactModel;
    private ArrayList<MessageModel2> arrayListmessageModel;
    private int count;

    public ChatsModel(){


    }

    public ChatsModel(ContactModel contactModel,ArrayList<MessageModel2> arrayListmessageModel, int count) {
        this.contactModel = contactModel;
        this.arrayListmessageModel = arrayListmessageModel;
        this.count = count;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }

    public ArrayList<MessageModel2> getArrayListmessageModel() {
        return arrayListmessageModel;
    }

    public void setArrayListmessageModel(ArrayList<MessageModel2> arrayListmessageModel) {
        this.arrayListmessageModel = arrayListmessageModel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
