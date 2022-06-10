package com.example.chatme.ui.firebasestorage;



import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FireBaseStorageModule {

    @Provides
    @Singleton
    public static StorageReference myStorageReference(){
        return FirebaseStorage.getInstance().getReference();
    }
}
