package com.example.chatme.ui.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.chatme.R;
import com.example.chatme.databinding.FragmentSignInBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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


    private FragmentSignInBinding binding;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        editSpinner();         //Edit Spinner
        onClickOnNextButton(); //Set On Click On Next Button
        onClickBack();         //Set On Click On Back Button
        return view;
    }

    private void onClickBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish(); //Finish Activity
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void editSpinner() {
        final List<String> list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.DialingCountryCode)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item,list){
            @Override
            public boolean isEnabled(int position) {
                    String s[] = list.get(position).split(",");
                    binding.SignEdittext1.setText(s[0].substring(1));
                    binding.SignSpinner.setSelection(position);
                    return true;
            }

        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item2);       //Set Drop Down View Resource To Spinner
        binding.SignSpinner.setAdapter(spinnerArrayAdapter);                        //Set Spinner Adapter
    }

    private void onClickOnNextButton() {
        binding.SignNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    if(binding.SignEdittext2.getText().toString().trim().equals("")){
                        Toast.makeText(getActivity(), getString(R.string.Write_Full_Number_First_Please), Toast.LENGTH_SHORT).show();
                    }else {
                        String number = binding.SignEdittext1.getText().toString().trim() + binding.SignEdittext2.getText().toString().trim();
                        Bundle bundle = new Bundle();
                        bundle.putString("num", number);
                        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_verifyCodeFragment, bundle);
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.Internet_Not_Connected), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}