package com.tablepick.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tablepick.common.DbConfig;
import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.session.SessionManager;

// 싱글톤 처리
public class TablePickSerivceCommon {
	private static TablePickSerivceCommon instance;
	// 싱글톤 패턴을 위한 TablePickSerivceCommon 객체 instance 선언

	AccountDao accountDao = null;
	// 인스턴스 변수 AccountDao 데이터형의 accountDao 선언
	private AccountVO logindata = null;
	// 인스턴스 변수 AccountVO 데이터형의 logindata 선언

	// [] 생성자 선언 : 싱글톤 패턴이므로 private로 선언, 외부에서 객체 생성 불가능 하게 막는다.
	private TablePickSerivceCommon() throws ClassNotFoundException {
		accountDao = new AccountDao();
		// 객체 생성시 accountDao 객체에 값 들어가게 함
		Class.forName(DbConfig.DRIVER);
	}

	// getInstance() 메서드 선언
	// 외부에서 TablePickSerivceCommon.getInstance()으로 호출, 이 클래스의 객체를 요청하는 유일한 메서드이다.
	// 객체가 존재하지 않을 때만 새로운 객체를 만드므로, 한번 객체가 생성되면 항상 같은 객체가 생성되게 된다.
	public static synchronized TablePickSerivceCommon getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new TablePickSerivceCommon();
		}
		return instance;
	}

	// 로그인 사용자 정보 반환
	public AccountVO getLoginData() {
		return logindata;
	}

// 회원가입 기능
// 이미 존재하는 insertAccount로 구현
	public boolean registerAccount(AccountVO accountVO) throws SQLException {
		return accountDao.insertAccount(accountVO);
	}

// 로그인 메서드 (id, password 체크)
// 로그인 성공이면 타입에 따라서 판별하여 고객 혹은 멤버 페이지로 이동
	public AccountVO login(String id, String password) throws SQLException {
		boolean isLogin = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = accountDao.getConnection();
			String sql = "SELECT id, type, name, password, tel FROM account WHERE id = ?";
			// sql 구문
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			// pstmt 객체에 ? 부분에 값 설정
			rs = pstmt.executeQuery();
			// pstmt로 쿼리문 실행하고 결과값 rs에 저장
			if (rs.next()) {
				String dbPassword = rs.getString("password");
				if (dbPassword.equals(password)) {
					isLogin = true;

					// [] 로그인 정보를 담고 있는 AccountVO 데이터형 객체 logindata 생성하고 값 저장
					logindata = new AccountVO();
					logindata.setId(id);
					logindata.setPassword(dbPassword);
					logindata.setType(rs.getString("type"));
					logindata.setName(rs.getString("name"));
					logindata.setPassword(rs.getString("password"));
					logindata.setTel(rs.getString("tel"));
					System.out.println("로그인 데이터 값 확인 : " + logindata);
				}
			}
		} finally {
			accountDao.closeAll(rs, pstmt, con);
		}
		return logindata;
	}
	// 로그아웃 처리
	public boolean logout() {
		if (this.logindata != null) {
			System.out.println(this.logindata.getName() + "님, 로그아웃 되었습니다.");
			this.logindata = null;
			return true;
		} else {
			System.out.println("현재 로그인된 사용자가 없습니다.");
			return false;
		}
	}
	
//로그인 메서드 (id, password 체크)
//로그인 성공이면 타입에 따라서 판별하여 고객 혹은 멤버 페이지로 이동
	public AccountVO loginSessionManager(String id, String password) throws SQLException {
		boolean isLogin = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = accountDao.getConnection();
			String sql = "SELECT id, type, name, password, tel FROM account WHERE id = ?";
			// sql 구문
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			// pstmt 객체에 ? 부분에 값 설정
			rs = pstmt.executeQuery();
			// pstmt로 쿼리문 실행하고 결과값 rs에 저장
			if (rs.next()) {
				String dbPassword = rs.getString("password");
				if (dbPassword.equals(password)) {
					isLogin = true;

					// [] 로그인 정보를 담고 있는 AccountVO 데이터형 객체 logindata 생성하고 값 저장
					logindata = new AccountVO();
					logindata.setId(id);
					logindata.setPassword(dbPassword);
					logindata.setType(rs.getString("type"));
					logindata.setName(rs.getString("name"));
					logindata.setPassword(rs.getString("password"));
					logindata.setTel(rs.getString("tel"));
					SessionManager.login(logindata);
				}
			}
		} finally {
			accountDao.closeAll(rs, pstmt, con);
		}
		return logindata;
	}

	// 로그아웃 처리
	public boolean logoutSession() {
		logout();
		if (this.logindata != null) {
			System.out.println(this.logindata.getName() + "님, 로그아웃 되었습니다.");
			this.logindata = null;
			return true;
		} else {
			System.out.println("현재 로그인된 사용자가 없습니다.");
			return false;
		}
	}
	
	/** 로그인한 계정의 타입을 판별해서 그에 맞는 View로 보내는 메서드
	**/
	public void checkAccountTypeAndMovePage(AccountVO logindata) {
		String accountType = logindata.getType();
		switch (accountType) {
			case "customer" :
				System.out.println("고객 메인 페이지 출력");
				// customerView().run();
				break;
			case "owner" :
				System.out.println("음식점 주인 메인 페이지 출력");
				// ownerView().run();
				break;
			case "admin" :
				System.out.println("관리자 메인 페이지 출력");
				// adminView().run();
				break;
		}
	}

}
