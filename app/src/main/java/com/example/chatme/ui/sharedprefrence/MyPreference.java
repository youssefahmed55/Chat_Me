package com.example.chatme.ui.sharedprefrence;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MyPreference {

    @Provides
    @Singleton
    public static SharedPreferences mySharedPrefernce(@ApplicationContext Context context){
        return context.getSharedPreferences("data",MODE_PRIVATE);
    }
}
