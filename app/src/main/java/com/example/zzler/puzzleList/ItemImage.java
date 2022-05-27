package com.example.zzler.puzzleList;

public class ItemImage {

    private String imgName;
    private String imgUrl;

    public ItemImage() {
    }

    public ItemImage(String imgName, String imgUrl) {

        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
