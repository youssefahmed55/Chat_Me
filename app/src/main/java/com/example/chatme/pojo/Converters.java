package com.example.chatme.pojo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {
    @TypeConverter
    public String fromlisttoString(List<MessageModel2> value) {
        return new Gson().toJson(value);
    }
    @TypeConverter
    public List<MessageModel2> fromStringtolist(String s) {
        return new Gson().fromJson(s,new TypeToken<List<MessageModel2>>() {}.getType());
    }

    @TypeConverter
    public String fromlisttoString2(ContactModel contactModel) {
        return new Gson().toJson(contactModel);
    }
    @TypeConverter
    public ContactModel fromStringtolist2(String s) {
        return new Gson().fromJson(s,new TypeToken<ContactModel>() {}.getType());
    }
}
