package com.example.chatme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.pojo.MessageModel;
import com.example.chatme.pojo.MessageModel2;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyRecycleAdapterMessages extends RecyclerView.Adapter<MyRecycleAdapterMessages.Holder>{

    private ArrayList<MessageModel2> messageModelArrayList ;
    private String userId ;
    private Context context;


    public MyRecycleAdapterMessages(Context context) {
        this.context = context;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessageModelArrayList(ArrayList<MessageModel2> messageModelArrayList) {
        this.messageModelArrayList = messageModelArrayList;
    }

    public ArrayList<MessageModel2> getMessageModelArrayList() {
        return messageModelArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(messageModelArrayList.get(viewType).getId().equals(userId)){
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_receive,parent,false));
        }else {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_send,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.textDate.setText(getTimeOfFirebase(messageModelArrayList.get(position).getTimeStampValue()));

            if(!messageModelArrayList.get(position).getMessage().equals("")){
                holder.textMessage.setText(messageModelArrayList.get(position).getMessage());
            }else {
                holder.textMessage.setVisibility(View.GONE);
            }

            if (messageModelArrayList.get(position).getImage().equals("")){
                holder.imageView.setVisibility(View.GONE);
            }else {
                Glide.with(context).load(messageModelArrayList.get(position).getImage()).into(holder.imageView);
            }

            if(messageModelArrayList.get(position).isRead()){
               holder.imageViewOfRead.setImageResource(R.drawable.ic_baseline_double_check_24);
            }

    }
    private String getTimeOfFirebase(Long time){
        Date date = new Date(time);
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm aa",
                Locale.getDefault());
        return sfd.format(date);

    }

    @Override
    public int getItemCount() {
        if(messageModelArrayList != null)
            return messageModelArrayList.size();
        else
            return 0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textMessage  , textDate;
        ImageView imageView , imageViewOfRead;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_listSRMessage);
            imageView = itemView.findViewById(R.id.image_listSRMessage);
            textDate = itemView.findViewById(R.id.textDate_listSRMessage);
            imageViewOfRead = itemView.findViewById(R.id.imageRead_listSRMessage);

        }
    }
}
