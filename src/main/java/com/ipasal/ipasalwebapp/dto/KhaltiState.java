package com.ipasal.ipasalwebapp.dto;

/**
 * @author Azens Eklak
 * Created On 2019-03-27
 */
public class KhaltiState {
    private String idx;
    private String name;
    private String template;
    public KhaltiState() {
    	
    }
    public KhaltiState(String idx, String name, String template) {
        this.idx = idx;
        this.name = name;
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
