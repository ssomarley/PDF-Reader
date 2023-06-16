package com.mallmo.pdf_reader.FileFragments;

import static com.mallmo.pdf_reader.statics.EXCEL_STATE;
import static com.mallmo.pdf_reader.statics.PDF_STATE;
import static com.mallmo.pdf_reader.statics.WORD_STATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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
import com.mallmo.pdf_reader.MainFragments.bookMarked;
import com.mallmo.pdf_reader.MainFragments.myFiles;
import com.mallmo.pdf_reader.R;
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
    List<recycleListFormat> filteredList=new ArrayList<>();
    public List<Integer> filteredListPos = new ArrayList<>();



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


        myFiles.binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    myFiles.binding.searchView.setBackgroundResource(R.drawable.tab_back);
                } else {
                    myFiles.binding.searchView.setBackground(null);
                }
            }
        });
        bookMarked.binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    bookMarked.binding.searchView.setBackgroundResource(R.drawable.tab_back);
                } else {
                    bookMarked.binding.searchView.setBackground(null);
                }
            }
        });

        return binding.getRoot();

    }

    private void searchFile(String Text) {
        filteredList.clear();
        filteredListPos.clear();
        for (recycleListFormat file : list) {
            if (file.file.getName().toLowerCase().contains(Text.toLowerCase())) {
                filteredList.add(file);
                setFilteredList(filteredList);
                filteredListPos.add(list.indexOf(file));
            }
        }
        adapter.setFilterList(filteredList);
    }
    //click event for every item
    @Override
    public void onItemClick(int position) {
        File myfile;
        if (getFilteredList().size()!=0){
            myfile=filteredList.get(position).getFile();

        }else{
            myfile=list.get(position).getFile();
        }

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

    @SuppressLint("NotifyDataSetChanged")
    private void unMarkFile(int position) {
        if (MainActivity.FLAG == MainActivity.MY_BOOKMARKED_STATE) {
            List<recycleListFormat> arrayList = files.getPdfFiles();
            if (getFilteredList().size() != 0) {
                int i = filteredListPos.get(position);
                myLoadedList.remove(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.saveFileToMemory(myLoadedList);
                }
                filteredList.remove(position);

                for (recycleListFormat l : arrayList) {
                    if (l.file.getAbsolutePath().equals(list.get(filteredListPos.get(position)).file.getAbsolutePath())) {
                        l.setTag(false);
                        files.setPdfList(arrayList);
                        break;
                    }
                }
                list.remove(i);

            } else {

                myLoadedList.remove(position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.saveFileToMemory(myLoadedList);
                }
                for (recycleListFormat l : arrayList) {
                    if (l.file.getAbsolutePath().equals(list.get(position).file.getAbsolutePath())) {
                        l.setTag(false);
                        files.setPdfList(arrayList);
                        break;
                    }
                }
                list.remove(position);
            }
        } else {

            if (getFilteredList().size() != 0) {
                int i = filteredListPos.get(position);
                filteredList.remove(position);
                list.get(i).setTag(false);
                adapter.setFilterList(filteredList);

                files.getExcelFiles().get(filteredListPos.get(position)).setTag(false);
                MainActivity.setGetFiles(files);
                for (recycleListFormat file : myLoadedList) {
                    if (list.get(filteredListPos.get(position)).file.getAbsolutePath().equals(file.getFile().getAbsolutePath())) {
                        myLoadedList.remove(file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            config.saveFileToMemory(myLoadedList);
                        }
                        break;
                    }
                }
            } else {

                list.get(position).setTag(false);
                for (recycleListFormat file : myLoadedList) {
                    if (list.get(position).file.getAbsolutePath().equals(file.getFile().getAbsolutePath())) {
                        myLoadedList.remove(file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            config.saveFileToMemory(myLoadedList);
                        }
                        break;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        toast("UnMarked");
    }

    private void markFile(int position) {
        List<recycleListFormat> myFilteredList;
        recycleListFormat newRow;
        if (getFilteredList().size() != 0) {
            myFilteredList = getFilteredList();
            newRow = new recycleListFormat(new File(myFilteredList.get(position).file.getAbsolutePath())
                    , myFilteredList.get(position).getId());
            list.get(filteredListPos.get(position)).setTag(true);
        } else {
            newRow = new recycleListFormat(new File(list.get(position).file.getAbsolutePath())
                    , list.get(position).getId());
            list.get(position).setTag(true);
        }

        newRow.setTag(true);
        myLoadedList.add(newRow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.saveFileToMemory(myLoadedList);
        }
        files.setPdfList(list);
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

        myFiles.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchFile(newText);

                return true;
            }
        });
        bookMarked.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchFile(newText);

                return true;
            }
        });
    }

    public List<recycleListFormat> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<recycleListFormat> filteredList) {
        this.filteredList = filteredList;
    }
}