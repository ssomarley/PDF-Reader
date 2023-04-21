package com.mallmo.pdf_reader;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.Permission;

public class permossionTaker {
    Context context;
    ActivityResultLauncher<Intent> luncher;
    private final String readPermission= READ_EXTERNAL_STORAGE;
    public permossionTaker(Context context , ActivityResultLauncher<Intent> luncher) {
        this.context = context;
        this.luncher=luncher;
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED ;
        }
    }
    public void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()){
                try {
                    Intent i =new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    i.addCategory("android.intent.category.DEFAULT");
                    i.setData(Uri.parse(String.format("package:%s"
                            ,context.getApplicationContext().getPackageName())));

                    luncher.launch(i);
                }catch (Exception i){
                    Intent i2 =new Intent();
                    i2.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);

                    luncher.launch(i2);
                }

            }
        }
        else {
            Dexter.withContext(context).withPermission(readPermission).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Toast.makeText(context, "Permission was Granted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showDiolog();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                }
            }).check();
        }
    }

    private void showDiolog() {
        new AlertDialog.Builder(context)
                .setTitle("Permission Denied...!")
                .setMessage("TO SHOW YOUR PDF FILES WE NEED YOUR PERMISSION.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }


}