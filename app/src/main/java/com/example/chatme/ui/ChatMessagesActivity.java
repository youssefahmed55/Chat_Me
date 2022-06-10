package com.example.chatme.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.adapters.MyRecycleAdapterMessages;
import com.example.chatme.databinding.ActivityChatMessagesBinding;
import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.ChatMessagesReceivedModel;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.pojo.MessageModel2;
import com.example.chatme.pojo.notification.DataModel;
import com.example.chatme.pojo.notification.NotificationModel;
import com.example.chatme.pojo.notification.RootModel;
import com.example.chatme.ui.api.MyApiViewModel;
import com.example.chatme.ui.auth.AuthViewModel;
import com.example.chatme.ui.firebasedatabase.FireBaseDataBaseViewModel;
import com.example.chatme.ui.firebasestorage.FireBaseStorageViewModel;
import com.example.chatme.ui.rroom.RoomViewModel;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatMessagesActivity extends AppCompatActivity {
    private ActivityChatMessagesBinding binding;
    private View view;
    private ContactModel contactModel;
    private ContactModel myContactModel;
    private AuthViewModel authViewModel;
    private ArrayList<ChatItemModel> chatItemModelArrayList;
    private FireBaseDataBaseViewModel fireBaseDataBaseViewModel;
    private FireBaseStorageViewModel fireBaseStorageViewModel;
    private RoomViewModel roomViewModel;
    private MyApiViewModel myApiViewModel;
    private final int IMAGE_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri imageUri ;
    private MyRecycleAdapterMessages myRecycleAdapterMessages;
    private Boolean isMessageSent = true;
    private int pos;
    private static final String TAG = "ChatMessagesActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatMessagesBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        inti();                //Initialize variables
        getIntentBundleData(); //Initialize variables
        ShowProfileImageAndNameFromContactModel(); // Show User Profile Image And Name
        observeAuthUserId();   //Observe Auth Id
        observeSaveImage();    //Observe Save Image Of Message
        observeSaveMessage();  //Observe Save Message
        onClickOnBackIcon();   //Set On Click On Back Icon
        onClickOnImageIcon();  //Set On Click On Image Icon
        onClickOnSendIcon();   //Set On Click On Send Icon
        onClickOnCameraIcon(); //Set On Click On Camera Icon
        onClickOnDotsIcon();   //Set On Click On Dots Icon
        onClickOnName();       //set On Click On Name Of User
        if (!isNetworkAvailable()&&!isInternetAvailable()) Toast.makeText(this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();
    }

    private void observeAuthUserId() {
        authViewModel.getFirebaseAuthId();
        authViewModel.getMutableLiveDataUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String id) {
                observeMyProfile(id);
                Log.d(TAG, "onChangedId: "+id + "   +  " + contactModel.getId());
            }
        });
    }

    private void getIntentBundleData() {
        ChatMessagesReceivedModel  chatMessagesReceivedModel= (ChatMessagesReceivedModel) getIntent().getSerializableExtra("model");
        chatItemModelArrayList = chatMessagesReceivedModel.getChats();
        contactModel = chatMessagesReceivedModel.getContactModel();
        pos = getIfExists();
        Log.d(TAG, "getIntentBundleData: "+pos);
        Log.d(TAG, "getIntentBundleData: " + chatItemModelArrayList.size());
        if (pos > -1){
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(chatItemModelArrayList.get(pos).getMessageModel2ArrayList());
            myRecycleAdapterMessages.setUserId(contactModel.getId()); //Set UserId To Recycler
            setAdapter(arrayList); //Set Adatper
        }




    }

    private int getIfExists() {
        for(int i = 0 ; i < chatItemModelArrayList.size() ; i++){
            if(contactModel.getId().equals(chatItemModelArrayList.get(i).getContactModel().getId())){
                return i;
            }
        }
        return -1;
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
    private void observeSaveMessage() {
        fireBaseDataBaseViewModel.getIsNewMessageSaved().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                arrangeChatsByCount();  //Arrange Chats By Count
                imageUri =null;
                binding.ChatActivityEditTextMessage.setText("");
                binding.ChatActivityShowImage.setImageResource(0);
                isMessageSent = true;
            }
        });
    }

    private void observeSaveImage() {
        fireBaseStorageViewModel.getMutableLiveImageUri().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                saveMessage(binding.ChatActivityEditTextMessage.getText().toString().trim(),s);
            }
        });
    }

    private void onClickOnName() {
        binding.ChatActivityTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessagesActivity.this, UserProfile.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",contactModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void onClickOnBackIcon() {
        binding.ChatActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //Finish/Close Activity
            }
        });

    }


    private void onClickOnDotsIcon() {
        binding.ChatActivityDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()&&!isInternetAvailable())
                    Toast.makeText(ChatMessagesActivity.this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();
                else {
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), binding.ChatActivityDots);

                    // Inflating popup menu from popup_menu.xml file
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (myRecycleAdapterMessages.getMessageModelArrayList().size()>0) {
                                fireBaseDataBaseViewModel.clearMessages(myContactModel.getId(), contactModel.getId());
                                if (pos > -1){
                                    chatItemModelArrayList.remove(pos);
                                    pos = -1 ;
                                }
                                roomViewModel.deleteAndInsert(chatItemModelArrayList);
                            }
                            return true;
                        }
                    });
                    // Showing the popup menu
                    popupMenu.show();
                }
            }
        });
    }

    private void onClickOnCameraIcon() {
        binding.ChatActivityCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }

            }
        });
    }

    private void observeMessages() {
        myRecycleAdapterMessages.setUserId(contactModel.getId());
        fireBaseDataBaseViewModel.getMessagesOfChatFromFirebase(myContactModel.getId(),contactModel.getId());
        fireBaseDataBaseViewModel.getMutableLiveDataChatMessagesArrayList().observe(this, new Observer<ArrayList<MessageModel2>>() {
            @Override
            public void onChanged(ArrayList<MessageModel2> messageModels) {
                if (messageModels.size() != 0) {
                    if (pos != -1) {
                        Log.d(TAG, "onChanged: " + chatItemModelArrayList.size());
                        List<MessageModel2> list = new ArrayList<>();
                        list.addAll(messageModels);
                        ArrayList<ChatItemModel> arrayList = new ArrayList<>();
                        arrayList.addAll(chatItemModelArrayList);
                        arrayList.get(pos).setMessageModel2ArrayList(list);
                        if (messageModels.get(messageModels.size() - 1).getId().equals(contactModel.getId())) {
                            arrayList.get(pos).setLastmessage(messageModels.get(messageModels.size() - 1).getMessage());
                        } else {
                            arrayList.get(pos).setLastmessage(getString(R.string.you) + " " + messageModels.get(messageModels.size() - 1).getMessage());
                        }
                        ArrayList<ChatItemModel> chatItemModels = new ArrayList<>();
                        chatItemModels.add(arrayList.get(pos));
                        arrayList.remove(pos);
                        chatItemModels.addAll(arrayList);
                        Log.d(TAG, "onChanged: iam here -1");
                        roomViewModel.deleteAndInsert(chatItemModels);
                    } else {
                        ArrayList<ChatItemModel> chatItemModels = new ArrayList<>();
                        List<MessageModel2> list = new ArrayList<>();
                        list.addAll(messageModels);

                        if (messageModels.get(messageModels.size() - 1).getId().equals(contactModel.getId())) {
                            chatItemModels.add(new ChatItemModel(contactModel, contactModel.getImage(), contactModel.getName(), messageModels.get(messageModels.size() - 1).getMessage(), getTimeOfFirebase(messageModels.get(messageModels.size() - 1).getTimeStampValue()), list));
                        } else {
                            chatItemModels.add(new ChatItemModel(contactModel, contactModel.getImage(), contactModel.getName(), getString(R.string.you) + " " + messageModels.get(messageModels.size() - 1).getMessage(), getTimeOfFirebase(messageModels.get(messageModels.size() - 1).getTimeStampValue()), list));
                        }
                        if (chatItemModelArrayList.size()>0)
                        chatItemModels.addAll(chatItemModelArrayList);
                        Log.d(TAG, "onChanged: iam here Not -" +chatItemModels.size());
                        roomViewModel.deleteAndInsert(chatItemModels);
                    }

                }


                setAdapter(messageModels); //Set Adapter
            }
        });
    }

    private String getTimeOfFirebase(Long time){
        Date date = new Date(time);
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm aa",
                Locale.getDefault());
        return sfd.format(date);

    }

    private void setAdapter(ArrayList<MessageModel2> messageModels) {
        myRecycleAdapterMessages.setMessageModelArrayList(messageModels); //Set Arraylist Of Adapter
        binding.ChatActivityRecycle.setAdapter(myRecycleAdapterMessages); //Set Recycler Adapter
        //binding.ChatActivityRecycle.scrollToPosition(messageModels.size()-1);
    }

    private void onClickOnSendIcon() {

        binding.ChatActivitySendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()&&!isInternetAvailable())
                    Toast.makeText(ChatMessagesActivity.this, getString(R.string.Internet_Not_Connected), Toast.LENGTH_SHORT).show();
                else {
                    String message = binding.ChatActivityEditTextMessage.getText().toString().trim();
                    if (((!message.equals("") && imageUri != null) || (message.equals("") && imageUri != null)) && isMessageSent) {
                        isMessageSent = false;
                        saveImageInFirebaseStorage(imageUri);

                    } else if (!message.equals("")) {
                        isMessageSent = false;
                        saveMessage(message, "");
                    }
                }
            }
        });
    }

    private void saveImageInFirebaseStorage(Uri imageUri) {
        fireBaseStorageViewModel.saveImageInFirebase(getFileExtension(imageUri),imageUri);

    }

    private  String getFileExtension(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));

    }

    private void saveMessage(String message, String image) {
       fireBaseDataBaseViewModel.saveMessageInFirebase(myContactModel,contactModel,message,image); //Save Image
       //fireBaseMessagingViewModel.sendMessageNotification(contactModel.getToken(),message);
       //new FCMMessages().sendMessageSingle( contactModel.getId(), "Chat Me", message, null);
       myApiViewModel.sendNotification(new RootModel(contactModel.getToken(),new NotificationModel(myContactModel.getNum(),message),new DataModel("youssef Ahmed" , "23")));//Send Notification To User
       fireBaseDataBaseViewModel.getMessagesOfChatFromFirebase(myContactModel.getId(),contactModel.getId()); //Get Messages
    }

    private void arrangeChatsByCount() {
        fireBaseDataBaseViewModel.arrangeChatsInFirebase(myContactModel.getId(),contactModel.getId());
    }

    private void onClickOnImageIcon() {
        binding.ChatActivityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE);
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
        //Initialize myApiViewModel
        myApiViewModel = new ViewModelProvider(this).get(MyApiViewModel.class);
        //Initialize myRecycleAdapterMessages
        myRecycleAdapterMessages = new MyRecycleAdapterMessages(this);
        //Initialize roomViewModel
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

    }

    private void observeMyProfile(String id) {
        fireBaseDataBaseViewModel.getMyContactProfile(id);
        fireBaseDataBaseViewModel.getMutableLiveDataMyContact().observe(this, new Observer<ContactModel>() {
            @Override
            public void onChanged(ContactModel contactModelll) {
                myContactModel = contactModelll;
                observeMessages();  //Observe Messages
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE ) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Log.d(TAG, "onActivityResult: iam in images");
                imageUri = data.getData();
                //Bitmap photo = (Bitmap) data.getExtras().get("data");
                binding.ChatActivityShowImage.setImageURI(imageUri);
            }
        }

        if (requestCode == CAMERA_REQUEST){
            if (resultCode == Activity.RESULT_OK)
            {
                Log.d(TAG, "onActivityResult: iam in camera");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageUri = getImageUri(photo);
                binding.ChatActivityShowImage.setImageBitmap(photo);
            }
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void ShowProfileImageAndNameFromContactModel() {
        Log.d(TAG, "ShowProfileImageAndNameFromContactModel: " + contactModel.getToken());
        binding.ChatActivityTextName.setText(contactModel.getName());
        Glide.with(this).load(contactModel.getImage()).into(binding.ChatActivityProfileImage);
    }
}