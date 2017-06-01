package com.example.abela.marketspiral.Utility;

import android.graphics.Bitmap;

/**
 * Created by HaZe on 5/31/17.
 */

public class ImageInfo {
    private String name;
    private String path;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

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
