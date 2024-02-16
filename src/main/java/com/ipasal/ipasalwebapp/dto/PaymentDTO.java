package com.ipasal.ipasalwebapp.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
/**
 * @author yoomes
 */

public class PaymentDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int paymentId;
	@Min(value = 1, message = "Payment Method Id Must Be 1 or Greater Than 1.")
	private int paymentMethodId;
	
	@PositiveOrZero(message = "Amount must be 0 or greater than 0.")
	private Double amount;
	private Date paymentDate;
	private boolean status;
	
	private String token;
	private PaymentMethodDTO paymentMethod;
	private String uniqueOrderIdentifier;
	
	public PaymentDTO() {
		
	}
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public int getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public PaymentMethodDTO getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUniqueOrderIdentifier() {
		return uniqueOrderIdentifier;
	}
	public void setUniqueOrderIdentifier(String uniqueOrderIdentifier) {
		this.uniqueOrderIdentifier = uniqueOrderIdentifier;
	}
}
