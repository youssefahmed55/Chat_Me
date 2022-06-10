package com.example.chatme.ui.sharedprefrence;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatme.pojo.ContactModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PreferenceViewModel extends ViewModel {
    private PreferenceRepo preferenceRepo;
    private final MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();

    @Inject
    public PreferenceViewModel(PreferenceRepo preferenceRepo) {
        this.preferenceRepo = preferenceRepo;
    }

    public MutableLiveData<Boolean> getMutableLiveData() {
        return mutableLiveData;
    }

    //Set Boolean If Still Sign In
    public void setBoolSharedPreferencesValue(boolean bool){
        preferenceRepo.setBooleanSharedPreferences(bool);
    }
    //Get Boolean Of Sign In
    public void getBoolSharedPreferencesValue(){
        mutableLiveData.setValue(preferenceRepo.getBooleanSharedPreferences());
    }
    //Save Contacts in SharedPreference To Use Them With FCM
    public void saveContactsSharedPreferences(ArrayList<ContactModel> contactModelArrayList){
        preferenceRepo.setArrayOfContactsSharedPreferences(contactModelArrayList);
    }
    //Delete Contacts
    public void deleteContactsSharedPreferences(){
        preferenceRepo.deleteArrayOfContactsSharedPreferences();
    }

}
