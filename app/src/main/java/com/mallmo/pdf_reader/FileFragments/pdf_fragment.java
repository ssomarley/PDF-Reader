package com.mallmo.pdf_reader.FileFragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.mallmo.pdf_reader.Adapters.onItemSaveClickListtener;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.dataBaseHelper;
import com.mallmo.pdf_reader.databinding.FragmentMyFragment1Binding;
import com.mallmo.pdf_reader.Adapters.onItemListener;
import com.mallmo.pdf_reader.Adapters.pdfRecyclAdapter;
import com.mallmo.pdf_reader.ShowFIles.showPDFfiles;
import com.mallmo.pdf_reader.getFiles;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pdf_fragment extends Fragment implements onItemListener , onItemSaveClickListtener {
   public  pdfRecyclAdapter adapter;
    dataBaseHelper config;
   public getFiles files;
    List<recycleListFormat> myLoadedList;
ArrayList<recycleListFormat> list=new ArrayList<>();
private int mflag;
    FragmentMyFragment1Binding binding;


    public static pdf_fragment newInstance(int flag) {
        pdf_fragment fragment =new pdf_fragment();
        Bundle args = new Bundle();
        args.putInt(statics.KEY, flag);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statics.FILE_FLAG=statics.PDF_STATE;
        if (getArguments()!=null){
            mflag=getArguments().getInt(statics.KEY);
        }
        config=new dataBaseHelper(getContext());
        myLoadedList =config.loadingFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding=FragmentMyFragment1Binding.inflate(inflater,container,false);

       files= MainActivity.getGetFiles();

      //  files.getAllFiles(Environment.getExternalStorageDirectory());
        if (MainActivity.FLAG==MainActivity.MY_FILES_STATE){

            list= (ArrayList<recycleListFormat>) files.getPdfFiles();

        } else if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE) {

            loadingFileFromStorage();

        }



        if (myLoadedList ==null) myLoadedList =new ArrayList<>();
         adapter=new pdfRecyclAdapter(list, this, myLoadedList,this);
         binding.recycl.setHasFixedSize(true);
         binding.recycl.setLayoutManager(new LinearLayoutManager(getContext()));
         binding.recycl.setAdapter(adapter);


         return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }


    @Override
    public void onItemClick(int position ) {

       String path=list.get(position).file.getAbsolutePath();
       showPDFfiles pdfFragmemt= showPDFfiles.newInstance(path ,MainActivity.FLAG);
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
         transaction.replace(R.id.frame,pdfFragmemt,"");
         transaction.commit();

    }

    private void loadingFileFromStorage(){


        list.clear();

        if (config.loadingFiles() ==null){
            list=new ArrayList<>();

        }
        else {
            list= (ArrayList<recycleListFormat>) config.loadingFiles();
        }
    }

    @Override
    public void onItemSaveClick(int position ,int action) {
            switch (action){
                case statics.MARK:
                    markFile(position);
                    break;
                case statics.UN_MARK:
                  unMarkFile(position);
                    break;
            }
    }

    private void unMarkFile(int position) {

        if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE){
            myLoadedList.remove(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.saveFileToMemory(myLoadedList);
            }
           List<recycleListFormat> arrayList=  files.getPdfFiles();
                for (recycleListFormat l : arrayList) {
                    if (l.file.getAbsolutePath().equals(list.get(position).file.getAbsolutePath())){
                        l.setTag(false);
                        files.setPdfList(arrayList);
                        break;
                    }
                }
            list.remove(position);
        }
        else {
            list.get(position).setTag(false);
        }
        adapter.notifyDataSetChanged();
        toast("UnMarked");
    }


    private void markFile(int position) {

        recycleListFormat newRow=new recycleListFormat(new File(list.get(position).file.getAbsolutePath())
                ,list.get(position).getId());
        newRow.setTag(true);
        myLoadedList.add(newRow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.saveFileToMemory(myLoadedList);
        }
        list.get(position).setTag(true);
        files.setPdfList(list);
        toast("Marked");

    }

    public void toast(String text){
            Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }
}