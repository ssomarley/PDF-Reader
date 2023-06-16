package com.mallmo.pdf_reader.MainFragments;

import static com.mallmo.pdf_reader.statics.EXCEL_STATE;
import static com.mallmo.pdf_reader.statics.PDF_STATE;
import static com.mallmo.pdf_reader.statics.POPUP_STATE;
import static com.mallmo.pdf_reader.statics.WORD_STATE;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.mallmo.pdf_reader.Adapters.filesViewPagerAdapter;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.pdfDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.wordDataBaseHelper;
import com.mallmo.pdf_reader.databinding.FragmentBookMarkedBinding;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.util.List;


public class bookMarked extends Fragment {
    public static FragmentBookMarkedBinding binding = FragmentBookMarkedBinding.inflate(LayoutInflater.from(MainActivity.instance), null, false);
    private int[] tabsBackground;
    private int[] statusBackground;
    private int[] tabsIcon = tabsIcon = new int[]{R.drawable.p, R.drawable.w, R.drawable.x};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //****marboot be onBackPress ***
        MainActivity.FLAG = MainActivity.MY_BOOKMARKED_STATE;

        //*** faal kardan botton haye nav ****
        MainActivity.binding.imgMarked.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        MainActivity.binding.imgFile.setColorFilter(getResources().getColor(R.color.DeActive));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookMarkedBinding.inflate(inflater, container, false);
        tabsBackground = new int[]{R.color.pdf_back, R.color.word_back, R.color.excell_back};
        statusBackground = new int[]{R.color.pdf_status, R.color.word_status, R.color.excell_status};

        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        filesViewPagerAdapter adapter = new filesViewPagerAdapter(this, MainActivity.getFragments());
        String[] title = getResources().getStringArray(R.array.tabLayoutTitle);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLay, binding.viewPager
                , (tab, position) -> {
            tab.setText(title[position]);
            tab.setIcon(tabsIcon[position]);
        }).attach();


        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLay.setBackgroundResource(tabsBackground[position]);
                binding.toolBar.setBackgroundResource(tabsBackground[position]);
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), statusBackground[position]));

            }
        });

        binding.btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), binding.btMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        clearList();
                        return false;


                    }
                });
                popupMenu.show();
            }
        });
    }

    private void clearList() {

        switch (statics.POPUP_STATE) {
            case PDF_STATE:
                pdfDataBaseHelper helper = new pdfDataBaseHelper(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<recycleListFormat> list = helper.loadingFiles();
                    list.clear();
                    helper.saveFileToMemory(list);
                    for (recycleListFormat file : MainActivity.getFiles.getPdfFiles()) {
                        if (file.getTag()) {
                            file.setTag(false);
                        }
                    }
                }
                toast(" pdf List cleared");
                break;
            case WORD_STATE:

                wordDataBaseHelper helper2 = new wordDataBaseHelper(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<recycleListFormat> list = helper2.loadingFiles();
                    list.clear();
                    helper2.saveFileToMemory(list);

                    for (recycleListFormat file : MainActivity.getFiles.getWordFiles()) {
                        if (file.getTag()) {
                            file.setTag(false);
                        }
                    }

                }
                toast("Word List cleared");
                break;
            case EXCEL_STATE:
                excelDataBaseHelper helper3 = new excelDataBaseHelper(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<recycleListFormat> list = helper3.loadingFiles();
                    list.clear();
                    helper3.saveFileToMemory(list);

                    for (recycleListFormat file : MainActivity.getFiles.getExcelFiles()) {
                        if (file.getTag()) {
                            file.setTag(false);
                        }
                    }

                }
                toast("Excel List cleared");
                break;

        }

        getParentFragmentManager().beginTransaction().replace(R.id.frame, new bookMarked()).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        binding = null;
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}