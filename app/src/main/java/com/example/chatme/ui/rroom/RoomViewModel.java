package com.example.chatme.ui.rroom;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatme.pojo.ChatItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RoomViewModel extends ViewModel {
    private static final String TAG = "RoomViewModel";
    private final RoomRepo roomRepo;
    private final MutableLiveData<List<ChatItemModel>> MutableLiveDataChats = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposableChats = new CompositeDisposable();

    @Inject
    public RoomViewModel(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    public MutableLiveData<List<ChatItemModel>> getMutableLiveDataChats() {
        return MutableLiveDataChats;
    }
    //Get All Chats
    public void getMyChats(){


        compositeDisposableChats.add(roomRepo.getAllChats().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o-> MutableLiveDataChats.setValue(o),e-> Log.d(TAG, "getMyChats: " + e.getMessage())));
    }

    //Delete All Chats Then Insert Chats
    public void deleteAndInsert(ArrayList<ChatItemModel> chatItemModelList){

            roomRepo.deleteAllChats().andThen(roomRepo.insert(chatItemModelList)).subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: delete&insert");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }
    //Delete All Chats
    public void deleteAll(){

        roomRepo.deleteAllChats().subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: deleteAll");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposableChats.clear();
    }
}
