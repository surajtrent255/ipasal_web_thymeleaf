/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Aug 30, 2019
 */
package com.ipasal.ipasalwebapp.dto;

import java.io.Serializable;

/**
 * Sales Report class which holds the most bought product,
 * total no of products sold
 * total confimed orders and total cancelled orders information
 */
public class SalesReport implements Serializable{
	private static final long serialVersionUID = 7767367956029851670L;
	private int totalProductsSold;
	private ProductDTO mostSoldProduct;
	private int totalConfirmedOrders;
	private int totalCancelledOrders;
	
	public SalesReport() {
	}
	public int getTotalProductsSold() {
		return totalProductsSold;
	}
	public void setTotalProductsSold(int totalProductsSold) {
		this.totalProductsSold = totalProductsSold;
	}
	public ProductDTO getMostSoldProduct() {
		return mostSoldProduct;
	}
	public void setMostSoldProduct(ProductDTO mostSoldProduct) {
		this.mostSoldProduct = mostSoldProduct;
	}
	public int getTotalConfirmedOrders() {
		return totalConfirmedOrders;
	}
	public void setTotalConfirmedOrders(int totalConfirmedOrders) {
		this.totalConfirmedOrders = totalConfirmedOrders;
	}
	public int getTotalCancelledOrders() {
		return totalCancelledOrders;
	}
	public void setTotalCancelledOrders(int totalCancelledOrders) {
		this.totalCancelledOrders = totalCancelledOrders;
	}
	
}
