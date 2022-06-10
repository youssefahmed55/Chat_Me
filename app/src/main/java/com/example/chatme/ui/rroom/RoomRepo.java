package com.example.chatme.ui.rroom;

import com.example.chatme.pojo.ChatItemModel;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Action;

public class RoomRepo implements @NonNull Action {
    private MyInterfaceDao myInterfaceDao;
    @Inject
    public RoomRepo(MyInterfaceDao myInterfaceDao) {
        this.myInterfaceDao = myInterfaceDao;

    }

    //Insert Chats
    public Completable insert (List<ChatItemModel> chatItemModelList){
        return myInterfaceDao.insertAll(chatItemModelList);
    }
    //Get All Chats
    public Single<List<ChatItemModel>> getAllChats(){
        return myInterfaceDao.getChats();
    }
    //Delete All Chats
    public Completable deleteAllChats(){
        return  myInterfaceDao.deleteAllChats();
    }

    @Override
    public void run() throws Throwable {

    }

}
