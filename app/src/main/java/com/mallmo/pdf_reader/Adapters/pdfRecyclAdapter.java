package com.mallmo.pdf_reader.Adapters;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.fileDetails;
import com.mallmo.pdf_reader.SavingFile.dataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.wordDataBaseHelper;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class pdfRecyclAdapter extends RecyclerView.Adapter<pdfRecyclAdapter.myholder> {



    public pdfRecyclAdapter(List<recycleListFormat> myList, List<recycleListFormat> myLoadedList) {
        this.myList = myList;
        this.myLoadedList = myLoadedList;
    }



    public  List<recycleListFormat> fileList;
    public List<recycleListFormat> myList;
    public List<recycleListFormat> myLoadedList;
    private onItemListener Listener;
    public onItemSaveClickListtener bookMarkListener;

 public int pos=0;
    public pdfRecyclAdapter(List<recycleListFormat> myList, onItemListener listener, List<recycleListFormat> myLoadedList,onItemSaveClickListtener bookMarkListener) {
        this.myList =myList;
        this.Listener = listener;
        this.myLoadedList = myLoadedList;
        this.bookMarkListener = bookMarkListener;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMyList(List<recycleListFormat> myList) {
        this.myList = myList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_recycler_view_row, parent, false);

        return new myholder(v, Listener, myList,myLoadedList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull myholder holder,  int position) {
        holder.txt.setText(myList.get(position).file.getName());
        if (myList.get(position).getTag()){
            holder.bt_bookmarks.setImageResource(R.drawable.bookmark_selected);

        }
        if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE){
            holder.bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
        }

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }
    @SuppressLint("NotifyDataSetChanged")



    public class myholder extends RecyclerView.ViewHolder {


        private List<fileDetails> fileDetailsList = new ArrayList<>();
        public SharedPreferences preferences;
        TextView txt, txt_size;
        ImageView bt_bookmarks, bt_option;



        public myholder(@NonNull View itemView, onItemListener listener, List<recycleListFormat> myList, List<recycleListFormat> myLoadedList) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_pdfName);
            txt_size = itemView.findViewById(R.id.txt_size);
            bt_bookmarks = itemView.findViewById(R.id.bt_bookmarks);
            bt_option= itemView.findViewById(R.id.bt_option);

            bt_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {


                        dataBaseHelper config = new dataBaseHelper(itemView.getContext());
                        recycleClicksHelper helper=new recycleClicksHelper
                                (myList.get(getAdapterPosition()).file.getAbsolutePath(),myList,config.loadingFiles()
                                        ,getAdapterPosition(),pdfRecyclAdapter.this);
                        helper.bookmarkClick();


                    }
                }
            });
            bt_bookmarks.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() != RecyclerView.NO_POSITION && bookMarkListener != null) {
                        savingToStorage(getAdapterPosition(), myList);

                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            listener.onItemClick(getAdapterPosition());
                        }
                    }
                }
            });


        }




            @SuppressLint("ResourceAsColor")

//save kardane file
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void savingToStorage(int position, List<recycleListFormat> myList) {

                boolean tag = true;
                if (tag) {
                    for (recycleListFormat f : myLoadedList) {

                        if (myList.get(position).file.getName().equals(f.file.getName())) {
                            bt_bookmarks.setImageResource(R.drawable.bookmark_unselected);

                           // fileList.remove(f);
                           bookMarkListener.onItemSaveClick(position,statics.UN_MARK);

                            //  myList.remove(myList.get(position));
                         //   notifyDataSetChanged();
                            return;
                        }
                    }

                    if (tag) {

                        bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
                        bookMarkListener.onItemSaveClick(position,statics.MARK);

//                        newRow.setTag(true);
//                        fileList.add(newRow);
//                        config(fileList);
                    }
                }





        }

    }


}
