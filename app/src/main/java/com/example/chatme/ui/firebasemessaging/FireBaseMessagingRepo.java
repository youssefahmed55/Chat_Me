package com.example.chatme.ui.firebasemessaging;


import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;


public class FireBaseMessagingRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private FirebaseMessaging myFirebaseMessaging;



    @Inject
    public FireBaseMessagingRepo(FirebaseMessaging myFirebaseMessaging) {
        this.myFirebaseMessaging = myFirebaseMessaging;

    }

    //Get Firebase Messaging Instance
    public FirebaseMessaging getMyFirebaseMessaging() {
        return myFirebaseMessaging;
    }

    @Override
    public void run() throws Throwable {

    }
}
