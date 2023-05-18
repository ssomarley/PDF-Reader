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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myFiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myFiles extends Fragment {
private filesViewPagerAdapter adapter;
private int[] tabsBackground;
private int[] statusBackground;
private int[] tabsIcon;

FragmentFilesBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myFiles() {
        // Required empty public constructor
    }


    public static myFiles newInstance(String param1, String param2) {
        myFiles fragment = new myFiles();
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
        MainActivity.FLAG=MainActivity.MY_FILES_STATE;
        MainActivity.binding.imgFile.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        MainActivity.binding.imgMarked.setColorFilter(getResources().getColor(R.color.DeActive));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFilesBinding.inflate(inflater,container,false);
        tabsBackground= new int[]{R.color.pdf_back, R.color.excell_back, R.color.word_back};
        statusBackground=new int[]{R.color.pdf_status,R.color.excell_status,R.color.word_status};
        tabsIcon=new int[]{R.drawable.p,R.drawable.x,R.drawable.w};


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