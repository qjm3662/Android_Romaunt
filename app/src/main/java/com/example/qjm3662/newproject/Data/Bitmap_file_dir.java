package com.example.qjm3662.newproject.Data;

import android.graphics.Bitmap;

/**
 * Created by qjm3662 on 2016/8/22 0022.
 */
public class Bitmap_file_dir {
    private Bitmap bitmap;
    private String targetDir;

    public Bitmap_file_dir(Bitmap bitmap, String targetDir) {
        this.bitmap = bitmap;
        this.targetDir = targetDir;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
