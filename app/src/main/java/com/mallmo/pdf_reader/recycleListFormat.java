package com.mallmo.pdf_reader;

import java.io.File;
// tamame faile ha be in shekl be recycle view pass dade mishavad
public class recycleListFormat {
    public File file;
    public boolean tag =false;
    public long id;
    public recycleListFormat(File file,long id) {
        this.file = file;
        this.id=id;
    }

    public File getFile() {
        return file;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
