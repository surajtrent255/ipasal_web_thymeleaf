package com.ipasal.ipasalwebapp.dto;

/**
 * @author Azens Eklak
 * Created On 2019-03-27
 */
public class KhaltiMerchant {
    private String idx;
    private String name;
    private String mobile;
    public KhaltiMerchant() {
    	
    }
    public KhaltiMerchant(String idx, String name, String mobile) {
        this.idx = idx;
        this.name = name;
        this.mobile = mobile;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "KhaltiMerchant{" +
                "idx='" + idx + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
