package com.mallmo.pdf_reader.Adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.fileDetails;
import com.mallmo.pdf_reader.SavingFile.sharedPreffConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pdfRecyclAdapter extends RecyclerView.Adapter<pdfRecyclAdapter.myholder> {
    public List<File> myList;
    public List<File> myLoadedList;
    private onItemListener Listener;

    public pdfRecyclAdapter(List<File> myList, onItemListener listener, List<File> myLoadedList) {
        this.myList = myList;
        this.Listener = listener;
        this.myLoadedList = myLoadedList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_library, parent, false);

        return new myholder(v, Listener, myList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.txt.setText(myList.get(position).getName());
        for (File file : myLoadedList) {
            if (myList.get(position).getName().equals(file.getAbsoluteFile().getName()) &&
                    file.getAbsolutePath().equals(myList.get(position).getAbsolutePath())  &&
                   file.getName().charAt(0)==myList.get(position).getName().charAt(0)) {
                holder.bt_bookmarks.setImageResource(R.drawable.bookmark_selected);
            }

        }

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

        public myholder(@NonNull View itemView, onItemListener listener, List<File> myList) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_pdfName);
            txt_size = itemView.findViewById(R.id.txt_size);
            bt_bookmarks = itemView.findViewById(R.id.bt_bookmarks);

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void savingToStorage(int position, List<File> myList) {


            sharedPreffConfig config = new sharedPreffConfig(itemView.getContext());

            List<File> fileList = config.loadingFiles();

//                fileList.clear();
//                config.saveFileToMemory(fileList);
            if (fileList == null) {
                fileList = new ArrayList<>();

            } else {
                boolean tag = true;
                if (tag) {
                    for (File f : fileList) {

                        if (myList.get(position).getName().equals(f.getName())) {
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
                        fileList.add(new File(myList.get(position).getAbsolutePath()));
                        config.saveFileToMemory(fileList);
                        Toast.makeText(itemView.getContext(), "Marked", Toast.LENGTH_SHORT).show();
                    }
                }


            }


        }


    }
}
