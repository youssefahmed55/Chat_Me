package com.example.chatme.ui.login;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.chatme.databinding.FragmentNewProfileBinding;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.HomeActivity;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.firebasestorage.FireBaseStorageViewModel;
import com.example.chatme.ui.sharedprefrence.PreferenceViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewProfileFragment newInstance(String param1, String param2) {
        NewProfileFragment fragment = new NewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            phoneNumber = getArguments().getString("num");
        }
    }
    private FragmentNewProfileBinding binding;
    private View view;
    private AuthViewModel authViewModel;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private FireBaseStorageViewModel fireBaseStorageViewModel;
    private PreferenceViewModel preferenceViewModel;
    private final int IMAGE_REQUEST_CODE = 1;
    private final String URL_PROFILE_IMAGE = "https://firebasestorage.googleapis.com/v0/b/chat-me-d53bf.appspot.com/o/dPI.png?alt=media&token=b5c48100-b3ce-4009-bb04-e3dbc6e1507f";
    private Uri uri ;
    private String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        inti();                  //Initialize variables
        observeFirebaseUserId(); //Observe FireBase User Id
        onClickOnImageCard();    //Set On Click On Image Card
        onClickOnNextButton();   //Set On Click On Next Button
        observeSaveImage();      //Observe Save Image Of Profile
        return view;
    }

    private void observeFirebaseUserId() {
        authViewModel.getFirebaseAuthId();
        authViewModel.getMutableLiveDataUserId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                id = s;
            }
        });
    }


    private void observeSaveImage() {
        fireBaseStorageViewModel.getMutableLiveDataUri().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                observeSaveUser(s);
            }
        });
    }


    private void observeSaveUser(String uriImage) {
        if (id != null) {
            fireBaseDataBaseViewModel.saveNewProfile(new ContactModel(id
                    , uriImage
                    , ""
                    , binding.NewProfileNickname.getText().toString().trim()
                    , phoneNumber
                    , ""
                    , ""));
            fireBaseDataBaseViewModel.getIsNewProfileSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        preferenceViewModel.setBoolSharedPreferencesValue(true);
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
        }
    }

    private void onClickOnNextButton() {
        binding.NewProfileNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    if(!binding.NewProfileNickname.getText().toString().trim().equals("")) {
                        if(uri == null) {
                        observeSaveUser(URL_PROFILE_IMAGE);
                        }else {
                         fireBaseStorageViewModel.saveImage(uri,getFileExtension(uri));
                        }



                    }else {
                        Toast.makeText(getActivity(), "Please Write nick name", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Internet is Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  String getFileExtension(Uri uri){
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void onClickOnImageCard() {
        binding.NewProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE); //Get image From Gallery
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== RESULT_OK && data != null && data.getData() != null){
                uri = data.getData();                                  //Save uri Of Image
                binding.NewProfileProfileImage.setImageURI(uri);       //Show Image Selected
            }

        }
    }

    private void inti() {
        //Initialize authViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        //Initialize fireBaseDataBaseViewModel
        fireBaseDataBaseViewModel= new ViewModelProvider(requireActivity()).get(FireBaseDataBaseViewModel.class);
        //Initialize fireBaseStorageViewModel
        fireBaseStorageViewModel = new ViewModelProvider(requireActivity()).get(FireBaseStorageViewModel.class);
        //Initialize preferenceViewModel
        preferenceViewModel = new ViewModelProvider(requireActivity()).get(PreferenceViewModel.class);
    }
}