package com.ipasal.ipasalwebapp.dto;

public class ImageDTO {
    private int imageId;
    private String image;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString(){return super.toString();}


}
