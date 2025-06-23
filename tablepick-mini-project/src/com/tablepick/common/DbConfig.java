package com.tablepick.common;

public interface DbConfig {
	//인터페이스이므로 public static final 로 자동 인식 
	String DRIVER = "com.mysql.cj.jdbc.Driver";
	String URL = "jdbc:mysql://localhost:3306/reserve?serverTimezone=Asia/Seoul";
	String USER = "mini"; // 계정명 
	String PASS = "project"; // 비밀번호
}
