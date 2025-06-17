package com.tablepick.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReserveVO {
	private int reserveId;
	private String accountId;
	private int restaurantId;
	private int reservePeople;
	private int reservetime;
	private LocalDateTime reserveDate;
	private LocalDateTime registerDate;
	
	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	
	
	public ReserveVO(String accountId, int restaurantId, int reservePeople, int reservetime, LocalDateTime reserveDate) {
		super();
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reservePeople = reservePeople;
		this.reservetime = reservetime;
		this.reserveDate = reserveDate;
	}

	public ReserveVO(int reserveId, String accountId, int restaurantId, int reservePeople, int reservetime,
			LocalDateTime reserveDate) {
		super();
		this.reserveId = reserveId;
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reservePeople = reservePeople;
		this.reservetime = reservetime;
		this.reserveDate = reserveDate;
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

	public int getReservePeople() {
		return reservePeople;
	}

	public void setReservePeople(int reservePeople) {
		this.reservePeople = reservePeople;
	}

	public int getReservetime() {
		return reservetime;
	}

	public void setReservetime(int reservetime) {
		this.reservetime = reservetime;
	}

	public LocalDateTime getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(LocalDateTime reserveDate) {
		this.reserveDate = reserveDate;
	}

	@Override
	public String toString() {
		return "ReserveVO [reserveId=" + reserveId + ", accountId=" + accountId + ", restaurantId=" + restaurantId
				+ ", reservePeople=" + reservePeople + ", reservetime=" + reservetime + ", reserveDate=" + reserveDate
				+ ", registerDate=" + registerDate + "]";
	}

}
