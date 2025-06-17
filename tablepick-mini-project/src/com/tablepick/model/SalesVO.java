package com.tablepick.model;

public class SalesVO {
	private int  idx;
	private int reserveId;
	private int sales;
	
	public SalesVO() {
		super();
	}

	public SalesVO(int idx, int reserveId, int sales) {
		super();
		this.idx = idx;
		this.reserveId = reserveId;
		this.sales = sales;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getReserveId() {
		return reserveId;
	}

	public void setReserveId(int reserveId) {
		this.reserveId = reserveId;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	@Override
	public String toString() {
		return "SalesVO [idx=" + idx + ", reserveId=" + reserveId + ", sales=" + sales + "]";
	}
	

}
