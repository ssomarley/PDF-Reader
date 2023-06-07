package com.mallmo.pdf_reader.ShowFIles;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.MainFragments.bookMarked;
import com.mallmo.pdf_reader.MainFragments.myFiles;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.databinding.FragmentPdfShowBinding;

import java.io.File;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link showPDFfiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showPDFfiles extends Fragment {
    FragmentPdfShowBinding binding;
    private PDFView pdfView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static final String FLAG_KEY="myFlagKey";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mFlag;

    public showPDFfiles() {
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
    public static showPDFfiles newInstance(String param1, int flag) {
        showPDFfiles fragment = new showPDFfiles();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(FLAG_KEY,flag);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mFlag = getArguments().getInt(FLAG_KEY);
        }
        MainActivity.binding.coordinatorLayout.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPdfShowBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        pdfView=view.findViewById(binding.pdfViewr.getId());
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        if ((mFlag == MainActivity.MY_FILES_STATE) | mFlag==MainActivity.MY_FLOUT_BUTTON_STATE){
                            myFiles fragment=new myFiles();
                            FragmentTransaction transaction= requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setReorderingAllowed(true);
                            transaction.replace(R.id.frame,fragment);
                            transaction.commit();
                            MainActivity.binding.coordinatorLayout.setVisibility(View.VISIBLE);
                        } else if (mFlag==MainActivity.MY_BOOKMARKED_STATE) {
                            bookMarked fragment=new bookMarked();
                            FragmentTransaction transaction=requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setReorderingAllowed(true);
                            transaction.replace(R.id.frame,fragment);
                            transaction.commit();
                            MainActivity.binding.coordinatorLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mParam1 !=null){
            if (mFlag ==MainActivity.MY_FLOUT_BUTTON_STATE){
                pdfView.fromUri(Uri.parse(mParam1)).load();
            } else  {
                File pdfFile=new File(mParam1);
                pdfView.fromFile(pdfFile).load();
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

}