package com.videos.phovio.model;

import java.io.File;

/**
 * Created by hsn on 06/11/2018.
 */

public class StatusWhatsapp {
    private File file;
    private int viewType;
    public Boolean playinig;
    public StatusWhatsapp setFile(File file) {
        this.file = file;
        return this;
    }

    public File getFile() {
        return file;
    }

    public StatusWhatsapp setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }

    public int getViewType() {
        return viewType;
    }
}
