package com.example.lijiang.zhihu.ObjectClass;

/**
 * Created by lijiang on 2017/4/24.
 */

public class Item {
    private String title;
    private int imageID;

    public Item(String title,int imageID){
        this.title = title;
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public int getImageID() {
        return imageID;
    }

}
