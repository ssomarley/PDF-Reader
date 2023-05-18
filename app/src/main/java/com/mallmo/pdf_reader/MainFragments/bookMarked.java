package com.mallmo.pdf_reader.MainFragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.mallmo.pdf_reader.Adapters.filesViewPagerAdapter;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.databinding.FragmentBookMarkedBinding;


public class bookMarked extends Fragment {
FragmentBookMarkedBinding binding;
private int[] tabsBackground;
private int[] statusBackground;
private int[] tabsIcon =tabsIcon=new int[]{R.drawable.p,R.drawable.x,R.drawable.w};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



    public static bookMarked newInstance(String param1, String param2) {
        bookMarked fragment = new bookMarked();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //****marboot be onBackPress ***
        MainActivity.FLAG=MainActivity.MY_BOOKMARKED_STATE;

        //*** faal kardan botton haye nav ****
        MainActivity.binding.imgMarked.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        MainActivity.binding.imgFile.setColorFilter(getResources().getColor(R.color.DeActive));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentBookMarkedBinding.inflate(inflater,container,false);
        tabsBackground= new int[]{R.color.pdf_back, R.color.excell_back, R.color.word_back};
        statusBackground=new int[]{R.color.pdf_status,R.color.excell_status,R.color.word_status};



        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        filesViewPagerAdapter adapter=new filesViewPagerAdapter( this, MainActivity.getFragments());
        String[] title=getResources().getStringArray(R.array.tabLayoutTitle);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLay,binding.viewPager
                ,(tab,position)->{tab.setText(title[position]);
                    tab.setIcon(tabsIcon[position]);
        }).attach();


        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLay. setBackgroundResource(tabsBackground[position]);
                binding.toolBar.setBackgroundResource(tabsBackground[position]);
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),statusBackground[position]));

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}