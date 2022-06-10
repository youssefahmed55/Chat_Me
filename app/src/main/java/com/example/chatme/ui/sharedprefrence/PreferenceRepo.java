package com.example.chatme.ui.sharedprefrence;


import android.content.SharedPreferences;


import com.example.chatme.pojo.ContactModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;



public class PreferenceRepo implements @NonNull Action {
    private static final String TAG = "Repositary";

    private SharedPreferences sharedPreferences;



    @Inject
    public PreferenceRepo(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

    }

   //Set Boolean If Still Sign In
   public void setBooleanSharedPreferences(boolean bool) {
        sharedPreferences.edit().putBoolean("key",bool).apply();
   }


    //Get Boolean Of Sign In
    public boolean getBooleanSharedPreferences(){
        return sharedPreferences.getBoolean("key",false);
    }

    //Save Contacts in SharedPreference To Use Them With FCM
    public void setArrayOfContactsSharedPreferences(ArrayList<ContactModel> contactModelArrayList){
        Gson gson = new Gson();
        String json = gson.toJson(contactModelArrayList);
        sharedPreferences.edit().putString("contacts",json).apply();
    }

    //Get Contacts From SharedPreferences
    public ArrayList<ContactModel> getArrayOfContactsSharedPreferences(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contacts", null);

        Type type = new TypeToken<ArrayList<ContactModel>>() {}.getType();
        //Type type2 = TypeToken.getParameterized(ArrayList.class, String.class).getType();
        if (gson.fromJson(json, type) != null) {
            return gson.fromJson(json, type);
        }else
        return null;
    }
    //Delete Contacts
    public void deleteArrayOfContactsSharedPreferences(){
        sharedPreferences.edit().putString("contacts","").apply();
    }


    @Override
    public void run() throws Throwable {

    }
}
