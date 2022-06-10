package com.example.chatme.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;


import com.bumptech.glide.Glide;

import com.example.chatme.databinding.ActivityMyProfileBinding;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.firebasestorage.FireBaseStorageViewModel;



import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyProfile extends AppCompatActivity {
    private ActivityMyProfileBinding binding;
    private View view;
    private AuthViewModel authViewModel;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private FireBaseStorageViewModel fireBaseStorageViewModel;
    private ContactModel contactModell;
    private Uri uri;
    private final int IMAGE_REQUEST_CODE = 1;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        inti();                      //Initialize variables
        onClickonBackIcon();         //Set On CLick On Back Icon
        if (isNetworkAvailable()) {
            observeFireBaseUserId(); //Observe Firebase User Id
            onClickOnImageCard();    //Set On Click On Image Card

        }

    }

    private void observeFireBaseUserId() {
        authViewModel.getFirebaseAuthId();
        authViewModel.getMutableLiveDataUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                id = s ;
                observeMyProfile();             //Observe my Profile
                textChangeListenerOfNickname(); //Text Change Listener To Nickname EditText
                textChangeListenerOfStatus();   //Text Change Listener To Status EditText
                observeProfileImageChanged();   //Observe New Profile Image If changed
            }
        });
    }

    private void onClickonBackIcon() {
        binding.myProfileBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void observeProfileImageChanged() {
        fireBaseStorageViewModel.getMutableLiveNewProfileUri().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fireBaseDataBaseViewModel.saveNewUriProfileImage(s,id);
            }
        });
    }

    private void onClickOnImageCard() {
        binding.myProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE);  //Get Image From Gallery
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== RESULT_OK && data != null && data.getData() != null){
                uri = data.getData();
                binding.myProfileProfileImage.setImageURI(uri);
                saveNewProfileImage(uri);
            }

        }
    }
    private  String getfileextention(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));

    }
    private void saveNewProfileImage(Uri uri) {
         if(isNetworkAvailable()){
             fireBaseStorageViewModel.saveNewProfileImage(getfileextention(uri),uri);
         }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void textChangeListenerOfStatus() {
        binding.myProfileStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!binding.myProfileStatus.getText().toString().trim().equals(contactModell.getAbout())) {
                    fireBaseDataBaseViewModel.saveNewStatusName(binding.myProfileStatus.getText().toString().trim(), id);
                }
            }
        });

    }

    private void textChangeListenerOfNickname() {
        binding.myProfileNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(contactModell.getNickname())) {
                fireBaseDataBaseViewModel.saveNewNickName(editable.toString(), id);
                }
            }
        });
    }

    private void inti() {
        //Initialize authViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        //Initialize fireBaseDataBaseViewModel
        fireBaseDataBaseViewModel= new ViewModelProvider(this).get(FireBaseDataBaseViewModel.class);
        //Initialize fireBaseStorageViewModel
        fireBaseStorageViewModel = new ViewModelProvider(this).get(FireBaseStorageViewModel.class);
    }

    private void observeMyProfile() {
        fireBaseDataBaseViewModel.getMyContactProfile(id);
        fireBaseDataBaseViewModel.getMutableLiveDataMyContact().observe(this, new Observer<ContactModel>() {
            @Override
            public void onChanged(ContactModel contactModel) {
                contactModell = contactModel;
                setProfileInfo(contactModell);

            }
        });
    }

    private void setProfileInfo(ContactModel contactModel) {
        Glide.with(this).load(contactModel.getImage()).into(binding.myProfileProfileImage);
        binding.myProfileNumber.setText("+ " + contactModel.getNum());
        binding.myProfileNickName.setText(contactModel.getNickname());
        binding.myProfileStatus.setText(contactModel.getAbout());
    }
}