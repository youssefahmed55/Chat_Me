package com.example.chatme.ui.auth;



import com.google.firebase.auth.FirebaseAuth;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MyAuth {


    @Provides
    @Singleton
    public static FirebaseAuth myFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }








}
