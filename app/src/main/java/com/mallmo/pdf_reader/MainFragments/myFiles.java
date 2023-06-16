package com.mallmo.pdf_reader.MainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.mallmo.pdf_reader.Adapters.filesViewPagerAdapter;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.databinding.FragmentFilesBinding;


public class myFiles extends Fragment {
private filesViewPagerAdapter adapter;
private int[] tabsBackground;
private int[] statusBackground;
private int[] tabsIcon;

public static FragmentFilesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.FLAG=MainActivity.MY_FILES_STATE;
        MainActivity.binding.imgFile.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        MainActivity.binding.imgMarked.setColorFilter(getResources().getColor(R.color.DeActive));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFilesBinding.inflate(inflater,container,false);
        tabsBackground= new int[]{R.color.pdf_back, R.color.word_status, R.color.excell_status};
        statusBackground=new int[]{R.color.pdf_status,R.color.word_status,R.color.excell_status};
        tabsIcon=new int[]{R.drawable.p,R.drawable.w,R.drawable.x};


        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        filesViewPagerAdapter adapter=new filesViewPagerAdapter( this, MainActivity.getFragments());
       String[] title=getResources().getStringArray(R.array.tabLayoutTitle);

        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLay,binding.viewPager
                ,(tab,position)->{
            tab.setText(title[position]);
            tab.setIcon(tabsIcon[position]);

        } ).attach();

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


}