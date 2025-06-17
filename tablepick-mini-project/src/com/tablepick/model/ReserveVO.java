package com.tablepick.model;

import java.time.LocalDateTime;

public class ReserveVO {
	private int reserveId;
	private String accountId;
	private int restaurantId;
	private int reserveCount;
	private LocalDateTime reserveDate;
	private LocalDateTime registerDate;
	private Long sale;
	
	public ReserveVO(String accountId, int restaurantId, int reserveCount, LocalDateTime reserveDate, Long sale) {
		super();
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reserveCount = reserveCount;
		this.reserveDate = reserveDate;
		this.sale = sale;
	}
	public ReserveVO(int restaurantId, int reserveCount, LocalDateTime reserveDate, LocalDateTime registerDate,
			Long sale) {
		super();
		this.restaurantId = restaurantId;
		this.reserveCount = reserveCount;
		this.reserveDate = reserveDate;
		this.registerDate = registerDate;
		this.sale = sale;
	}

	public ReserveVO(int reserveId, String accountId, int restaurantId, int reserveCount, LocalDateTime reserveDate,
			Long sale) {
		super();
		this.reserveId = reserveId;
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reserveCount = reserveCount;
		this.reserveDate = reserveDate;
		this.sale = sale;
	}
	
	public ReserveVO(int reserveId, String accountId, int restaurantId, int reserveCount, LocalDateTime reserveDate,
			LocalDateTime registerDate, Long sale) {
		super();
		this.reserveId = reserveId;
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reserveCount = reserveCount;
		this.reserveDate = reserveDate;
		this.registerDate = registerDate;
		this.sale = sale;
	}

	public int getReserveId() {
		return reserveId;
	}

	public void setReserveId(int reserveId) {
		this.reserveId = reserveId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getReserveCount() {
		return reserveCount;
	}

	public void setReserveCount(int reserveCount) {
		this.reserveCount = reserveCount;
	}

	public LocalDateTime getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(LocalDateTime reserveDate) {
		this.reserveDate = reserveDate;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	public Long getSale() {
		return sale;
	}

	public void setSale(Long sale) {
		this.sale = sale;
	}
	

	@Override
	public String toString() {
		return "ReserveVO [reserveId=" + reserveId + ", accountId=" + accountId + ", restaurantId=" + restaurantId
				+ ", reserveCount=" + reserveCount + ", reserveDate=" + reserveDate + ", sale=" + sale + "]";
	}

}
