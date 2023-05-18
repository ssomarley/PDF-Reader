package com.mallmo.pdf_reader;

import com.mallmo.pdf_reader.SavingFile.sharedPreffConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class getFiles {

    public  List<File> wordList=new ArrayList<>();
    public  List<recyclPdf> pdfList=new ArrayList<>();

    public  List<File> exelList=new ArrayList<>();
    public recyclPdf finalPdfList;


    public void getAllFiles(File file ){
        sharedPreffConfig config=new sharedPreffConfig(MainActivity.instance);
        List<recyclPdf> loadedList=config.loadingFiles();


        File[] files=file.listFiles();
        if (files!=null){

            for (File singleFile:files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){

                   getAllFiles( singleFile);
                }else {
                    if ( singleFile.getName().toLowerCase().endsWith(".pdf")) {

                        finalPdfList=new recyclPdf(singleFile);
                        if (loadedList !=null){
                            for (recyclPdf f:loadedList){
                                if (f.file.getAbsoluteFile().getName().equals(singleFile.getAbsoluteFile().getName()) &&
                               f.file.getAbsoluteFile().getAbsolutePath().equals(singleFile.getAbsoluteFile().getAbsolutePath())){
                                    finalPdfList.setTag(true);
                                }
                            }
                        }

                        pdfList.add(finalPdfList);

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
    
    public  List<recyclPdf> getPdfFiles(){
        return  (pdfList == null) ? new ArrayList<>() :pdfList ;
    }

     public List<File> getExcelFiles(){
        return (exelList == null) ? new ArrayList<>() :exelList ;
    }
}
