package com.mallmo.pdf_reader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatWidthException;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

public class pdfRecyclAdapter extends RecyclerView.Adapter<pdfRecyclAdapter.myholder> {

    List<File> myList;
    private onItemListener Listener;

    public pdfRecyclAdapter(List<File> myList, onItemListener listener) {
        this.myList = myList;
        this.Listener=listener;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_library,parent,false);

        return new myholder(v,Listener);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
                holder.txt.setText(myList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }



    public static class myholder extends RecyclerView.ViewHolder  {
        TextView txt;
        public myholder(@NonNull View itemView, onItemListener listener) {
            super(itemView);
        txt=itemView.findViewById(R.id.txt_pdfName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener !=null){
                    if (getAdapterPosition() !=RecyclerView.NO_POSITION){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            }
        });
        }
    }
}
