package com.example.chatme.ui.firebasemessaging;


import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MyFirebaseMessagingModule {


    @Provides
    @Singleton
    public static FirebaseMessaging myFirebaseMessaging(){
        return FirebaseMessaging.getInstance();
    }








}
