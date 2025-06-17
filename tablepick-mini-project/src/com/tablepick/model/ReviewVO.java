package com.tablepick.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ReviewVO {
	private int idx;
	private int reserve_idx;
	private int star;
	private String comment;
	private Timestamp registerdate;
	// sql datetime 데이터형은 자바에서 Timestamp로 사용한다.
	// sql date 데이터형은 자바에서 Date로 사용한다.
	
	// 기본 생성자
	public ReviewVO() {
		super();
		
	}
	// 매개변수 추가된 생성자
	public ReviewVO(int idx, int reserve_idx, int star, String comment, Timestamp registerdate) {
		super();
		this.idx = idx;
		this.reserve_idx = reserve_idx;
		this.star = star;
		this.comment = comment;
		this.registerdate = registerdate;
	}
	
	//getter, setter 메서드
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getReserve_idx() {
		return reserve_idx;
	}
	public void setReserve_idx(int reserve_idx) {
		this.reserve_idx = reserve_idx;
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
	public Timestamp getRegisterdate() {
		return registerdate;
	}
	public void setRegisterdate(Timestamp registerdate) {
		this.registerdate = registerdate;
	}
	
	// toString() 메서드 선언
	@Override
	public String toString() {
		return "ReviewVO [idx=" + idx + ", reserve_idx=" + reserve_idx + ", star=" + star + ", comment=" + comment
				+ ", registerdate=" + registerdate + "]";
	}
	
	
	
	
	
}