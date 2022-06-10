package com.example.chatme.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatme.R;
import com.example.chatme.databinding.FragmentVerifyCodeBinding;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.HomeActivity;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.sharedprefrence.PreferenceViewModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyCodeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerifyCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyCodeFragment newInstance(String param1, String param2) {
        VerifyCodeFragment fragment = new VerifyCodeFragment();
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

    private static final String TAG = "VerifyCodeFragment";
    private FragmentVerifyCodeBinding binding;
    private View view;
    private AuthViewModel authViewModel;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private PreferenceViewModel preferenceViewModel;
    private String sentCode , enterCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText[] editTextsArray = new EditText[6] ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVerifyCodeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        inti();                      //Initialize variables
        addOnTextChange();           //Text Change Listener To EditTexts
        if (phoneNumber != null) {
            sendCode();              //Send Code
            observeSignIn();         //Observe sign In
            onClickOnConfirmButton();//Set On Click On Confirm Button
            onClickOnReSendButton(); //Set On Click On ReSend Button
        }




        return view;
    }


    private void sendCode() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                authViewModel.signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_LONG).show();

                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                sentCode = verificationId;
            }
        };
                authViewModel.setAuthLangKey("fr");
                authViewModel.getFirebaseAuth();
                authViewModel.getMutableLiveDataFireBaseAuth().observe(getViewLifecycleOwner(), new Observer<FirebaseAuth>() {
                    @Override
                    public void onChanged(FirebaseAuth firebaseAuth) {
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(firebaseAuth)
                                        .setPhoneNumber("+"+phoneNumber)       // Phone number to verify
                                        .setTimeout(10L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(getActivity())                 // Activity (for callback binding)
                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);
                    }
                });







    }

    private void addOnTextChange() {
        editTextsArray[0] = binding.VerifyEdittext1;
        editTextsArray[1] = binding.VerifyEdittext2;
        editTextsArray[2] = binding.VerifyEdittext3;
        editTextsArray[3] = binding.VerifyEdittext4;
        editTextsArray[4] = binding.VerifyEdittext5;
        editTextsArray[5] = binding.VerifyEdittext6;

        for(int j = 0 ; j < editTextsArray.length - 1;j++){
            int J = j;
            editTextsArray[j].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.length() != 0){
                        editTextsArray[J + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }

    private void onClickOnReSendButton() {
        binding.VerifyReSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
    }

    private void onClickOnConfirmButton() {
        binding.VerifyConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySignInCode();
            }
        });
    }

    private void observeSignIn() {
        authViewModel.getMutableLiveDataSignCompleted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    observeUsersFromFireBaseDataBase(); //Observe user From FireBase DataBase
                }else {
                    Toast.makeText(getActivity(), "Error Sign In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void observeUsersFromFireBaseDataBase() {
           fireBaseDataBaseViewModel.getUsersFromFirebaseDataBase();
           fireBaseDataBaseViewModel.getArrayListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ContactModel>>() {
               @Override
               public void onChanged(ArrayList<ContactModel> contactModels) {
                   authViewModel.getFirebaseAuthId();
                   authViewModel.getMutableLiveDataUserId().observe(getViewLifecycleOwner(), new Observer<String>() {
                       @Override
                       public void onChanged(String authid) {
                           if(contactModels.size() >0){
                               Log.d(TAG, "onChanged: " +contactModels.size());
                               for(int i = 0 ; i < contactModels.size() ; i++){
                                   if(authid.equals(contactModels.get(i).getId())){
                                       preferenceViewModel.setBoolSharedPreferencesValue(true);
                                       Intent intent = new Intent(getActivity(),HomeActivity.class);
                                       startActivity(intent);
                                       getActivity().finish();
                                       break;
                                   }
                                   if(!authid.equals(contactModels.get(i).getId())&&( i == contactModels.size() - 1)){
                                       Bundle bundle = new Bundle();
                                       bundle.putString("num", phoneNumber);
                                       Navigation.findNavController(view).navigate(R.id.action_verifyCodeFragment_to_newProfileFragment, bundle);
                                       break;
                                   }
                               }

                           }else {
                               Bundle bundle = new Bundle();
                               bundle.putString("num", phoneNumber);
                               Navigation.findNavController(view).navigate(R.id.action_verifyCodeFragment_to_newProfileFragment, bundle);
                           }
                       }
                   });

               }
           });
    }


    private void inti() {
        //Initialize authViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        //Initialize fireBaseDataBaseViewModel
        fireBaseDataBaseViewModel= new ViewModelProvider(requireActivity()).get(FireBaseDataBaseViewModel.class);
        //Initialize preferenceViewModel
        preferenceViewModel = new ViewModelProvider(requireActivity()).get(PreferenceViewModel.class);

    }


    private boolean isCodeEnteredFull() {
        if(!binding.VerifyEdittext1.getText().toString().trim().equals("")
                &&!binding.VerifyEdittext2.getText().toString().trim().equals("")
                &&!binding.VerifyEdittext3.getText().toString().trim().equals("")
                &&!binding.VerifyEdittext4.getText().toString().trim().equals("")
                &&!binding.VerifyEdittext5.getText().toString().trim().equals("")
                &&!binding.VerifyEdittext6.getText().toString().trim().equals("")
        ) {
            enterCode = binding.VerifyEdittext1.getText().toString().trim()
                    + binding.VerifyEdittext2.getText().toString().trim()
                    + binding.VerifyEdittext3.getText().toString().trim()
                    + binding.VerifyEdittext4.getText().toString().trim()
                    + binding.VerifyEdittext5.getText().toString().trim()
                    + binding.VerifyEdittext6.getText().toString().trim();
            return true;
        }
        return false;
    }




    private void  verifySignInCode(){
        if (sentCode != null) {
            if (isCodeEnteredFull()){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentCode, enterCode);
                authViewModel.signInWithPhoneAuthCredential(credential);
            }else{
                Toast.makeText(getActivity(), "Write Full Verify Code", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "SentCode is Empty", Toast.LENGTH_SHORT).show();

        }
    }

}