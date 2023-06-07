package com.mallmo.pdf_reader;

import com.mallmo.pdf_reader.SavingFile.dataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.excelDataBaseHelper;
import com.mallmo.pdf_reader.SavingFile.wordDataBaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public  class getFiles {

    public List<recycleListFormat> wordList=new ArrayList<>();
    public  List<recycleListFormat> pdfList=new ArrayList<>();

    public  List<recycleListFormat> exelList=new ArrayList<>();
    public  recycleListFormat finalList;
    public long id =0;

    public void setWordList(List<recycleListFormat> wordList) {
        this.wordList = wordList;
    }

    public void setPdfList(List<recycleListFormat> pdfList) {
        this.pdfList = pdfList;
    }

    public void setExelList(List<recycleListFormat> exelList) {
        this.exelList = exelList;
    }

    public  void getAllFiles(File file ){
        //load kardan file save shode
        dataBaseHelper config=new dataBaseHelper(MainActivity.instance);
        List<recycleListFormat> pdfLoadedList=config.loadingFiles();

        wordDataBaseHelper wconfig=new wordDataBaseHelper(MainActivity.instance);
        List<recycleListFormat> wordLoadedList=wconfig.loadingFiles();

        excelDataBaseHelper exConfig=new excelDataBaseHelper(MainActivity.instance);
        List<recycleListFormat> exLoadedList=exConfig.loadingFiles();


        File[] files=file.listFiles();
        if (files!=null){

            for (File singleFile:files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){

                   getAllFiles( singleFile);
                }else {
                    if ( singleFile.getName().toLowerCase().endsWith(".pdf")) {

                        finalList =new recycleListFormat(singleFile,++id);
                        if (pdfLoadedList !=null){
                            for (recycleListFormat f:pdfLoadedList){
                                if (f.file.getAbsoluteFile().getName().equals(singleFile.getAbsoluteFile().getName()) &&
                               f.file.getAbsoluteFile().getAbsolutePath().equals(singleFile.getAbsoluteFile().getAbsolutePath())){
                                    finalList.setTag(true);
                                }
                            }
                        }

                        pdfList.add(finalList);

                    }


                    else if (singleFile.getName().toLowerCase().endsWith(".xlsx")) {
                        finalList=new recycleListFormat(singleFile,++id);
                        if (exLoadedList !=null){
                            for (recycleListFormat f:exLoadedList){
                                if (f.file.getAbsoluteFile().getName().equals(singleFile.getAbsoluteFile().getName()) &&
                                        f.file.getAbsoluteFile().getAbsolutePath().equals(singleFile.getAbsoluteFile().getAbsolutePath())){
                                    finalList.setTag(true);
                                }
                            }
                        }


                     exelList.add(finalList);
                    }
                    else if (singleFile.getName().toLowerCase().endsWith(".docx")) {
                           finalList=new recycleListFormat(singleFile,++id);

                        if (wordLoadedList !=null){
                            for (recycleListFormat f:wordLoadedList){
                                if (f.file.getAbsoluteFile().getName().equals(singleFile.getAbsoluteFile().getName()) &&
                                        f.file.getAbsoluteFile().getAbsolutePath().equals(singleFile.getAbsoluteFile().getAbsolutePath())){
                                    finalList.setTag(true);
                                }
                            }
                        }
                         wordList.add(finalList);
                    }

                }
            }
        }



    }

    public List<recycleListFormat> getWordFiles(){
        return (wordList == null) ? new ArrayList<>() :wordList ;
    }
    
    public    List<recycleListFormat> getPdfFiles(){
        return  (pdfList == null) ? new ArrayList<>() :pdfList ;
    }
     public List<recycleListFormat> getExcelFiles(){
        return (exelList == null) ? new ArrayList<>() :exelList ;
    }


}
