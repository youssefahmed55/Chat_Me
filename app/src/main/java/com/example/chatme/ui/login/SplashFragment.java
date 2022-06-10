package com.example.chatme.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatme.R;
import com.example.chatme.ui.HomeActivity;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.sharedprefrence.PreferenceViewModel;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View view;
    private PreferenceViewModel preferenceViewModel;
    private AuthViewModel authViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        inti();                                   //Initialize variables
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Observe FireBase User
                authViewModel.getFirebaseUser();
                authViewModel.getMutableLiveDataFireBaseUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser firebaseUser) {
                        observeUserCreatedProfile(firebaseUser); //Observe User Create Profile
                    }
                });

            }
        },3000);


        return view;
    }


    private void observeUserCreatedProfile(FirebaseUser user) {
        preferenceViewModel.getBoolSharedPreferencesValue();
        preferenceViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(user != null && aBoolean){
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_signInFragment);
                }
            }
        });
    }

    private void inti() {
        //Initialize preferenceViewModel
        preferenceViewModel = new ViewModelProvider(requireActivity()).get(PreferenceViewModel.class);
        //Initialize authViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }
}