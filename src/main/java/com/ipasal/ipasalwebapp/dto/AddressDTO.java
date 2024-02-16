package com.ipasal.ipasalwebapp.dto;

import java.io.Serializable;

/**
 * 
 * @author yoomes
 *
 */
public class AddressDTO implements Serializable{
	private static final long serialVersionUID = 6709732707060656254L;
	private BillingAddress billingAddress = new BillingAddress();
	private ShippingDTO shippingAddress = new ShippingDTO();
	public AddressDTO() {
		
	}
	public BillingAddress getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	public ShippingDTO getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(ShippingDTO shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
}
