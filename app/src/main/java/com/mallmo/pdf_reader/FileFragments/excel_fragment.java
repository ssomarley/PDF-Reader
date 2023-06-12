package com.mallmo.pdf_reader.FileFragments;

import static com.mallmo.pdf_reader.statics.EXCEL_STATE;
import static com.mallmo.pdf_reader.statics.PDF_STATE;
import static com.mallmo.pdf_reader.statics.WORD_STATE;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mallmo.pdf_reader.Adapters.onItemListener;
import com.mallmo.pdf_reader.Adapters.onItemSaveClickListtener;
import com.mallmo.pdf_reader.Adapters.RecyclAdapter;
import com.mallmo.pdf_reader.Adapters.onOptionClickListner;
import com.mallmo.pdf_reader.Adapters.recycleClicksHelper;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.databinding.FragmentExcelFragmentBinding;
import com.mallmo.pdf_reader.getFiles;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class excel_fragment extends Fragment implements onItemListener , onItemSaveClickListtener, onOptionClickListner {
    FragmentExcelFragmentBinding binding;
    ArrayList<recycleListFormat> list=new ArrayList<>();
    private int mflag;
    RecyclAdapter adapter;
    excelDataBaseHelper config;
    public getFiles files;
    List<recycleListFormat> myLoadedList;


    public static excel_fragment newInstance(int flag) {
        excel_fragment fragment = new excel_fragment();
        Bundle args = new Bundle();
        args.putInt(statics.KEY, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statics.FILE_FLAG= PDF_STATE;

        if (getArguments() != null) {
            mflag=getArguments().getInt(statics.KEY);
        }
        config=new excelDataBaseHelper(getContext());
        myLoadedList =config.loadingFiles();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentExcelFragmentBinding.inflate(inflater,container,false);

        files=MainActivity.getGetFiles();
        if (MainActivity.FLAG==MainActivity.MY_FILES_STATE){
           list= (ArrayList<recycleListFormat>) files.getExcelFiles();
        } else if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE) {
            loadingFileFromStorage();
        }


        if (myLoadedList==null) myLoadedList=new ArrayList<>();
         adapter=new RecyclAdapter(list, this,myLoadedList,this,this,EXCEL_STATE);
        binding.recycl.setHasFixedSize(true);
        binding.recycl.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycl.setAdapter(adapter);

        return binding.getRoot();

    }
    //click event for every item
    @Override
    public void onItemClick(int position) {
        File myfile=list.get(position).getFile();
        Uri uri=Uri.fromFile(myfile);
        String mime=getContext().getContentResolver().getType(uri);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setType(mime);
        getContext().startActivity(Intent.createChooser(intent,"Open with ...?"));
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
            List<recycleListFormat> arrayList=  files.getExcelFiles();
            for (recycleListFormat l : arrayList) {
                if (l.file.getAbsolutePath().equals(list.get(position).file.getAbsolutePath())){
                    l.setTag(false);
                    files.setExelList(arrayList);
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

        recycleListFormat newFile=new recycleListFormat(new File(list.get(position).file.getAbsolutePath())
                ,list.get(position).getId());
        newFile.setTag(true);
        myLoadedList.add(newFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.saveFileToMemory(myLoadedList);
        }
        list.get(position).setTag(true);
        files.setExelList(list);
        toast("Marked");

    }

    public void toast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }

    //loading file from storage
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
    public void onOptionClick(int position) {
        recycleClicksHelper helper=new recycleClicksHelper(list,myLoadedList,position,adapter,files, EXCEL_STATE);
        helper.bookmarkClick();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
    @Override
    public void onResume() {
        super.onResume();
        statics.POPUP_STATE= EXCEL_STATE;
    }
}