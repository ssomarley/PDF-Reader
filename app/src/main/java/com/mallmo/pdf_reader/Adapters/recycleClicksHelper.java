package com.mallmo.pdf_reader.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.databinding.BottomSheetLayoutBinding;

import java.io.File;

public class recycleClicksHelper  {
    public String filePath;
    public File file;
  // public Context context;
    public recycleClicksHelper(String filePath) {
        //this.context=context;
        this.filePath = filePath;
        file=new File(filePath);

    }

    Intent intent;
    @SuppressLint("ResourceAsColor")
    public void bookmarkClick(int posotion){

        BottomSheetLayoutBinding BtBinding=BottomSheetLayoutBinding.inflate(MainActivity.instance.getLayoutInflater());
        BottomSheetDialog dialog=new BottomSheetDialog(MainActivity.instance, R.style.bottomSheetStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(BtBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.DeActive));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        BtBinding.deleteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();

                dialog.dismiss();
            }
        });
        
        BtBinding.shareLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareItem();
                dialog.dismiss();
            }
        });
        
        BtBinding.renameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            renameItem();
            dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void renameItem() {
        if (file!=null && file.getAbsoluteFile().exists()){
            file.renameTo(new File("hossein has chende this "));
        }
    }

    private void shareItem() {
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        if (file!=null && file.getAbsoluteFile().exists()){
            intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("application/pdf");
           MainActivity.instance.startActivity(intent);
        }


    }


    private void deleteItem() {
    }
}
