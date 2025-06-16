package com.tablepick.model;

public class AccountVO {
	private String id;
	private String type;
	private String name;
	private String password;
	private String tel;
	
	// 기본 생성자
	public AccountVO() {
		super();
	}
	// 매개변수 추가한 생성자
	public AccountVO(String id, String type, String name, String password, String tel) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.password = password;
		this.tel = tel;
	}
	// getter, setter 매서드
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	// toString 메서드
	@Override
	public String toString() {
		return "AccountVO [id=" + id + ", type=" + type + ", name=" + name + ", password=" + password + ", tel=" + tel
				+ "]";
	}
	
}
