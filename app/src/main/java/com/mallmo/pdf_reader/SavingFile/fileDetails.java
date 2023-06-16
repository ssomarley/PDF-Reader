package com.mallmo.pdf_reader.SavingFile;

import java.util.ArrayList;
import java.util.List;

public class fileDetails {

    public String fileName;
    public String filePath;

   public long id;

   public boolean tag=false;

    public boolean getTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public fileDetails(String fileName, String filePath, long id) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.id = id;
    }

}
