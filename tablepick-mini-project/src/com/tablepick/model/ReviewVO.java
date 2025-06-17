package com.tablepick.model;

import java.time.LocalDateTime;

public class ReviewVO {
	private int reviewId;
	private String account_id;
	private int restaurantId;
	private int star;
	private String comment;
	private LocalDateTime registerDate;

	public ReviewVO(int reviewId, String account_id, int restaurantId, int star, String comment,
			LocalDateTime registerDate) {
		super();
		this.reviewId = reviewId;
		this.account_id = account_id;
		this.restaurantId = restaurantId;
		this.star = star;
		this.comment = comment;
		this.registerDate = registerDate;
	}
	

	public ReviewVO(String account_id, int restaurantId, int star, String comment, LocalDateTime registerDate) {
		super();
		this.account_id = account_id;
		this.restaurantId = restaurantId;
		this.star = star;
		this.comment = comment;
		this.registerDate = registerDate;
	}


	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getAccount_id() {
		return account_id;
	}


	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}


	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return "ReviewVO [reviewId=" + reviewId + ", account_id=" + account_id + ", restaurantId=" + restaurantId
				+ ", star=" + star + ", comment=" + comment + ", registerDate=" + registerDate + "]";
	}

}
