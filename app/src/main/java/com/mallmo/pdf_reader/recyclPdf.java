package com.mallmo.pdf_reader;

import java.io.File;

public class recyclPdf {
    public File file;
    public boolean tag =false;

    public recyclPdf(File file) {
        this.file = file;
    }

    public boolean getTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
