package com.example.chatme.ui.firebasedatabase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.ChatsModel;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.pojo.MessageModel;
import com.example.chatme.pojo.MessageModel2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FireBaseDataBaseViewModel extends ViewModel {
    private static final String TAG = "FireBaseDataBaseViewMod";
    private FireBaseDataBaseRepo fireBaseDataBaseRepo;
    private final MutableLiveData<ArrayList<ContactModel>> arrayListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNewProfileSaved = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ChatsModel>> mutableLiveDataChatsArrayList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ChatItemModel>> mutableLiveDataChatsItemArrayList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactModel>> mutableLiveDataContactsNotInServerArrayList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactModel>> mutableLiveDataSameContactsArrayList = new MutableLiveData<>();
    private final MutableLiveData<ContactModel> mutableLiveDataMyContact = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ChatItemModel>> mutableLiveDataUserChatsArrayList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNewMessageSaved = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MessageModel2>> mutableLiveDataChatMessagesArrayList = new MutableLiveData<>();



    @Inject
    public FireBaseDataBaseViewModel(FireBaseDataBaseRepo fireBaseDataBaseRepo) {
        this.fireBaseDataBaseRepo = fireBaseDataBaseRepo;
    }

    public MutableLiveData<ArrayList<ContactModel>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsNewProfileSaved() {
        return isNewProfileSaved;
    }

    public MutableLiveData<ArrayList<ChatsModel>> getMutableLiveDataChatsArrayList() {
        return mutableLiveDataChatsArrayList;
    }

    public MutableLiveData<ArrayList<ChatItemModel>> getMutableLiveDataChatsItemArrayList() {
        return mutableLiveDataChatsItemArrayList;
    }

    public MutableLiveData<ArrayList<ContactModel>> getMutableLiveDataContactsNotInServerArrayList() {
        return mutableLiveDataContactsNotInServerArrayList;
    }

    public MutableLiveData<ArrayList<ContactModel>> getMutableLiveDataSameContactsArrayList() {
        return mutableLiveDataSameContactsArrayList;
    }

    public MutableLiveData<ContactModel> getMutableLiveDataMyContact() {
        return mutableLiveDataMyContact;
    }

    public MutableLiveData<Boolean> getIsNewMessageSaved() {
        return isNewMessageSaved;
    }

    public MutableLiveData<ArrayList<MessageModel2>> getMutableLiveDataChatMessagesArrayList() {
        return mutableLiveDataChatMessagesArrayList;
    }

    public MutableLiveData<ArrayList<ChatItemModel>> getMutableLiveDataUserChatsArrayList() {
        return mutableLiveDataUserChatsArrayList;
    }

    //Get Users From FireBase DataBase
    public void getUsersFromFirebaseDataBase() {
        ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    contactModelArrayList.add(snapshot1.getValue(ContactModel.class));
                }
                Log.d(TAG, "onDataChange: getUsersFromFirebaseDataBase ");
                arrayListMutableLiveData.setValue(contactModelArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Save new Profile
    public void saveNewProfile(ContactModel contactModel) {
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(contactModel.getId()).setValue(contactModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    getIsNewProfileSaved().setValue(true);
                }
            }
        });

    }
    //Get Chats From Firebase
    public void getChatsFromFirebase(String id) {
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(id).child("chats").orderByChild("count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatsModel> chatsModelArrayList = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    chatsModelArrayList.add(snapshot1.getValue(ChatsModel.class));
                }
                Log.d(TAG, "onDataChange: getChatsFromFirebase ");
                mutableLiveDataChatsArrayList.setValue(chatsModelArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Get Message Of Chats From Firebase
    public void getMessagesFromFirebase(String id , ArrayList<ChatsModel> chatsModelArrayList,String you) {
        ArrayList<ChatItemModel> chatItemModelArrayList = new ArrayList<>();
        if(chatsModelArrayList.size() > 0)

            for (int o = 0 ; o < chatsModelArrayList.size() ; o++){

                int finalO = o;
                fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(id).child("chats").child(chatsModelArrayList.get(finalO).getContactModel().getId()).child("messageModel").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<MessageModel2> arrayListMessageModel = new ArrayList<>();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            arrayListMessageModel.add(snapshot1.getValue(MessageModel2.class));

                        }

                        if (arrayListMessageModel.size() == 0){

                            chatItemModelArrayList.add(new ChatItemModel(chatsModelArrayList.get(finalO).getContactModel(),chatsModelArrayList.get(finalO).getContactModel().getImage()
                                    , chatsModelArrayList.get(finalO).getContactModel().getName(), "", "",arrayListMessageModel));
                        }else {
                            Log.d(TAG, "onDataChange: " +arrayListMessageModel.get(arrayListMessageModel.size()-1).isRead());
                            String s = "";
                            if(arrayListMessageModel.get(arrayListMessageModel.size() - 1).getId().equals(id)){s=you + " ";}

                            if (arrayListMessageModel.get(arrayListMessageModel.size() - 1).getImage().equals("")) {
                                chatItemModelArrayList.add(new ChatItemModel(chatsModelArrayList.get(finalO).getContactModel(), chatsModelArrayList.get(finalO).getContactModel().getImage()
                                        , chatsModelArrayList.get(finalO).getContactModel().getName(), s + arrayListMessageModel.get(arrayListMessageModel.size() - 1).getMessage(), getTimeOfFirebase(arrayListMessageModel.get(arrayListMessageModel.size() - 1).getTimeStampValue()),arrayListMessageModel));
                            }else {

                                chatItemModelArrayList.add(new ChatItemModel(chatsModelArrayList.get(finalO).getContactModel(), chatsModelArrayList.get(finalO).getContactModel().getImage()
                                        , chatsModelArrayList.get(finalO).getContactModel().getName(), s + "IMAGE", getTimeOfFirebase(arrayListMessageModel.get(arrayListMessageModel.size() - 1).getTimeStampValue()),arrayListMessageModel));
                            }
                        }



                        if(finalO == chatsModelArrayList.size() -1)
                        mutableLiveDataChatsItemArrayList.setValue(chatItemModelArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


    }
    //GeT Time Of FireBase To Local Time
    private String getTimeOfFirebase(Long time){
        Date date = new Date(time);
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm aa",
                Locale.getDefault());
        return sfd.format(date);

    }
    //Check Chats user Profile Updates
    public void checkUserProfileUpdated(ArrayList<ChatItemModel> chatItemModels){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (ChatItemModel chatItem : chatItemModels){
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        if(chatItem.getContactModel().getId().equals(snapshot1.getValue(ContactModel.class).getId())){
                            chatItem.setImageUrl(snapshot1.getValue(ContactModel.class).getImage());
                            chatItem.getContactModel().setImage(snapshot1.getValue(ContactModel.class).getImage());
                            chatItem.getContactModel().setNickname(snapshot1.getValue(ContactModel.class).getNickname());
                            chatItem.getContactModel().setAbout(snapshot1.getValue(ContactModel.class).getAbout());
                            chatItem.getContactModel().setToken(snapshot1.getValue(ContactModel.class).getToken());
                            break;

                        }
                }
                    Log.d(TAG, "onDataChange: iam hereeeeeeeee");
                    mutableLiveDataUserChatsArrayList.setValue(chatItemModels);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Divide Number That In Server And Not
    public void divideNumberThatInServerAndNot(ArrayList<ContactModel> contactsArrayList) {
        ArrayList<ContactModel> contactsInServerArrayList = new ArrayList<>();
        ArrayList<ContactModel> contactsSameArrayList = new ArrayList<>();
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    contactsInServerArrayList.add(dataSnapshot.getValue(ContactModel.class));
                }
                if(contactsInServerArrayList.size() >0) {
                    for (int i = 0; i < contactsInServerArrayList.size(); i++) {
                        String num = contactsInServerArrayList.get(i).getNum();
                        for (int j = 0; j < contactsArrayList.size(); j++) {
                            if (num.equals(contactsArrayList.get(j).getNum().substring(1))) {
                                contactsInServerArrayList.get(i).setName(contactsArrayList.get(j).getName());
                                contactsSameArrayList.add(contactsInServerArrayList.get(i));
                                break;
                            }

                        }
                    }
                }
                if(contactsSameArrayList.size() > 0){
                    for(int i = 0 ; i <contactsSameArrayList.size();i++){
                        String num = contactsSameArrayList.get(i).getNum();
                        for (int j = 0; j < contactsArrayList.size(); j++) {
                            if (num.equals(contactsArrayList.get(j).getNum().substring(1))) {
                                contactsArrayList.remove(j);
                                break;
                            }

                        }

                    }

                }

                mutableLiveDataContactsNotInServerArrayList.setValue(contactsArrayList);
                mutableLiveDataSameContactsArrayList.setValue(contactsSameArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //Get My Contact Profile
    public void getMyContactProfile(String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(id.equals(snapshot1.getValue(ContactModel.class).getId())){
                        mutableLiveDataMyContact.setValue(snapshot1.getValue(ContactModel.class));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    //Save Message In Firebase
    public void saveMessageInFirebase(ContactModel myContactModel , ContactModel userContactModel , String message , String image){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        //dateFormat.format(new Date())
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myContactModel.getId()).child("chats").child(userContactModel.getId()).child("contactModel").setValue(userContactModel);
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userContactModel.getId()).child("chats").child(myContactModel.getId()).child("contactModel").setValue(myContactModel);
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myContactModel.getId()).child("chats").child(userContactModel.getId()).child("messageModel").push().setValue(new MessageModel(myContactModel.getId(), message, image,ServerValue.TIMESTAMP , false));
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userContactModel.getId()).child("chats").child(myContactModel.getId()).child("messageModel").push().setValue(new MessageModel(myContactModel.getId(), message, image,ServerValue.TIMESTAMP , false));
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myContactModel.getId()).child("chats").child(userContactModel.getId()).child("count").setValue(1);
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userContactModel.getId()).child("chats").child(myContactModel.getId()).child("count").setValue(1);

        isNewMessageSaved.setValue(true);
    }

    //Arrange Chats In Firebase
    public void arrangeChatsInFirebase(String myId , String userId){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatsModel> arrayList = new ArrayList<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    arrayList.add(snapshot1.getValue(ChatsModel.class));
                }


                for (ChatsModel chatsModel : arrayList) {

                    if (!chatsModel.getContactModel().getId().equals(userId)) {
                        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").child(chatsModel.getContactModel().getId()).child("count").setValue(chatsModel.getCount() + 1);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userId).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatsModel> arrayList2 = new ArrayList<>();


                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    arrayList2.add(snapshot1.getValue(ChatsModel.class));
                }
                for (ChatsModel chatsModel : arrayList2){

                    if(!chatsModel.getContactModel().getId().equals(myId)){
                        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userId).child("chats").child(chatsModel.getContactModel().getId()).child("count").setValue(chatsModel.getCount()+1);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    //Get Messages Of Chat From Firebase
    public void getMessagesOfChatFromFirebase(String myId, String userId){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").child(userId).child("messageModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MessageModel2> messageModelArrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messageModelArrayList.add(dataSnapshot.getValue(MessageModel2.class));
                }
                Log.d(TAG, "onDataChangeeee: " + myId + " ++ " + userId);
                mutableLiveDataChatMessagesArrayList.setValue(messageModelArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
   //Clear Messages Of Chat
   public void clearMessages(String myId , String userId){
       fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").child(userId).setValue(null);
   }

   //Set Messages Read To Me
   public void setUserMessagesReadToMe(String myId , String userId){
       fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").child(userId).child("messageModel").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               ArrayList<MessageModel2> messageModelArrayList = new ArrayList<>();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   messageModelArrayList.add(dataSnapshot.getValue(MessageModel2.class));
               }

               if(messageModelArrayList.size() > 0){
                   for (MessageModel2 model : messageModelArrayList){
                       if (model.getId().equals(userId)) {
                           model.setRead(true);
                       }
                   }
                       fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(myId).child("chats").child(userId).child("messageModel").setValue(messageModelArrayList);
                   }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
    //Set Messages Read To User
   public void setMessagesReadToUser(String myId , String userId){

       fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userId).child("chats").child(myId).child("messageModel").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               ArrayList<MessageModel2> messageModelArrayList = new ArrayList<>();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   messageModelArrayList.add(dataSnapshot.getValue(MessageModel2.class));
               }

               if(messageModelArrayList.size() > 0){
                   for (MessageModel2 model : messageModelArrayList){
                       if (model.getId().equals(userId))
                           model.setRead(true);
                       }
                   fireBaseDataBaseRepo.getFireBaseDataBase().child("Chats").child(userId).child("chats").child(myId).child("messageModel").setValue(messageModelArrayList);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
   //Save New NickName
   public void saveNewNickName(String nickName , String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(id).child("nickname").setValue(nickName);

   }
    //Save New Status
    public void saveNewStatusName(String status , String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(id).child("about").setValue(status);
    }
    //Save New Profile Image
    public void saveNewUriProfileImage(String uri,String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(id).child("image").setValue(uri);

    }
    //Save New Access Token In Firebase
    public void saveNewAccessToken(String token,String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(id).child("token").setValue(token);

    }
    //Delete Access Token From Firebase
    public void deleteAccessToken(String id){
        fireBaseDataBaseRepo.getFireBaseDataBase().child("Users").child(id).child("token").setValue("");

    }

}
