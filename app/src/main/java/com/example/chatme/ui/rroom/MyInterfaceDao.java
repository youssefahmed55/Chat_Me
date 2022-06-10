package com.example.chatme.ui.rroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.chatme.pojo.ChatItemModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface MyInterfaceDao {
    //Get All Chats
    @Query("select * from myTable")
    public Single<List<ChatItemModel>> getChats();
    //Insert Chats
    @Insert
    public Completable insertAll(List<ChatItemModel> chatItemModelList);
    //Delete All Chats
    @Query("Delete FROM myTable")
    public Completable deleteAllChats();

}
