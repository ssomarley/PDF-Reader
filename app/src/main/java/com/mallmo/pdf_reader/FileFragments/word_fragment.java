package com.mallmo.pdf_reader.FileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallmo.pdf_reader.Adapters.onItemListener;
import com.mallmo.pdf_reader.Adapters.pdfRecyclAdapter;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.sharedPreffConfig;
import com.mallmo.pdf_reader.databinding.FragmentExcelFragmentBinding;
import com.mallmo.pdf_reader.databinding.FragmentMyFragment1Binding;
import com.mallmo.pdf_reader.databinding.FragmentWordFragmentBinding;
import com.mallmo.pdf_reader.getFiles;
import com.mallmo.pdf_reader.recyclPdf;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class word_fragment extends Fragment implements onItemListener {
    FragmentWordFragmentBinding binding;
    ArrayList<recyclPdf> list=new ArrayList<>();
    private int mflag;

    public static word_fragment newInstance(int flag) {
        word_fragment fragment = new word_fragment();
        Bundle args = new Bundle();
        args.putInt(statics.KEY, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mflag=getArguments().getInt(statics.KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentWordFragmentBinding.inflate(inflater,container,false);
        getFiles files=new getFiles();
        files.getAllFiles(Environment.getExternalStorageDirectory());



        if (MainActivity.FLAG==MainActivity.MY_FILES_STATE){

          //  list= (ArrayList<File>) files.getWordFiles();

        } else if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE) {

           // loadingFileFromStorage();

        }

        sharedPreffConfig config=new sharedPreffConfig(getContext());
        List<recyclPdf> myFiles=config.loadingFiles();
        if (myFiles==null) myFiles=new ArrayList<>();
        pdfRecyclAdapter adapter=new pdfRecyclAdapter(list, this,myFiles);
        binding.recycl.setHasFixedSize(true);
        binding.recycl.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycl.setAdapter(adapter);

        return binding.getRoot();

    }

    @Override
    public void onItemClick(int position) {

    }
}