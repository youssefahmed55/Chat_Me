package com.example.chatme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.chatme.R;
import com.example.chatme.adapters.MyRecycleAdapterContacts;
import com.example.chatme.adapters.MyRecycleAdapterContactsFriendaInServer;
import com.example.chatme.databinding.ActivityContactsBinding;
import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.ChatMessagesReceivedModel;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.login.MainActivity;
import com.example.chatme.ui.rroom.RoomViewModel;
import com.example.chatme.ui.sharedprefrence.PreferenceViewModel;

import java.net.InetAddress;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private View view;
    private ArrayList<ContactModel> contactsArrayList;
    private ArrayList<ContactModel> simArrayList;
    private ArrayList<ContactModel> inviteArrayList;
    private MyRecycleAdapterContacts myRecycleAdapterContactsInvite;
    private MyRecycleAdapterContactsFriendaInServer myRecycleAdapterContactsSim;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private AuthViewModel authViewModel;
    private RoomViewModel roomViewModel;
    private PreferenceViewModel preferenceViewModel;
    private ArrayList<ChatItemModel> chatItemModelArrayList;
    private String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        displayBackIcon();             //Display Back Icon
        inti();                        //Initialize variables
        observeMyId();                 //Observe Auth Id
        observeSameAndInviteContacts();//Observe Same And Invite Contacts
        setOnclickOnItemToSRecycle();  //Set On Click On Same Contacts Recycler
        setOnclickOnItemToFRecycle();  //Set On Click On Invite Friend Recycler
        if (!isNetworkAvailable()&&!isInternetAvailable()) Toast.makeText(this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();

    }

    private void observeMyId() {
        authViewModel.getFirebaseAuthId();
        authViewModel.getMutableLiveDataUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                myId = s ;
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuu,menu);
        MenuItem menuItem = menu.findItem(R.id.searchIcon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty()) {
                    setAdapterSim(getSearchItem(s, simArrayList));
                    setAdapterInvite(getSearchItem(s, inviteArrayList));
                }else {
                    setAdapterSim(simArrayList);
                    setAdapterInvite(inviteArrayList);
                }

                return true;
            }
        });



        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.logout:
                preferenceViewModel.setBoolSharedPreferencesValue(false);
                authViewModel.authSignOut();
                roomViewModel.deleteAll();
                if (myId != null)
                fireBaseDataBaseViewModel.deleteAccessToken(myId);
                preferenceViewModel.deleteContactsSharedPreferences();
                Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.profile:
                if (!isNetworkAvailable()&&!isInternetAvailable())
                    Toast.makeText(this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();
                else {
                    Intent intent2 = new Intent(ContactsActivity.this, MyProfile.class);
                    startActivity(intent2);
                }

        }


        return super.onOptionsItemSelected(item);
    }
    private ArrayList<ContactModel> getSearchItem(String s, ArrayList<ContactModel> s2) {

        ArrayList<ContactModel> arrayList1 = new ArrayList<>();
        if (!s.equals(""))
            for (ContactModel e : s2) {
                if (e.getName().toLowerCase().charAt(0) == s.toLowerCase().charAt(0))
                    if (e.getName().toLowerCase().contains(s.toLowerCase())) {
                        arrayList1.add(e);
                    }
            }
        return arrayList1;
    }

    private void observeSameAndInviteContacts() {
        fireBaseDataBaseViewModel.divideNumberThatInServerAndNot(contactsArrayList);
        fireBaseDataBaseViewModel.getMutableLiveDataSameContactsArrayList().observe(this, new Observer<ArrayList<ContactModel>>() {
            @Override
            public void onChanged(ArrayList<ContactModel> contactModels) {
                simArrayList = contactModels;
                setAdapterSim(simArrayList);  //Set Adapter
            }
        });
        fireBaseDataBaseViewModel.getMutableLiveDataContactsNotInServerArrayList().observe(this, new Observer<ArrayList<ContactModel>>() {
            @Override
            public void onChanged(ArrayList<ContactModel> contactModels) {
               inviteArrayList =  contactModels;
               setAdapterInvite(inviteArrayList); //Set Adapter
            }
        });

    }
    private void setAdapterSim(ArrayList<ContactModel> contactModels){
        myRecycleAdapterContactsSim.setContactModelArrayList(contactModels);     //Set Array Of Adapter
        binding.ContactsActivityRecycle.setAdapter(myRecycleAdapterContactsSim); //Set Recycler Adapter
    }

    private void setAdapterInvite(ArrayList<ContactModel> contactModels){
        myRecycleAdapterContactsInvite.setContactModelArrayList(contactModels);      //Set Array Of Adapter
        binding.ContactsActivityRecycle2.setAdapter(myRecycleAdapterContactsInvite); //set Recycler Adapter

    }

    private void inti() {
        //Initialize contactsArrayList
        contactsArrayList = (ArrayList<ContactModel>) getIntent().getSerializableExtra("myContacts");
        //Initialize chatItemModelArrayList
        chatItemModelArrayList =  (ArrayList<ChatItemModel>) getIntent().getSerializableExtra("modelArrayList");
        //Initialize simArrayList
        simArrayList = new ArrayList<>();
        //Initialize inviteArrayList
        inviteArrayList = new ArrayList<>();
        //Initialize fireBaseDataBaseViewModel
        fireBaseDataBaseViewModel=  new ViewModelProvider(this).get(FireBaseDataBaseViewModel.class);
        //Initialize roomViewModel
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        //Initialize authViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        //Initialize preferenceViewModel
        preferenceViewModel =  new ViewModelProvider(this).get(PreferenceViewModel.class);
        //Initialize myRecycleAdapterContactsInvite
        myRecycleAdapterContactsInvite = new MyRecycleAdapterContacts();
        //Initialize myRecycleAdapterContactsSim
        myRecycleAdapterContactsSim = new MyRecycleAdapterContactsFriendaInServer(this);
    }

    private void displayBackIcon() {
        ActionBar actionBar = this.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void setOnclickOnItemToSRecycle() {
        myRecycleAdapterContactsInvite.setOnmyClickListenerrr(new MyRecycleAdapterContacts.OnmyClickListenerrr() {
            @Override
            public void onclick2(ContactModel contactModel) {
                Intent intent = new Intent(Intent.ACTION_VIEW , Uri.fromParts("sms",contactModel.getNum(),null));
                intent.putExtra("sms_body","Hi, I Invite You To Download (Chat Me) App");
                startActivity(intent);
            }
        });

    }

    private void setOnclickOnItemToFRecycle() {
        myRecycleAdapterContactsSim.setOnmyClickListenerrr(new MyRecycleAdapterContactsFriendaInServer.OnmyClickListenerrr() {
            @Override
            public void onclick2(ContactModel contactModel) {
                Intent intent = new Intent(ContactsActivity.this,ChatMessagesActivity.class);
                ChatMessagesReceivedModel chatMessagesReceivedModel = new ChatMessagesReceivedModel(chatItemModelArrayList,contactModel);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",chatMessagesReceivedModel);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


}