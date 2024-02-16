package com.ipasal.ipasalwebapp.dto;

/**
 * @author Azens Eklak
 * Created On 2019-03-29
 */
public class StripeChargeDTO {

    public enum Currency {
        EUR, USD;
    }
    private String description;
    private Integer amount; // cents
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getStripeEmail() {
        return stripeEmail;
    }

    public void setStripeEmail(String stripeEmail) {
        this.stripeEmail = stripeEmail;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }
}
