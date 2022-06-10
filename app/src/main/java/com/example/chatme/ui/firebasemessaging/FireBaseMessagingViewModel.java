package com.example.chatme.ui.firebasemessaging;


import android.util.Log;


import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;


import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FireBaseMessagingViewModel extends ViewModel {
    private static final String TAG = "FireBaseMessagingViewMo";
    private final FireBaseMessagingRepo fireBaseMessagingRepo;
    private final MutableLiveData<String> mutableLiveDataToken = new MutableLiveData<>();


    @Inject
    public FireBaseMessagingViewModel(FireBaseMessagingRepo fireBaseMessagingRepo) {
        this.fireBaseMessagingRepo = fireBaseMessagingRepo;
    }

    public MutableLiveData<String> getMutableLiveDataToken() {
        return mutableLiveDataToken;
    }
    //Get FireBase Token
    public void getFirebaseToken() {

        fireBaseMessagingRepo.getMyFirebaseMessaging().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        mutableLiveDataToken.setValue(task.getResult());
                        //Toast.makeText(getActivity(), token, Toast.LENGTH_SHORT).show();
                    }

                });

    }
}