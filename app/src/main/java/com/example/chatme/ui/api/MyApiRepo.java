package com.example.chatme.ui.api;

import com.example.chatme.pojo.notification.RootModel;


import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import okhttp3.ResponseBody;

public class MyApiRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private MyApiInterface myApiInterface;



    @Inject
    public MyApiRepo(MyApiInterface myApiInterface) {
        this.myApiInterface = myApiInterface;

    }

    //Send Notification
    public Observable<ResponseBody> sendNotification (RootModel rootModel){
        return myApiInterface.sendNotification(rootModel);
    }



    @Override
    public void run() throws Throwable {

    }
}
