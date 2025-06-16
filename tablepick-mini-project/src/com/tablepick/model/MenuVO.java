package com.tablepick.model;

public class MenuVO {
	
	private int menuId;
	private int restaurantId;
	private String name;
	private int price;
	
	public MenuVO(int restaurantId, String name, int price) {
		super();
		this.restaurantId =restaurantId;
		this.name = name;
		this.price = price;
	}
	
	public MenuVO(int menuId, int restaurantId, String name, int price) {
		super();
		this.menuId = menuId;
		this.restaurantId = restaurantId;
		this.name = name;
		this.price = price;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "MenuVO [menuId=" + menuId + ", restaurantId=" + restaurantId + ", name=" + name + ", price=" + price
				+ "]";
	}

	
	
}
