package com.example.abela.marketspiral.Utility;

import android.graphics.Bitmap;

/**
 * Created by HaZe on 5/31/17.
 */

public class ImageInfo {
    private String name;
    private String path;

    public ImageInfo(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public ImageInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
