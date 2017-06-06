package com.example.abela.marketspiral.Utility;

/**
 * Created by HaZe on 5/31/17.
 */

public class ImageInfo {
    private String name;
    private String path;
    private String encoded;

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

    public void setEncoded(String encoded){
        this.encoded = encoded;
    }

    public String getEncoded(){
        return encoded;
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
