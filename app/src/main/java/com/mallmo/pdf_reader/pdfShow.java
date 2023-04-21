package com.mallmo.pdf_reader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.mallmo.pdf_reader.databinding.FragmentPdfShowBinding;

import java.io.File;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pdfShow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pdfShow extends Fragment {
    FragmentPdfShowBinding binding;
    private PDFView pdfView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;


    public pdfShow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment pdfShow.
     */
    // TODO: Rename and change types and number of parameters
    public static pdfShow newInstance(String param1) {
        pdfShow fragment = new pdfShow();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPdfShowBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        pdfView=view.findViewById(binding.pdfViewr.getId());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File pdfFile=new File(mParam1);
        pdfView.fromFile(pdfFile)
                .load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}