package com.tablepick.model;

import java.time.LocalTime;

public class RestaurantVO {
	
	private int restaurantId;
	private String accountId;
	private String name;
	private String type;
	private String address;
	private String tel;
	private LocalTime openTime;
	
	
	
	public RestaurantVO(int restaurantId, String accountId, String name, String type, String address, String tel,
			LocalTime openTime) {
		super();
		this.restaurantId = restaurantId;
		this.accountId = accountId;
		this.name = name;
		this.type = type;
		this.address = address;
		this.tel = tel;
		this.openTime = openTime;
	}

	public RestaurantVO(String accountId, String name, String type, String address, String tel, LocalTime openTime) {
		super();
		this.accountId = accountId;
		this.name = name;
		this.type = type;
		this.address = address;
		this.tel = tel;
		this.openTime = openTime; 
	}
	
	public RestaurantVO(int restaurantId, String accountId, String name, String type, String address, String tel) {
		super();
		this.restaurantId = restaurantId;
		this.accountId = accountId;
		this.name = name;
		this.type = type;
		this.address = address;
		this.tel = tel;
	}
	

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	@Override
	public String toString() {
		return "RestaurantVO [restaurantId=" + restaurantId + ", accountId=" + accountId + ", name=" + name + ", type="
				+ type + ", address=" + address + ", tel=" + tel + ", openTime=" + openTime + "]";
	}
}
