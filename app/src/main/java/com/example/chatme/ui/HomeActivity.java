package com.example.chatme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.chatme.R;
import com.example.chatme.adapters.MyRecycleAdapterChatItem;
import com.example.chatme.databinding.ActivityHomeBinding;
import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.ChatMessagesReceivedModel;
import com.example.chatme.pojo.ChatsModel;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.firebasemessaging.FireBaseMessagingViewModel;
import com.example.chatme.ui.login.MainActivity;
import com.example.chatme.ui.rroom.RoomViewModel;
import com.example.chatme.ui.sharedprefrence.PreferenceViewModel;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity{
    private View view;
    private ActivityHomeBinding binding;
    private ArrayList<ContactModel> contactsArrayList;
    private ArrayList<ChatItemModel> chatItemsArrayList;
    private MyRecycleAdapterChatItem myRecycleAdapterChatItem;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private FireBaseMessagingViewModel fireBaseMessagingViewModel;
    private AuthViewModel authViewModel;
    private PreferenceViewModel preferenceViewModel;
    private RoomViewModel roomViewModel;
    private static final String TAG = "HomeActivity";
    private String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        inti();                          //Initialize variables
        observeFireBaseUserID();         //Observe FireBase User Id
        checkRoomData();                 //Check Room Data
        onClickOnChatCardContactsImage();//Set On Click on Contacts Image
        if (!isNetworkAvailable()&&!isInternetAvailable()) Toast.makeText(this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();



    }

    private void observeFireBaseUserID() {
        authViewModel.getFirebaseAuthId();
        authViewModel.getMutableLiveDataUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                saveAccessToken(s);    //Save Access Token Of FCM
                myRecycleAdapterChatItem = new MyRecycleAdapterChatItem(HomeActivity.this,s); //Initialize myRecycleAdapterChatItem
                setOnClickOnRecycle(); //Set On Click On Recycler Item
                myId = s;
                checkPermission();     //Check Permission To Get Contacts Numbers And Names
            }
        });
    }

    private void checkRoomData() {
        roomViewModel.getMyChats();
        roomViewModel.getMutableLiveDataChats().observe(this, new Observer<List<ChatItemModel>>() {
            @Override
            public void onChanged(List<ChatItemModel> chatItemModelList) {
                if (chatItemModelList!=null) {
                    chatItemsArrayList.clear();
                    chatItemsArrayList.addAll(chatItemModelList);
                    Log.d(TAG, "onChanged: " + chatItemModelList.size());
                    setAdapter(chatItemsArrayList);
                }
            }
        });
    }


    private void saveAccessToken(String id) {
            fireBaseMessagingViewModel.getFirebaseToken();
            fireBaseMessagingViewModel.getMutableLiveDataToken().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    fireBaseDataBaseViewModel.saveNewAccessToken(s,id);
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
                if (!s.isEmpty())
                    setAdapter(getSearchItem(s,chatItemsArrayList));
                else
                    setAdapter(chatItemsArrayList);

                return true;
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                 preferenceViewModel.setBoolSharedPreferencesValue(false);
                 authViewModel.authSignOut();
                 roomViewModel.deleteAll();
                if (myId != null)
                 fireBaseDataBaseViewModel.deleteAccessToken(myId);
                 preferenceViewModel.deleteContactsSharedPreferences();
                 Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                 startActivity(intent);
                 finish();
                 break;
            case R.id.profile:
                if (!isNetworkAvailable()&&!isInternetAvailable())
                Toast.makeText(this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();
                else {
                    Intent intent2 = new Intent(HomeActivity.this, MyProfile.class);
                    startActivity(intent2);
                }

        }


        return super.onOptionsItemSelected(item);
    }


    private ArrayList<ChatItemModel> getSearchItem(String s, ArrayList<ChatItemModel> s2) {

        ArrayList<ChatItemModel> arrayList1 = new ArrayList<>();
        if (!s.equals(""))
            for (ChatItemModel e : s2) {
                if (e.getName().toLowerCase().charAt(0) == s.toLowerCase().charAt(0))
                    if (e.getName().toLowerCase().contains(s.toLowerCase())) {
                        arrayList1.add(e);
                    }
            }
        return arrayList1;
    }

    private void onClickOnChatCardContactsImage() {
        binding.HomeCardContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("modelArrayList",chatItemsArrayList);
                bundle.putSerializable("myContacts",contactsArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setOnClickOnRecycle() {
        myRecycleAdapterChatItem.setOnmyClickListenerrr(new MyRecycleAdapterChatItem.OnmyClickListenerrr() {
            @Override
            public void onclick2(ChatItemModel chatItemModel) {
                Intent intent = new Intent(HomeActivity.this, ChatMessagesActivity.class);
                ChatMessagesReceivedModel chatMessagesReceivedModel = new ChatMessagesReceivedModel(chatItemsArrayList,chatItemModel.getContactModel());
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",chatMessagesReceivedModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void inti() {
        fireBaseDataBaseViewModel= new ViewModelProvider(this).get(FireBaseDataBaseViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        preferenceViewModel =  new ViewModelProvider(this).get(PreferenceViewModel.class);
        fireBaseMessagingViewModel =  new ViewModelProvider(this).get(FireBaseMessagingViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        contactsArrayList = new ArrayList<>();
        chatItemsArrayList = new ArrayList<>();

    }

    private void checkPermission(){
        //Check condition
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //When permission is not granted
            //Request permission
            //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( new String[]{Manifest.permission.READ_CONTACTS}, 100);
            }
        }else {
            //When permission is granted
            //Create method
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getContactList();
                }
            });


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //When permission is granted
            //Call method
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getContactList();
                }
            });


        }else {
            //When permission is denied
            //Display toast
            Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            //Call check permission method
            //checkPermission();

        }
    }
    private void getContactList() {

        //Initialize uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        //Sort by ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+"ASC";
        //Initialize cursor
        Cursor cursor = this.getContentResolver().query(
                uri,null,null,null,sort
        );


        //Check condition
        if(cursor != null && cursor.moveToFirst() &&cursor.getCount() > 0) {
            //cursor.moveToPosition(0);
            //When count is greater than 0
            //Use while loop

            while (cursor.moveToNext()) {
                //Cursor move to next
                //Get contact name
                int name = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String namee = cursor.getString(name);
                // Get Contact id
                String id = cursor.getString((int)cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //Initialize phone uri
                Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                //Initialize selection
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                //Initialize phone cursor
                Cursor phoneCursor = this.getContentResolver().query(
                        phoneUri,null,selection,new String[]{id},null
                );
                //Check condition
                if(phoneCursor.moveToNext()){
                    //When phone cursor move to next
                    String number = phoneCursor.getString((int)phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    //add contact model
                    contactsArrayList.add(new ContactModel("","",namee, "" , number, "",""));
                    //Set array of adapter
                    //myRecycleAdapterContacts.setContactModelArrayList(contactsArrayList);

                    //Close phone cursor
                    phoneCursor.close();
                }
            }
            //Close cursor
            cursor.close();


        }
        //Set adapter
        //binding.ContactsActivityRecycle.setAdapter(myRecycleAdapterContacts);
        observeAuthPhoneNumber();      //Observe Auth Phone Number
        observeGetChatsFromFirebase(); //Observe Get Chats From Firebase


    }

    private void observeAuthPhoneNumber() {
        authViewModel.getAuthPhoneNum();
        authViewModel.getMutableLiveDataPhoneNumber().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phoneNumber) {
                editNumbersWithCountryCode(phoneNumber); //Edit Numbers With Country Code
            }
        });
    }

    private void observeGetChatsFromFirebase() {
        fireBaseDataBaseViewModel.getChatsFromFirebase(myId);
        fireBaseDataBaseViewModel.getMutableLiveDataChatsArrayList().observe(this, new Observer<ArrayList<ChatsModel>>() {
            @Override
            public void onChanged(ArrayList<ChatsModel> chatsModels) {
                if (chatsModels.size() ==0){
                    setAdapter(new ArrayList<>());
                }else
                editNamesOfThemToContactsName(chatsModels); //Edit Names Of Chats To Contacts Names
            }
        });


    }
    private void editNamesOfThemToContactsName(ArrayList<ChatsModel> chatsModelArrayList) {
        if(chatsModelArrayList.size() >0) {
            for (int i = 0; i < chatsModelArrayList.size(); i++) {
                String num = chatsModelArrayList.get(i).getContactModel().getNum();
                for (int j = 0; j < contactsArrayList.size(); j++) {
                    if (num.equals(contactsArrayList.get(j).getNum().substring(1))) {
                        chatsModelArrayList.get(i).getContactModel().setName(contactsArrayList.get(j).getName());
                        break;
                    }
                    if(j == contactsArrayList.size() -1){
                        chatsModelArrayList.get(i).getContactModel().setName("+ " + chatsModelArrayList.get(i).getContactModel().getNum());
                    }

                }
            }
        }

        observeGetMessagesFromFireBase(chatsModelArrayList); //Observe Get Messages Of Chats From Firebase

    }

    private void observeGetMessagesFromFireBase(ArrayList<ChatsModel> chatsModelArrayList) {
        fireBaseDataBaseViewModel.getMessagesFromFirebase(myId,chatsModelArrayList,getString(R.string.you));
        fireBaseDataBaseViewModel.getMutableLiveDataChatsItemArrayList().observe(this, new Observer<ArrayList<ChatItemModel>>() {
            @Override
            public void onChanged(ArrayList<ChatItemModel> chatItemModels) {

                observeCheckUserProfileUpdates(chatItemModels); //Observe Check Chats Users Updates

            }
        });
    }

    private void observeCheckUserProfileUpdates(ArrayList<ChatItemModel> chatItemModels) {
        fireBaseDataBaseViewModel.checkUserProfileUpdated(chatItemModels);
        fireBaseDataBaseViewModel.getMutableLiveDataUserChatsArrayList().observe(this, new Observer<ArrayList<ChatItemModel>>() {
            @Override
            public void onChanged(ArrayList<ChatItemModel> chatItemModelss) {
                Log.d(TAG, "onChanged: " + chatItemModelss.size());
                if (chatItemModelss.size() != 0) {
                    chatItemsArrayList = chatItemModelss;
                    roomViewModel.deleteAndInsert(chatItemsArrayList);
                    Log.d(TAG, "onChanged: Home Room");
                    setAdapter(chatItemsArrayList); //Set Adapter
                }
            }
        });
    }

    private void setAdapter(ArrayList<ChatItemModel> chatItemModelArrayList) {
        myRecycleAdapterChatItem.setChatItemModelList(chatItemModelArrayList); //Set ArrayList Of Adapter
        binding.HomeRecycle.setAdapter(myRecycleAdapterChatItem);              //Set Recycler Adapter
    }

    private void editNumbersWithCountryCode(String phoneNumber) {
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso().toUpperCase();
        String countryCodeForRegion = String.valueOf(PhoneNumberUtil.createInstance(this).getCountryCodeForRegion(countryCode));

        //String[] arrContryCode=this.getResources().getStringArray(R.array.DialingCountryCode);

        for (int i = 0 ; i < contactsArrayList.size() ; i++){
            String s = contactsArrayList.get(i).getNum();
            if(s.charAt(0) != '+') {
                if (s.charAt(0) == countryCodeForRegion.charAt(countryCodeForRegion.length() - 1)) {
                    contactsArrayList.get(i).setNum("+" + countryCodeForRegion + s.substring(1));
                } else {
                    contactsArrayList.get(i).setNum("+" + countryCodeForRegion + s);
                }
            }
        }

        for(int i = 0 ; i < contactsArrayList.size() ; i++){   // Remove User Number From Them If Exists
            if(phoneNumber.equals(contactsArrayList.get(i).getNum())){
                contactsArrayList.remove(i);
            }
        }

        preferenceViewModel.saveContactsSharedPreferences(contactsArrayList); //Save Contacts In Shared Preferences
    }

}