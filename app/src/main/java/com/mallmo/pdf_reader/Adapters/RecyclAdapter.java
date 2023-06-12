package com.mallmo.pdf_reader.Adapters;

import static com.mallmo.pdf_reader.statics.EXCEL_STATE;
import static com.mallmo.pdf_reader.statics.PDF_STATE;
import static com.mallmo.pdf_reader.statics.WORD_STATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.fileDetails;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.util.ArrayList;
import java.util.List;

public class RecyclAdapter extends RecyclerView.Adapter<RecyclAdapter.myholder> {



    public  List<recycleListFormat> fileList;
    public List<recycleListFormat> myList;
    public List<recycleListFormat> myLoadedList;
    private onItemListener Listener;
    public onOptionClickListner onOptionClickListner;
    public onItemSaveClickListtener bookMarkListener;
    public int state;

 public int pos=0;
    public RecyclAdapter(List<recycleListFormat> myList, onItemListener listener
            , List<recycleListFormat> myLoadedList, onItemSaveClickListtener bookMarkListener,
                         onOptionClickListner onOptionClickListner,int state) {

        this.myList =myList;
        this.Listener = listener;
        this.myLoadedList = myLoadedList;
        this.bookMarkListener = bookMarkListener;
        this.onOptionClickListner = onOptionClickListner;
        this.state=state;
    }

    @Override
    public int getItemViewType(int position) {
        switch (state) {
            case EXCEL_STATE:
                return EXCEL_STATE;
            case PDF_STATE:
                return PDF_STATE;
            case WORD_STATE:
                return WORD_STATE;

        }
        return PDF_STATE;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMyList(List<recycleListFormat> myList) {
        this.myList = myList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case PDF_STATE:
                return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_recycler_view_row, parent, false), Listener, myList,myLoadedList);
            case WORD_STATE:
                return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.word_recycler_view_row, parent, false), Listener, myList,myLoadedList);
            case EXCEL_STATE:
                return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.excel_recycler_view_row, parent, false), Listener, myList,myLoadedList);

                default:return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_recycler_view_row, parent, false), Listener, myList,myLoadedList);

        }

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
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && onOptionClickListner!=null) {
                            onOptionClickListner.onOptionClick(getAdapterPosition());

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
                           bookMarkListener.onItemSaveClick(position,statics.UN_MARK);
                            return;
                        }
                    }

                    if (tag) {

                        bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
                        bookMarkListener.onItemSaveClick(position,statics.MARK);
                    }
                }

        }

    }


}
