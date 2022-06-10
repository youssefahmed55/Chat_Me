package com.example.chatme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatme.R;
import com.example.chatme.pojo.ContactModel;

import java.util.ArrayList;

public class MyRecycleAdapterContacts extends RecyclerView.Adapter<MyRecycleAdapterContacts.Holder>{

    private ArrayList<ContactModel> contactModelArrayList;
    private OnmyClickListenerrr onmyClickListenerrr;


    public void setContactModelArrayList(ArrayList<ContactModel> contactModelArrayList) {
        this.contactModelArrayList = contactModelArrayList;
    }

    public void setOnmyClickListenerrr(OnmyClickListenerrr onmyClickListenerrr) {
        this.onmyClickListenerrr = onmyClickListenerrr;
    }

    public interface OnmyClickListenerrr {
        void onclick2(ContactModel contactModel) ;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.textName.setText(contactModelArrayList.get(position).getName());
            holder.textNumber.setText(contactModelArrayList.get(position).getNum());

    }

    @Override
    public int getItemCount() {
        if(contactModelArrayList != null)
            return contactModelArrayList.size();
        else
            return 0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName , textNumber ;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name_listContact);
            textNumber = itemView.findViewById(R.id.number_listContact);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // you can trust the adapter position
                        // do whatever you intend to do with this position
                        if (onmyClickListenerrr != null)
                            onmyClickListenerrr.onclick2(contactModelArrayList.get(getAbsoluteAdapterPosition()));
                    }

                }
            });
        }
    }
}
