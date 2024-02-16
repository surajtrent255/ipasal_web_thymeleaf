package com.ipasal.ipasalwebapp.dto;

import java.io.Serializable;

/**
 * @author Azens Eklak
 * Created On 2019-03-22
 */

public class PaymentMethodDTO implements Serializable{
	private static final long serialVersionUID = -9209498312077984696L;
	private int id;
    private String paymentName;
    private String paymentStatus;
    private String publicApiKey;
    private String privateApiKey;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    
    public String getPublicApiKey() {
		return publicApiKey;
	}

	public void setPublicApiKey(String publicApiKey) {
		this.publicApiKey = publicApiKey;
	}

	public String getPrivateApiKey() {
		return privateApiKey;
	}

	public void setPrivateApiKey(String privateApiKey) {
		this.privateApiKey = privateApiKey;
	}

	@Override
    public String toString() {
        return "PaymentMethodDTO{" +
                "id=" + id +
                ", paymentName='" + paymentName + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
