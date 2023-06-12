package com.mallmo.pdf_reader;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.mallmo.pdf_reader.SavingFile.pdfDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.wordDataBaseHelper;
import com.mallmo.pdf_reader.ShowFIles.showPDFfiles;
import com.mallmo.pdf_reader.databinding.ActivityMainBinding;
import com.mallmo.pdf_reader.databinding.IntroductionDialogBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static ActivityMainBinding binding;
    public static  getFiles getFiles;
    FragmentTransaction transaction;
    private ActivityResultLauncher<Intent> launcher;
    public permossionTaker permission;
    public static  int FLAG ;

    private ActivityResultLauncher<String> Filelauncher;

    public static MainActivity instance;
public static final int MY_FILES_STATE=101;
public static final int MY_BOOKMARKED_STATE=102;
public static final int MY_FLOUT_BUTTON_STATE=103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        instance=this;
        pdfDataBaseHelper helper=new pdfDataBaseHelper(this);
        wordDataBaseHelper whelper =new wordDataBaseHelper(this);
        excelDataBaseHelper exhelper =new excelDataBaseHelper(this);

//               helper.loadingFiles().clear();  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            helper.saveFileToMemory(new ArrayList<>());
//        }

        launcher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    if (Environment.isExternalStorageManager()) {
                                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                                        new myAsyntask().execute();
                                    }

                                } else
                                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        });

        // marboot be load kardan file az tarighe float button
        Filelauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    String filePath=result.getPath();

                    if (filePath.toLowerCase().endsWith(".pdf")){

                        showPDFfiles showFile=showPDFfiles.newInstance(result.toString(),MY_FLOUT_BUTTON_STATE);
                        transaction=getSupportFragmentManager().beginTransaction();
                        transaction.setReorderingAllowed(true);
                        transaction.replace(binding.frame.getId(),showFile,"");
                        transaction.commit();

                    } else if (filePath.toLowerCase().endsWith(".xlsx")) {

                    } else if (filePath.toLowerCase().endsWith(".docx")) {

                    }else {
                        Toast.makeText(MainActivity.this, "File Was Incompatible", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        permission= new permossionTaker(this, launcher);

       binding.btFloat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!permission.checkPermission()) {
                   showPermissionDialog();

               }else {
                   //load kardan file az tarighe intent
                     addFileFromBotton();

               }
           }
       });

       //check kardan permission
        if (!permission.checkPermission()) {
            showPermissionDialog();
        }
        else {
            new myAsyntask().execute();

        }


    }

    @SuppressLint("ResourceAsColor")
    public void showPermissionDialog() {


            IntroductionDialogBinding InBinding=IntroductionDialogBinding.inflate(getLayoutInflater());
            //gereftan size **********
            Point size= new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            Dialog dialog=new Dialog(this);
            dialog.setContentView(InBinding.getRoot());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setLayout((int) (0.9*size.x), (int) (0.7*size.y));
            dialog.setCanceledOnTouchOutside(false);
            InBinding.proceedBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    permission.requestPermission();
                    dialog.dismiss();
                }
            });

        dialog.show();
    }

    private void addFileFromBotton() {

        Filelauncher.launch("*/*");
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
                goTOFiles();
                break;
            case R.id.bt_library:
                 goToBookMarks();
                break;
        }
    }

    public void goTOFiles() {

        if (!permission.checkPermission()) {
                showPermissionDialog();
        }else {

            myFiles f=new myFiles();
            transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.frame.getId(),f);
            transaction.commit();
        }

    }

    private void goToBookMarks() {

        if (!permission.checkPermission()) {
            showPermissionDialog();
        } else {
            binding.imgMarked.setColorFilter(getResources().getColor(R.color.botomNaveSelected));
            binding.imgFile.setColorFilter(getResources().getColor(R.color.DeActive));

            bookMarked fragment = new bookMarked();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.frame.getId(), fragment);
            transaction.commit();
        }
    }

    public class myAsyntask extends AsyncTask<Void,Void,getFiles>{
        @Override
        protected void onPostExecute(getFiles getFiles) {
            super.onPostExecute(getFiles);
            setGetFiles(getFiles);
            binding.progressBar.setVisibility(View.GONE);
            binding.animationView.setVisibility(View.GONE);
            binding.loadingTxt.setVisibility(View.GONE);
            goTOFiles();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(View.VISIBLE);
            binding.loadingTxt.setVisibility(View.VISIBLE);
        }

        @Override
        protected getFiles doInBackground(Void... objects) {

            getFiles files=new getFiles();
            files.getAllFiles(Environment.getExternalStorageDirectory());


            return files;

        }
    }




    public static getFiles getGetFiles() {
        return getFiles;
    }

    public static void setGetFiles(com.mallmo.pdf_reader.getFiles getFiles) {
        MainActivity.getFiles = getFiles;
    }
}