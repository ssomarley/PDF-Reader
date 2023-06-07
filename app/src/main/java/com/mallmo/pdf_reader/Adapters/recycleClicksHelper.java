package com.mallmo.pdf_reader.Adapters;

import static com.mallmo.pdf_reader.MainActivity.getFiles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mallmo.pdf_reader.MainActivity;
import com.mallmo.pdf_reader.R;
import com.mallmo.pdf_reader.SavingFile.dataBaseHelper;
import com.mallmo.pdf_reader.databinding.BottomSheetLayoutBinding;
import com.mallmo.pdf_reader.getFiles;
import com.mallmo.pdf_reader.recycleListFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class recycleClicksHelper  {
    pdfRecyclAdapter adapter;
    public String filePath;
    public final File file;
    public List<recycleListFormat> myList;
    public List<recycleListFormat> myLoadedList;
    dataBaseHelper dataBaseHelper=new dataBaseHelper(MainActivity.instance);
    getFiles myFinalFiles;
    long id;
  public int position;
    public recycleClicksHelper(String filePath , List<recycleListFormat> myList,
                               List<recycleListFormat> myLoadedList, int position,pdfRecyclAdapter adapter) {
        //this.context=context;
        this.filePath = filePath;
        file=new File(filePath);
        this.myList=myList;
        this.myLoadedList=myLoadedList;
        this.position=position;
        this.adapter=adapter;
        myFinalFiles=MainActivity.getGetFiles();
        id=myFinalFiles.getPdfFiles().get(myFinalFiles.getPdfFiles().size()-1).getId();
    }

    Intent intent;
    @SuppressLint("ResourceAsColor")
    public void bookmarkClick(){

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
        if (file.getAbsoluteFile().exists()){
            AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.instance);
            alert.setTitle("Rename To ...");
            EditText ed=new EditText(MainActivity.instance);
            String fileName=file.getName();
            fileName=fileName.substring(0,fileName.lastIndexOf("."));
            ed.setText(fileName);
            alert.setView(ed);
            ed.requestFocus();
            alert.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String parentFile= Objects.requireNonNull(file.getParentFile()).getAbsolutePath();
                    String fileExt=file.getAbsolutePath();
                    fileExt=fileExt.substring(fileExt.lastIndexOf("."));
                    String newPath=parentFile + "/" + ed.getText().toString() + fileExt;
                    File newFile=new File(newPath);
                    String oldFileName=file.getAbsoluteFile().getName();
                    String oldFilePath= Objects.requireNonNull(file.getAbsoluteFile().getParentFile()).getAbsolutePath();



                    boolean reName=file.renameTo(newFile);

                    if (reName){


                        ContentResolver resolver=MainActivity.instance.getContentResolver();
                        resolver.delete(MediaStore.Files.getContentUri("external"),
                                MediaStore.MediaColumns.DATA + "=?",new String[]
                                        {file.getAbsolutePath()});
                        intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(newFile));
                        MainActivity.instance.sendBroadcast(intent);

                        recycleListFormat form =new recycleListFormat(newFile,++id);



                        for (recycleListFormat obj : myLoadedList) {
                            if (obj.file.getName().equals(oldFileName) &&
                                    oldFilePath.equals(Objects.requireNonNull(obj.file.getAbsoluteFile().getParentFile()).getAbsolutePath())) {
                                myLoadedList.remove(obj);
                                form.tag = true;
                                myLoadedList.add(form);
                                myList.add(form);
                                myFinalFiles.getPdfFiles().remove(obj);
                                MainActivity.setGetFiles(myFinalFiles);



//                                 Log.i("id", obj.getId() + "\t " );
//                                for (recycleListFormat format : myFinalFiles.getPdfFiles()) {
//                                    if (format.getId()==obj.getId()){
//                                        Toast.makeText(MainActivity.instance, "in", Toast.LENGTH_LONG).show();
//
//                                        myFinalFiles.getPdfFiles().add(form);
//                                        myFinalFiles.getPdfFiles().remove(format);
//
//                                        MainActivity.setGetFiles(myFinalFiles);
//                                    }
//                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    dataBaseHelper.saveFileToMemory(myLoadedList);
                                }

                                adapter.setMyList(myList);
                                Toast.makeText(MainActivity.instance, "renamed", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        myList.remove(myList.get(position));
                        myList.add(form);
                        adapter.setMyList(myList);
                            Toast.makeText(MainActivity.instance, "renamed", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        Toast.makeText(MainActivity.instance, "not renamed", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        alert.create().show();

        }

        File newFile=file.getParentFile();
        file.renameTo(newFile);
        myList.remove(myList.get(position));
        adapter.setMyList(myList);
    }

    private void shareItem() {
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        if (file.getAbsoluteFile().exists()){
            intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("application/pdf");
           MainActivity.instance.startActivity(intent);
        }


    }



    @SuppressLint("NotifyDataSetChanged")
    private void deleteItem() {
        if (file.getAbsoluteFile().exists()){
           if (myList==null | myLoadedList==null ){
               myList=new ArrayList<>();
               myLoadedList=new ArrayList<>();
           }else {

               if (MainActivity.FLAG==MainActivity.MY_BOOKMARKED_STATE){
                  getFiles myFinalFiles=MainActivity.getGetFiles();
                  for (recycleListFormat form:myFinalFiles.getPdfFiles()){
                      if (form.getId()==myList.get(position).getId()){

                          myFinalFiles.getPdfFiles().remove(form);
                          MainActivity.setGetFiles(myFinalFiles);

                      }
                  }
               }
               myList.remove(myList.get(position));

               for (recycleListFormat obj : myLoadedList) {
                   if (obj.file.getAbsoluteFile().getName().equals(file.getAbsoluteFile().getName()) &&
                           obj.file.getAbsolutePath().equals(file.getAbsolutePath())){

                       myLoadedList.remove(obj);
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                           dataBaseHelper.saveFileToMemory(myLoadedList);
                       }
                   }
               }
           }

            file.delete();

            adapter.setMyList(myList);
            Toast.makeText(MainActivity.instance, "File Were Deleted", Toast.LENGTH_SHORT).show();

        }
    }

    public void fff(){
        getFiles myFinalFiles=MainActivity.getGetFiles();
        for (recycleListFormat form:myFinalFiles.getPdfFiles()){
            if (form.getId()==myList.get(position).getId()){

                myFinalFiles.getPdfFiles().remove(form);
                MainActivity.setGetFiles(myFinalFiles);

            }
        }
    }
}
