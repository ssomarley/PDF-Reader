package com.mallmo.pdf_reader.FileFragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.sharedPreffConfig;
import com.mallmo.pdf_reader.databinding.FragmentMyFragment1Binding;
import com.mallmo.pdf_reader.Adapters.onItemListener;
import com.mallmo.pdf_reader.Adapters.pdfRecyclAdapter;
import com.mallmo.pdf_reader.ShowFIles.showPDFfiles;
import com.mallmo.pdf_reader.getFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pdf_fragment extends Fragment implements onItemListener {
ArrayList<File> list=new ArrayList<>();
private int mflag;
    FragmentMyFragment1Binding binding;
    public static final String KEY="MY ARRAY KEY";

    public static pdf_fragment newInstance(int flag) {
        pdf_fragment fragment =new pdf_fragment();
        Bundle args = new Bundle();
        args.putInt(KEY, flag);
        fragment.setArguments(args);
        return fragment;
    }

    //'k;l
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            mflag=getArguments().getInt(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentMyFragment1Binding.inflate(inflater,container,false);
        getFiles files=new getFiles();
        files.getAllFiles(Environment.getExternalStorageDirectory());

        //iiii

        if (MainActivity.FLAG==MainActivity.MY_FILES_STATE){

            list= (ArrayList<File>) files.getPdfFiles();

        } else if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE) {

            loadingFileFromStorage();

        }

        sharedPreffConfig config=new sharedPreffConfig(getContext());
        List<File> myFiles=config.loadingFiles();
        if (myFiles==null) myFiles=new ArrayList<>();
        pdfRecyclAdapter adapter=new pdfRecyclAdapter(list, this,myFiles);
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
       String path=list.get(position).getAbsolutePath();
       showPDFfiles pdfFragmemt= showPDFfiles.newInstance(path ,MainActivity.FLAG);

        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
         transaction.replace(R.id.frame,pdfFragmemt,"");
         transaction.commit();


    }

    private void loadingFileFromStorage(){


        list.clear();
        sharedPreffConfig config=new sharedPreffConfig(getContext());
        if (config.loadingFiles() ==null){
            list=new ArrayList<>();

        }
        else {
            list= (ArrayList<File>) config.loadingFiles();
        }
    }
}