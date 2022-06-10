package com.example.chatme.ui.auth;



import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;


public class AuthRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private FirebaseAuth firebaseAuth;



    @Inject
    public AuthRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;

    }

    //Get Firebase Auth Instance
    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }
    //Get Firebase User
    public FirebaseUser getFirebaseUser(){
        return firebaseAuth.getCurrentUser();
    }
    //Get Firebase User Id
    public String getFirebaseUserId(){
        return getFirebaseUser().getUid();
    }
    //Set Auth Language Key
    public void setAuthLanguageKey(String key){
        firebaseAuth.setLanguageCode(key);
    }
    //Get Firebase User Phone Number
    public String getAuthPhoneNumber(){
        return firebaseAuth.getCurrentUser().getPhoneNumber();
    }



    @Override
    public void run() throws Throwable {

    }
}
