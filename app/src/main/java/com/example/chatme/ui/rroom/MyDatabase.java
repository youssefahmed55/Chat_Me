package com.example.chatme.ui.rroom;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.Converters;


@Database(entities = ChatItemModel.class,version = 21 , exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyInterfaceDao myInterfaceDao();

}
