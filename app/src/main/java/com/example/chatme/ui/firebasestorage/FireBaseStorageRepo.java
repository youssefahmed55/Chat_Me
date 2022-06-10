package com.example.chatme.ui.firebasestorage;



import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;


public class FireBaseStorageRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private StorageReference myStorageRef;



    @Inject
    public FireBaseStorageRepo(StorageReference myStorageRef) {
        this.myStorageRef = myStorageRef;

    }

   //Get Firebase Storage Instance Reference
   public StorageReference getFireBaseStorage(){
        return myStorageRef;
   }


    @Override
    public void run() throws Throwable {

    }
}
