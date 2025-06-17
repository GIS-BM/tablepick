package com.tablepick.model;

public class TotalSalesVO {
	private int  idx;
	private int restaurantId;
	private int sales;
	
	public TotalSalesVO() {
		super();
	}

	public TotalSalesVO(int idx, int restaurantId, int sales) {
		super();
		this.idx = idx;
		this.restaurantId = restaurantId;
		this.sales = sales;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	@Override
	public String toString() {
		return "TotalSales [idx=" + idx + ", restaurantId=" + restaurantId + ", sales=" + sales + "]";
	}
	

}
