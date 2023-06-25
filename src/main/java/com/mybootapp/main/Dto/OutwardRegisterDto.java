package com.mybootapp.main.Dto;

// Data Transfer Object for OutwardRegister.
// The class variables are decided based on the negotiations between Frontend and Backend people.
public class OutwardRegisterDto {
	private String productTitle;
	private int productQuantity;
	private String godownLocation;
	private String godownManager;
	private String recieptNo;
	
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public int getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	public String getGodownLocation() {
		return godownLocation;
	}
	public void setGodownLocation(String godownLocation) {
		this.godownLocation = godownLocation;
	}
	public String getGodownManager() {
		return godownManager;
	}
	public void setGodownManager(String godownManager) {
		this.godownManager = godownManager;
	}
	public String getRecieptNo() {
		return recieptNo;
	}
	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
	}
	
}
