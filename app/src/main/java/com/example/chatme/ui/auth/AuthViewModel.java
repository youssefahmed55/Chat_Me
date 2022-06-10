package com.example.chatme.ui.auth;



import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;


import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";
    private AuthRepo authRepo;
    private final MutableLiveData<Boolean> mutableLiveDataSignCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataUserId = new MutableLiveData<>();
    private final MutableLiveData<FirebaseAuth> mutableLiveDataFireBaseAuth = new MutableLiveData<>();
    private final MutableLiveData<FirebaseUser> mutableLiveDataFireBaseUser = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataPhoneNumber = new MutableLiveData<>();

    @Inject
    public AuthViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;

    }

    public MutableLiveData<String> getMutableLiveDataUserId() {
        return mutableLiveDataUserId;
    }



    public MutableLiveData<Boolean> getMutableLiveDataSignCompleted() {
        return mutableLiveDataSignCompleted;
    }

    public MutableLiveData<FirebaseAuth> getMutableLiveDataFireBaseAuth() {
        return mutableLiveDataFireBaseAuth;
    }

    public MutableLiveData<FirebaseUser> getMutableLiveDataFireBaseUser() {
        return mutableLiveDataFireBaseUser;
    }

    public MutableLiveData<String> getMutableLiveDataPhoneNumber() {
        return mutableLiveDataPhoneNumber;
    }

    public void getAuthPhoneNum(){
        mutableLiveDataPhoneNumber.setValue(authRepo.getAuthPhoneNumber());
    }

    public void getFirebaseAuth(){
        mutableLiveDataFireBaseAuth.setValue(authRepo.getFirebaseAuth());
    }

    public void getFirebaseAuthId(){
        mutableLiveDataUserId.setValue(authRepo.getFirebaseUserId());
    }

    public void getFirebaseUser(){
        mutableLiveDataFireBaseUser.setValue(authRepo.getFirebaseUser());

    }
    //Set Auth Language Key
    public void setAuthLangKey(String key){
        authRepo.setAuthLanguageKey(key);
    }
    //Sign In With phone Auth Credential
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        authRepo.getFirebaseAuth().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   mutableLiveDataSignCompleted.setValue(true);
               }else {
                   mutableLiveDataSignCompleted.setValue(false);
               }
            }
        });


    }
    // Auth Sign Out
    public void authSignOut(){
        authRepo.getFirebaseAuth().signOut();
    }


    }



