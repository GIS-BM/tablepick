package com.tablepick.model;

import java.time.LocalDateTime;

public class ReviewVO {
	private int idx;
	private int reserveIdx;
	private int star;
	private String comment;
	private LocalDateTime registerDate;
	// sql datetime 데이터형은 자바에서 Timestamp로 사용한다.
	// sql date 데이터형은 자바에서 Date로 사용한다.
	
	// 기본 생성자
	public ReviewVO() {
		super();
		
	}
	// 매개변수 추가된 생성자
	public ReviewVO(int idx, int reserveIdx, int star, String comment, LocalDateTime registerDate) {
		super();
		this.idx = idx;
		this.reserveIdx = reserveIdx;
		this.star = star;
		this.comment = comment;
		this.registerDate = registerDate;
	}	
	//getter, setter 메서드
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getReserveIdx() {
		return reserveIdx;
	}
	public void setReserveIdx(int reserveIdx) {
		this.reserveIdx = reserveIdx;
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

	// toString() 메서드 선언
	@Override
	public String toString() {
		return "ReviewVO [idx=" + idx + ", reserveIdx=" + reserveIdx + ", star=" + star + ", comment=" + comment
				+ ", registerDate=" + registerDate + "]";
	}
	
	
}
