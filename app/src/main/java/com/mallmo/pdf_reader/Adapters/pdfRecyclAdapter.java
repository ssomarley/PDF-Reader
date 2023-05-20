package com.mallmo.pdf_reader.Adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.fileDetails;
import com.mallmo.pdf_reader.SavingFile.sharedPreffConfig;
import com.mallmo.pdf_reader.databinding.BottomSheetLayoutBinding;
import com.mallmo.pdf_reader.recyclPdf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pdfRecyclAdapter extends RecyclerView.Adapter<pdfRecyclAdapter.myholder> {
    public List<recyclPdf> myList;
    public List<recyclPdf> myLoadedList;
    private onItemListener Listener;
 public int pos=0;
    public pdfRecyclAdapter(List<recyclPdf> myList, onItemListener listener, List<recyclPdf> myLoadedList) {
        this.myList = myList;
        this.Listener = listener;
        this.myLoadedList = myLoadedList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_recycler_view_row, parent, false);

        return new myholder(v, Listener, myList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull myholder holder,  int position) {
        holder.txt.setText(myList.get(position).file.getName());
        if (myList.get(position).getTag()){
            holder.bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
            Log.i("ttt", String.valueOf(position +"\t"+ myList.get(position).file.getName()));

        }

//        for (File file : myLoadedList) {
//            if (file.getAbsoluteFile().getName().equals(myList.get(position).file.getAbsoluteFile().getName()) &&
//                    file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())  &&
//                    position !=pos+12 &&
//                    file.getAbsoluteFile().getParentFile().equals(myList.get(position).getAbsoluteFile().getParentFile()))
//                   {
//                        String fileName=file.getAbsoluteFile().getName();
//                         String fileListName=file.getAbsoluteFile().getName();
//                        int n=file.getAbsoluteFile().getName().length();
//
//                       for (int i = 0; i < n; i++) {
//                           if (fileName.charAt(i)!=fileListName.charAt(i)){
//                                break ;
//                           }else if (i==n-1){
//                               holder.bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
//                               pos=position;
//                           }
//                       }
//
//
//
//
//            }
//
//        }

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }


    public static class myholder extends RecyclerView.ViewHolder {


        private List<fileDetails> fileDetailsList = new ArrayList<>();
        public SharedPreferences preferences;
        TextView txt, txt_size;
        ImageView bt_bookmarks, bt_option;



        public myholder(@NonNull View itemView, onItemListener listener, List<recyclPdf> myList) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_pdfName);
            txt_size = itemView.findViewById(R.id.txt_size);
            bt_bookmarks = itemView.findViewById(R.id.bt_bookmarks);
            bt_option= itemView.findViewById(R.id.bt_option);

            bt_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                        recycleClicksHelper helper=new recycleClicksHelper
                                (myList.get(getAdapterPosition()).file.getAbsolutePath());
                        helper.bookmarkClick(getAdapterPosition());

                    }
                }
            });
            bt_bookmarks.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

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


        @RequiresApi(api = Build.VERSION_CODES.N)
        private void savingToStorage(int position, List<recyclPdf> myList) {


            sharedPreffConfig config = new sharedPreffConfig(itemView.getContext());

            List<recyclPdf> fileList = config.loadingFiles();

//                fileList.clear();
//                config.saveFileToMemory(fileList);
            if (fileList == null) {
                fileList = new ArrayList<>();

            } else {
                boolean tag = true;
                if (tag) {
                    for (recyclPdf f : fileList) {

                        if (myList.get(position).file.getName().equals(f.file.getName())) {
                            bt_bookmarks.setImageResource(R.drawable.bookmark_unselected);
                            Toast.makeText(itemView.getContext(), "UnMarked", Toast.LENGTH_SHORT).show();
                            fileList.remove(f);
                            config.saveFileToMemory(fileList);
                            tag = false;
                            return;
                        }
                    }

                    if (tag) {

                        bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
                        fileList.add(new recyclPdf(new File(myList.get(position).file.getAbsolutePath())));
                        config.saveFileToMemory(fileList);
                        Toast.makeText(itemView.getContext(), "Marked", Toast.LENGTH_SHORT).show();
                    }
                }


            }


        }


    }
}
