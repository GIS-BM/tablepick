package com.tablepick.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReserveVO {
	private int reserveId;
	private String accountId;
	private int restaurantId;
	private int reservePeople;
	private LocalDate reserveDate;
	private int reserveTime;
	private LocalDateTime registerDate;

	public ReserveVO(int reserveId, String accountId, int restaurantId, int reservePeople, LocalDate reserveDate,
			int reserveTime, LocalDateTime registerDate) {
		super();
		this.reserveId = reserveId;
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reservePeople = reservePeople;
		this.reserveDate = reserveDate;
		this.reserveTime = reserveTime;
		this.registerDate = registerDate;
	}

	public ReserveVO(int reserveId, String accountId, int restaurantId, int reservePeople, LocalDate reserveDate,
			int reserveTime) {
		super();
		this.reserveId = reserveId;
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reservePeople = reservePeople;
		this.reserveDate = reserveDate;
		this.reserveTime = reserveTime;
	}

	public ReserveVO(String accountId, int restaurantId, int reservePeople, LocalDate reserveDate,
			int reserveTime) {
		super();
		this.accountId = accountId;
		this.restaurantId = restaurantId;
		this.reservePeople = reservePeople;
		this.reserveDate = reserveDate;
		this.reserveTime = reserveTime;
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

	public LocalDate getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(LocalDate reserveDate) {
		this.reserveDate = reserveDate;
	}

	public int getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(int reserveTime) {
		this.reserveTime = reserveTime;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return "ReserveVO [reserveId=" + reserveId + ", accountId=" + accountId + ", restaurantId=" + restaurantId
				+ ", reservePeople=" + reservePeople + ", reserveDate=" + reserveDate + ", reserveTime=" + reserveTime
				+ ", registerDate=" + registerDate + "]";
	}

}
