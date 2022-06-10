package com.example.chatme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.R;
import com.example.chatme.pojo.ChatItemModel;
import com.example.chatme.pojo.MessageModel2;


import java.util.ArrayList;

public class MyRecycleAdapterChatItem extends RecyclerView.Adapter<MyRecycleAdapterChatItem.Holder>{
    private ArrayList<ChatItemModel> chatItemModelArrayList;
    private OnmyClickListenerrr onmyClickListenerrr;
    private Context context;
    private String myId;
    public MyRecycleAdapterChatItem(Context context,String myId) {
        this.context = context;
        this.myId = myId;
    }

    public void setChatItemModelList(ArrayList<ChatItemModel> chatItemModelArrayList) {
        this.chatItemModelArrayList = chatItemModelArrayList;
    }

    public void setOnmyClickListenerrr(OnmyClickListenerrr onmyClickListenerrr) {
        this.onmyClickListenerrr = onmyClickListenerrr;
    }

    public interface OnmyClickListenerrr {
        void onclick2(ChatItemModel chatItemModel) ;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chatitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
            Glide.with(context).load(chatItemModelArrayList.get(position).getImageUrl()).into(holder.imageView);
            holder.textName.setText(chatItemModelArrayList.get(position).getName());
            holder.textMessage.setText(chatItemModelArrayList.get(position).getLastmessage());
            holder.textTime.setText(chatItemModelArrayList.get(position).getTime());
            if(chatItemModelArrayList.get(position).getMessageModel2ArrayList()!=null) {
                if (!chatItemModelArrayList.get(position).getMessageModel2ArrayList().get(chatItemModelArrayList.get(position).getMessageModel2ArrayList().size() - 1).isRead() && !chatItemModelArrayList.get(position).getMessageModel2ArrayList().get(chatItemModelArrayList.get(position).getMessageModel2ArrayList().size() - 1).getId().equals(myId)) {
                    holder.textMessage.setTextColor(Color.BLUE);
                }
                int unreadCount = 0;
                for (MessageModel2 messageModel2 : chatItemModelArrayList.get(position).getMessageModel2ArrayList()) {
                    if (!messageModel2.isRead() && !messageModel2.getId().equals(myId))
                        unreadCount++;
                }
                if (unreadCount > 0) {
                    holder.cardView.setVisibility(View.VISIBLE);
                    holder.textUnRead.setText(String.valueOf(unreadCount));
                }

            }

    }

    @Override
    public int getItemCount() {
        if(chatItemModelArrayList != null)
            return chatItemModelArrayList.size();
        else
            return 0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName , textMessage , textTime ,textUnRead;
        ImageView imageView;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name_listChatItem);
            textMessage = itemView.findViewById(R.id.message_listChatItem);
            textTime = itemView.findViewById(R.id.time_listChatItem);
            imageView = itemView.findViewById(R.id.profileImage_listChatItem);
            textUnRead = itemView.findViewById(R.id.unReadMessages_listChatItem);
            cardView = itemView.findViewById(R.id.unReadMessagesCard_listChatItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // you can trust the adapter position
                        // do whatever you intend to do with this position
                        if (onmyClickListenerrr != null)
                            onmyClickListenerrr.onclick2(chatItemModelArrayList.get(getAbsoluteAdapterPosition()));
                    }

                }
            });
        }
    }
}
