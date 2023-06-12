package com.mallmo.pdf_reader.SavingFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mallmo.pdf_reader.recycleListFormat;
import com.mallmo.pdf_reader.statics;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class pdfDataBaseHelper {

    public Context context;
    public SharedPreferences preferences;
    public pdfDataBaseHelper(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(statics.SHARED_KEY, Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveFileToMemory(List<recycleListFormat> fileList){

        Gson json=new Gson();
        List<fileDetails> fileObj=new ArrayList<>();
        if (fileList !=null){
            fileList.forEach(file ->  fileObj.add(new fileDetails(file.file.getName(),file.file.getAbsolutePath(), file.getId())));
        }


        String fileString=json.toJson(fileObj);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(statics.SHERED_EDITOE_KEY,fileString);
        editor.apply();


    }

    public List<recycleListFormat> loadingFiles(){
        List<fileDetails> fileDetailsList=new ArrayList<>();
        List<recycleListFormat> fileList=new ArrayList<>();
        String path= preferences.getString(statics.SHERED_EDITOE_KEY,"");
        Gson json=new Gson();
        Type type=new TypeToken<List<fileDetails>>(){}.getType();

        fileDetailsList=json.fromJson(path,type);
        if (fileDetailsList !=null ){
            for(fileDetails file :fileDetailsList){
                fileList.add(new recycleListFormat(new File(file.filePath),file.id));

            }
        }

        return fileList;
    }
}
