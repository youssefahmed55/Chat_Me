package com.example.chatme.ui.firebasestorage;

import android.net.Uri;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FireBaseStorageViewModel extends ViewModel {
    private FireBaseStorageRepo fireBaseStorageRepo;
    private final MutableLiveData<String> mutableLiveDataUri = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveImageUri = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveNewProfileUri = new MutableLiveData<>();

    @Inject
    public FireBaseStorageViewModel(FireBaseStorageRepo fireBaseStorageRepo) {
        this.fireBaseStorageRepo = fireBaseStorageRepo;
    }
    public MutableLiveData<String> getMutableLiveDataUri() {
        return mutableLiveDataUri;
    }

    public MutableLiveData<String> getMutableLiveImageUri() {
        return mutableLiveImageUri;
    }

    public MutableLiveData<String> getMutableLiveNewProfileUri() {
        return mutableLiveNewProfileUri;
    }
    //Save Image
    public void saveImage(Uri uri , String fileExtension) {
        final StorageReference filerefrence = fireBaseStorageRepo.getFireBaseStorage().child(System.currentTimeMillis() + "." + fileExtension);
        filerefrence.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filerefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mutableLiveDataUri.setValue(uri.toString());
                    }
                });
            }
        });

    }
    //Save Image
    public void saveImageInFirebase(String fileExtension , Uri uri){
        final StorageReference fileReference = fireBaseStorageRepo.getFireBaseStorage().child(System.currentTimeMillis() + "." + fileExtension);
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mutableLiveImageUri.setValue(uri.toString());
                    }
                });
            }
        });


    }
    //Save New Profile Image
    public void saveNewProfileImage(String fileExtension ,Uri uri){

        final StorageReference fileReference = fireBaseStorageRepo.getFireBaseStorage().child(System.currentTimeMillis() + "." + fileExtension);
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mutableLiveNewProfileUri.setValue(uri.toString());
                    }
                });
            }
        });


    }

}