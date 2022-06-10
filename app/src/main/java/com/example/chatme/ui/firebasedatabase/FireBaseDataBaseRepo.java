package com.example.chatme.ui.firebasedatabase;




import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;


public class FireBaseDataBaseRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private DatabaseReference myRef;




    @Inject
    public FireBaseDataBaseRepo(DatabaseReference myRef) {
        this.myRef = myRef;

    }

   //Get FireBaseDataBase Instance Reference
   public DatabaseReference getFireBaseDataBase(){
        return myRef;
   }


    @Override
    public void run() throws Throwable {

    }
}
