package com.mallmo.pdf_reader;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.mallmo.pdf_reader.FileFragments.excel_fragment;
import com.mallmo.pdf_reader.FileFragments.pdf_fragment;
import com.mallmo.pdf_reader.FileFragments.word_fragment;
import com.mallmo.pdf_reader.MainFragments.bookMarked;
import com.mallmo.pdf_reader.MainFragments.myFiles;
import com.mallmo.pdf_reader.SavingFile.fileDetails;
import com.mallmo.pdf_reader.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static ActivityMainBinding binding;
    FragmentTransaction transaction;
    public permossionTaker permission;
    public static  int FLAG ;
    private ActivityResultLauncher<Intent> launcher;
public static final int MY_FILES_STATE=101;
public static final int MY_BOOKMARKED_STATE=102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        launcher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    if (Environment.isExternalStorageManager()) {
                                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                                    }

                                } else
                                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        });



       binding.btFloat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity.this, "float btn", Toast.LENGTH_SHORT).show();
           }
       });
        permission = new permossionTaker(this, launcher);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        }

    }

   public static List<Fragment> getFragments() {
      List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new pdf_fragment());
        fragmentList.add(new word_fragment());
        fragmentList.add(new excel_fragment());

        return fragmentList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_files:
                getFiles();
                break;
            case R.id.bt_library:
                getBookMarked();
                break;
        }
    }

    public void getFiles() {
        permission = new permossionTaker(this, launcher);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        }else {
            //        binding.imgFile.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
//        binding.imgMarked.setColorFilter(getResources().getColor(R.color.DeActive));
            myFiles f=new myFiles();
            transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.frame.getId(),f);
            transaction.commit();
        }

    }

    private void getBookMarked() {
        binding.imgMarked.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
        binding.imgFile.setColorFilter(getResources().getColor(R.color.DeActive));
        permission = new permossionTaker(this, launcher);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        } else {
            bookMarked fragment = new bookMarked();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.frame.getId(), fragment);
            transaction.commit();
        }
    }



}