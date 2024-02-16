package com.ipasal.ipasalwebapp.dto;

/**
 * @author Azens Eklak
 * Created On 2019-03-27
 */
public class KhaltiPayloadDTO{
  	String token;
    int amount;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
