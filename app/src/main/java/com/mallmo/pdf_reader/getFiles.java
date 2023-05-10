package com.mallmo.pdf_reader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class getFiles {

    public  List<File> wordList=new ArrayList<>();
    public  List<File> pdfList=new ArrayList<>();

    public  List<File> exelList=new ArrayList<>();



    public void getAllFiles(File file ){

        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        if (files!=null){

            for (File singleFile:files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){

                   getAllFiles( singleFile);
                }else {
                    if ( singleFile.getName().toLowerCase().endsWith(".pdf")) {
                        arrayList.add(singleFile);
                        pdfList.add(singleFile);
                    }


                    else if (singleFile.getName().toLowerCase().endsWith(".xlsx")) {
                     exelList.add(singleFile);
                    }
                    else if (singleFile.getName().toLowerCase().endsWith(".docx")) {
                         wordList.add(singleFile);
                    }

                }
            }
        }



    }

    public List<File> getWordFiles(){
        return (wordList == null) ? new ArrayList<>() :wordList ;
    }
    
    public  List<File> getPdfFiles(){
        return  (pdfList == null) ? new ArrayList<>() :pdfList ;
    }

     public List<File> getExcelFiles(){
        return (exelList == null) ? new ArrayList<>() :exelList ;
    }
}
