package com.mallmo.pdf_reader.FileFragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;


import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.databinding.FragmentMyFragment1Binding;
import com.mallmo.pdf_reader.Adapters.onItemListener;
import com.mallmo.pdf_reader.Adapters.pdfRecyclAdapter;
import com.mallmo.pdf_reader.pdfShow;

import java.io.File;
import java.util.ArrayList;

public class pdf_fragment extends Fragment implements onItemListener {
ArrayList<File> list=new ArrayList<>();
    FragmentMyFragment1Binding binding;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentMyFragment1Binding.inflate(inflater,container,false);

        list =findSong(Environment.getExternalStorageDirectory());


         pdfRecyclAdapter adapter=new pdfRecyclAdapter(list, this);
         binding.recycl.setHasFixedSize(true);
         binding.recycl.setLayoutManager(new GridLayoutManager(getContext(),3));
         binding.recycl.setAdapter(adapter);

         return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        if (files!=null){


            for (File singleFile:files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(findSong(singleFile));
                }else {
                    if (
                            singleFile.getName().toLowerCase().endsWith(".pdf"))
                        arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onItemClick(int position) {
       String path=list.get(position).getAbsolutePath();

       pdfShow pdfFragmemt=pdfShow.newInstance(path);

        FragmentTransaction transaction=getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
         transaction.replace(R.id.frame,pdfFragmemt,"");
         transaction.commit();


    }
}