package com.example.chatme.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chatme.databinding.ActivityUserProfileBinding;
import com.example.chatme.pojo.ContactModel;

public class UserProfile extends AppCompatActivity {
    private ActivityUserProfileBinding binding;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        ShowProfileInfo();   //Show Profile Info
        onClickOnBackIcon(); //Set On Click On Back Icon
    }

    private void onClickOnBackIcon() {
        binding.userProfileBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close/Finish Activity
            }
        });
    }

    private void ShowProfileInfo() {
       ContactModel contactModel = (ContactModel) getIntent().getSerializableExtra("model");
       Glide.with(this).load(contactModel.getImage()).into(binding.userProfileProfileImage);
       binding.userProfileName.setText(contactModel.getName());
       if (contactModel.getName().equals("+ " +contactModel.getNum()))
       binding.userProfileNumber.setText(contactModel.getNickname());
       else
       binding.userProfileNumber.setText("+ " +contactModel.getNum());
       binding.userProfileStatus.setText(contactModel.getAbout());
    }
}