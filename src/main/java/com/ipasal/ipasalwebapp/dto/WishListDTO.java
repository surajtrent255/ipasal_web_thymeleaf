package com.ipasal.ipasalwebapp.dto;

/**
 * Created by aevin on Feb 27, 2019
 **/
/*
* Main Step: 1
* */
public class WishListDTO {
    private int id;
    private int userId;
    private int productId;
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "WishListDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", delete=" + deleted +
                '}';
    }
}
