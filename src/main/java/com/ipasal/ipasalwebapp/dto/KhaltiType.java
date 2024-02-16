package com.ipasal.ipasalwebapp.dto;

/**
 * @author Azens Eklak
 * Created On 2019-03-27
 */
public class KhaltiType {
    private String idx;
    private String name;
    
    public KhaltiType() {
    	
    }
    public KhaltiType(String idx, String name) {
        this.idx = idx;
        this.name = name;
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


}
