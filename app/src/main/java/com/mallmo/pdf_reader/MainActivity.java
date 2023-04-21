package com.mallmo.pdf_reader;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.mallmo.pdf_reader.MainFragments.files;
import com.mallmo.pdf_reader.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityMainBinding binding;
    FragmentTransaction transaction;
    public permossionTaker permission;
    private ActivityResultLauncher<Intent> launcher;


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


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_files:
                getFiles();
                break;

        }
        if (v == binding.btLibrary) {
            library();
        }


    }

    private void getFiles() {
        files f=new files();
        transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frame.getId(),f);
        transaction.commit();
    }

    private void library() {
        permission = new permossionTaker(this, launcher);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        } else {
            library_fragment fragment = new library_fragment();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.frame.getId(), fragment);
            transaction.commit();
        }
    }


}