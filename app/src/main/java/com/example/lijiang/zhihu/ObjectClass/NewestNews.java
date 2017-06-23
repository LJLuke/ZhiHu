package com.example.lijiang.zhihu.ObjectClass;

/**
 * Created by lijiang on 2017/5/4.
 */

public class NewestNews {
    private String date;
    private String imageUrl;
    private int id;
    private String title;
    private String topImageUrl;
    private int topId;
    private String topTitle;
    private boolean isMoreImages;

    public boolean isMoreImages() {
        return isMoreImages;
    }

    public void setMoreImages(boolean moreImages) {
        isMoreImages = moreImages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopImageUrl() {
        return topImageUrl;
    }

    public void setTopImageUrl(String topImageUrl) {
        this.topImageUrl = topImageUrl;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public String getTopTitel() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }
}
