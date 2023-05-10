package com.mallmo.pdf_reader.MainFragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
<<<<<<< HEAD
import androidx.core.content.ContextCompat;
=======
>>>>>>> origin/master
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

<<<<<<< HEAD

public class bookMarked extends Fragment {
FragmentBookMarkedBinding binding;
private int[] tabsBackground;
private int[] statusBackground;
private int[] tabsIcon =tabsIcon=new int[]{R.drawable.p,R.drawable.w,R.drawable.x};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



=======
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bookMarked#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bookMarked extends Fragment {
FragmentBookMarkedBinding binding;
private int[] tabsBackground;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public bookMarked() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bookMarked.
     */
    // TODO: Rename and change types and number of parameters
>>>>>>> origin/master
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
<<<<<<< HEAD

        //****marboot be onBackPress ***
        MainActivity.FLAG=MainActivity.MY_BOOKMARKED_STATE;

        //*** faal kardan botton haye nav ****
        MainActivity.binding.imgMarked.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        MainActivity.binding.imgFile.setColorFilter(getResources().getColor(R.color.DeActive));
=======
>>>>>>> origin/master
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentBookMarkedBinding.inflate(inflater,container,false);
        tabsBackground= new int[]{R.color.pdf_back, R.color.excell_back, R.color.word_back};
<<<<<<< HEAD
        statusBackground=new int[]{R.color.pdf_status,R.color.excell_status,R.color.word_status};



=======
>>>>>>> origin/master
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
<<<<<<< HEAD
                ,(tab,position)->{tab.setText(title[position]);
                    tab.setIcon(tabsIcon[position]);
        }).attach();
=======
                ,(tab,position)-> tab.setText(title[position])).attach();
>>>>>>> origin/master


        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLay. setBackgroundResource(tabsBackground[position]);
                binding.toolBar.setBackgroundResource(tabsBackground[position]);
<<<<<<< HEAD
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),statusBackground[position]));
=======
>>>>>>> origin/master

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}