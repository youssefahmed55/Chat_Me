package com.example.chatme.ui.api;


import android.util.Log;


import androidx.lifecycle.ViewModel;

import com.example.chatme.pojo.notification.RootModel;


import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class MyApiViewModel extends ViewModel {
    private static final String TAG = "MyApiViewModel";
    private MyApiRepo myApiRepo;
    private final CompositeDisposable compositeDisposableSendNotification = new CompositeDisposable();

    @Inject
    public MyApiViewModel(MyApiRepo myApiRepo) {
        this.myApiRepo = myApiRepo;

    }
    //Send Notification
    public void sendNotification(RootModel rootModel){

        compositeDisposableSendNotification.add(myApiRepo.sendNotification(rootModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o-> Log.d(TAG, "sendNotification: "+ o.string()),e-> Log.d(TAG, "sendNotification: " +  e.getMessage())));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposableSendNotification.clear();
    }
}



