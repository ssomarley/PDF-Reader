package com.mallmo.pdf_reader.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import com.mallmo.pdf_reader.SavingFile.pdfDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.wordDataBaseHelper;
import com.mallmo.pdf_reader.databinding.BottomSheetLayoutBinding;
import com.mallmo.pdf_reader.getFiles;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class recycleClicksHelper {

    private static final String TAG = "recycleClicksHelper";
    RecyclAdapter adapter;
    public List<recycleListFormat> myList;
    public List<recycleListFormat> myLoadedList;
    pdfDataBaseHelper dataBaseHelper = new pdfDataBaseHelper(MainActivity.instance);

    getFiles myFinalFiles;
    public getFiles getFiles;
    long id;
    public int position;
    public File file;
    public int action;

    public recycleClicksHelper(List<recycleListFormat> myList,
                               List<recycleListFormat> myLoadedList, int position, RecyclAdapter adapter
            , getFiles getFiles, int action) {
        //this.context=context;
        this.getFiles = getFiles;
        this.myList = myList;
        this.myLoadedList = myLoadedList;
        this.position = position;
        this.adapter = adapter;
        this.action = action;
        this.file = new File(myList.get(position).getFile().getAbsolutePath());
        myFinalFiles = MainActivity.getGetFiles();
        id = myFinalFiles.getPdfFiles().get(myFinalFiles.getPdfFiles().size() - 1).getId();
    }

    Intent intent;

    @SuppressLint("ResourceAsColor")
    public void bookmarkClick() {

        BottomSheetLayoutBinding BtBinding = BottomSheetLayoutBinding.inflate(MainActivity.instance.getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.instance, R.style.bottomSheetStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(BtBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        if (file.getAbsoluteFile().exists()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.instance);
            alert.setTitle("Rename To ...");
            EditText ed = new EditText(MainActivity.instance);
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            ed.setText(fileName);
            alert.setView(ed);
            ed.requestFocus();
            alert.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String parentFile = Objects.requireNonNull(file.getParentFile()).getAbsolutePath();
                    String fileExt = file.getAbsolutePath();
                    fileExt = fileExt.substring(fileExt.lastIndexOf("."));
                    String newPath = parentFile + "/" + ed.getText().toString() + fileExt;
                    File newFile = new File(newPath);
                    String oldFileName = file.getAbsoluteFile().getName();
                    String oldFilePath = Objects.requireNonNull(file.getAbsoluteFile().getParentFile()).getAbsolutePath();
                    boolean reName = file.renameTo(newFile);

                    if (reName) {


                        ContentResolver resolver = MainActivity.instance.getContentResolver();
                        resolver.delete(MediaStore.Files.getContentUri("external"),
                                MediaStore.MediaColumns.DATA + "=?", new String[]
                                        {file.getAbsolutePath()});
                        intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(newFile));
                        MainActivity.instance.sendBroadcast(intent);

                        recycleListFormat form = new recycleListFormat(newFile, ++id);
                        if (MainActivity.FLAG == MainActivity.MY_BOOKMARKED_STATE) {
                                form.setTag(true);

                            switch (action) {
                                case statics.PDF_STATE:
                                    for (recycleListFormat format : getFiles.getPdfFiles()) {
                                        if (format.file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())) {
                                            getFiles.getPdfFiles().remove(format);
                                            getFiles.getPdfFiles().add(form);
                                            MainActivity.setGetFiles(getFiles);
                                            myList.remove(position);
                                            break;
                                        }
                                    }
                                    break;
                                case statics.WORD_STATE:

                                    for (recycleListFormat format : getFiles.getWordFiles()) {

                                        if (format.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {

                                            getFiles.getWordFiles().remove(format);
                                            getFiles.getWordFiles().add(form);
                                            MainActivity.setGetFiles(getFiles);
                                            myList.remove(position);
                                            break;
                                        }

                                    }

                                    break;
                                case statics.EXCEL_STATE:
                                    for (recycleListFormat format : getFiles.getExcelFiles()) {
                                        if (format.file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())) {
                                            getFiles.getExcelFiles().remove(format);
                                            getFiles.getExcelFiles().add(form);
                                            MainActivity.setGetFiles(getFiles);
                                            myList.remove(position);

                                            break;
                                        }
                                    }
                                    break;
                            }


                            myList.add(position, form);
                            myLoadedList.remove(position);
                            myLoadedList.add(position, form);
                            setMyLoadedList(myLoadedList);
                            adapter.notifyDataSetChanged();

                        } else {
                            if (myList.get(position).getTag()) {

                                for (recycleListFormat item : myLoadedList) {

                                    if (item.file.getAbsolutePath().equals(file.getAbsolutePath())) {

                                        form.setTag(true);
                                        myLoadedList.remove(item);
                                        myLoadedList.add(form);
                                        myList.add(form);
                                        myList.remove(position);
                                        setMyList(myList);
                                        setMyLoadedList(myLoadedList);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            } else {
                                myList.remove(position);
                                myList.add(position, form);

                                setMyList(myList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(MainActivity.instance, "Renamed", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(MainActivity.instance, "not renamed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            alert.create().show();
        }
    }

    private void shareItem() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        if (file.getAbsoluteFile().exists()) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("application/pdf");
            MainActivity.instance.startActivity(intent);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private void deleteItem() {
        // File newFile = myList.get(position).getFile();
        if (!file.exists()) {
            Toast.makeText(MainActivity.instance, "file was naot found", Toast.LENGTH_SHORT).show();
            return;
//           if (myList==null | myLoadedList==null ){
//               myList=new ArrayList<>();
//               myLoadedList=new ArrayList<>();
        } else {

            if (MainActivity.FLAG == MainActivity.MY_BOOKMARKED_STATE) {
                myLoadedList.remove(myLoadedList.get(position));
                setMyLoadedList(myLoadedList);
                switch (action) {
                    case statics.PDF_STATE:
                        for (recycleListFormat form : getFiles.getPdfFiles()) {
                            if (form.file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())) {
                                getFiles.getPdfFiles().remove(form);
                                MainActivity.setGetFiles(getFiles);
                                myList.remove(position);
                                break;
                            }
                        }
                        break;
                    case statics.WORD_STATE:

                        for (recycleListFormat form : getFiles.getWordFiles()) {

                            if (form.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                                getFiles.getWordFiles().remove(form);
                                MainActivity.setGetFiles(getFiles);
                                break;
                            }

                        }

                        break;
                    case statics.EXCEL_STATE:
                        for (recycleListFormat form : getFiles.getExcelFiles()) {
                            if (form.file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())) {
                                getFiles.getExcelFiles().remove(form);
                                MainActivity.setGetFiles(getFiles);
                                myList.remove(position);

                                break;
                            }
                        }
                        break;
                }

            } else {

                if (myList.get(position).getTag()) {

                    for (recycleListFormat form : myLoadedList) {
                        if (form.file.getAbsolutePath().equals(myList.get(position).file.getAbsolutePath())) {
                            myLoadedList.remove(form);
                            myList.remove(myList.get(position));
                            setMyList(myList);
                            setMyLoadedList(myLoadedList);
                            break;
                        }
                    }
                } else {

                    myList.remove(myList.get(position));
                    getFiles.setPdfList(myList);
                    setMyList(myList);
                }
            }
        }

        file.delete();
        Toast.makeText(MainActivity.instance, "File Were Deleted", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();

    }

    public List<recycleListFormat> getMyList() {
        return myList;
    }

    public void setMyList(List<recycleListFormat> myList) {
        switch (action) {
            case statics.PDF_STATE:
                getFiles.setPdfList(myList);
                break;
            case statics.WORD_STATE:
                getFiles.setWordList(myList);
                break;
            case statics.EXCEL_STATE:
                getFiles.setExelList(myList);
                break;
        }
        this.myList = myList;
    }

    public List<recycleListFormat> getMyLoadedList() {

        return myLoadedList;
    }

    @SuppressLint("NewApi")
    public void setMyLoadedList(List<recycleListFormat> myLoadedList) {

        switch (action) {
            case statics.PDF_STATE:
                dataBaseHelper.saveFileToMemory(myLoadedList);
                break;
            case statics.WORD_STATE:

                this.myList = myLoadedList;
                wordDataBaseHelper wordDataBaseHelper = new wordDataBaseHelper(MainActivity.instance);
                wordDataBaseHelper.saveFileToMemory(myLoadedList);
                break;
            case statics.EXCEL_STATE:
                excelDataBaseHelper excelDataBaseHelper = new excelDataBaseHelper(MainActivity.instance);
                excelDataBaseHelper.saveFileToMemory(myLoadedList);
                break;
        }
        this.myLoadedList = myLoadedList;
    }

}
