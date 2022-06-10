package com.example.chatme.ui.firebasedatabase;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;

import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FirebaseDataBaseModule {

    @Provides
    @Singleton
    public static DatabaseReference myDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference("Chat Me");
    }


}
