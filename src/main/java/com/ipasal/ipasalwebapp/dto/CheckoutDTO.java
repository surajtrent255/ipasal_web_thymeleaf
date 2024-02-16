package com.ipasal.ipasalwebapp.dto;

import java.io.Serializable;

/**
 * 
 * @author yoomes
 *
 */

public class CheckoutDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private UserDTO userInfo;
	private AddressDTO billingShippingInfo;
	private PaymentDTO paymentInfo;
	public CheckoutDTO() {
		
	}
	
	public UserDTO getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserDTO userInfo) {
		this.userInfo = userInfo;
	}
	public PaymentDTO getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentDTO paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public AddressDTO getBillingShippingInfo() {
		return billingShippingInfo;
	}
	public void setBillingShippingInfo(AddressDTO billingShippingInfo) {
		this.billingShippingInfo = billingShippingInfo;
	}
	
}
