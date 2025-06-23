package com.tablepick.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.login.AccountNotFoundException;

import com.tablepick.common.DbConfig;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.session.SessionManager;
import com.tablepick.view.AdminIndex;
import com.tablepick.view.UIAdminMain;
import com.tablepick.view.UICustomerMain;
import com.tablepick.view.UIOwnerMain;

// 싱글톤 처리
public class CommonService {
	private static CommonService instance;
	// 싱글톤 패턴을 위한 TablePickSerivceCommon 객체 instance 선언

	AccountDao accountDao = null;
	// 인스턴스 변수 AccountDao 데이터형의 accountDao 선언
	private AccountVO logindata = null;
	// 인스턴스 변수 AccountVO 데이터형의 logindata 선언
	private AccountVO logindatasession = null;

	// [] 생성자 선언 : 싱글톤 패턴이므로 private로 선언, 외부에서 객체 생성 불가능 하게 막는다.
	private CommonService() throws ClassNotFoundException {
		accountDao = new AccountDao();
		// 객체 생성시 accountDao 객체에 값 들어가게 함
		Class.forName(DbConfig.DRIVER);
	}

	// getInstance() 메서드 선언
	// 외부에서 TablePickSerivceCommon.getInstance()으로 호출, 이 클래스의 객체를 요청하는 유일한 메서드이다.
	// 객체가 존재하지 않을 때만 새로운 객체를 만드므로, 한번 객체가 생성되면 항상 같은 객체가 생성되게 된다.
	public static synchronized CommonService getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new CommonService();
		}
		return instance;
	}

// 회원가입 기능
// 이미 존재하는 insertAccount로 구현
	public boolean createAccount(AccountVO accountVO) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 1. 타입 유효성 검사
		String type = accountVO.getType();
		if (!"customer".equalsIgnoreCase(type) && !"owner".equalsIgnoreCase(type)) {
			System.out.println("회원가입 실패: 허용되지 않은 계정 타입입니다. (입력된 타입: " + type + ")");
			return false;
		}
		try {

			// 2. 아이디 중복 검사
			con = accountDao.getConnection();
			String checkSql = "SELECT COUNT(*) FROM account WHERE id = ?";
			pstmt = con.prepareStatement(checkSql);
			pstmt.setString(1, accountVO.getId());
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				System.out.println("회원가입 실패: 이미 존재하는 아이디입니다. (입력된 아이디: " + accountVO.getId() + ")");
				return false;
			}

			con = accountDao.getConnection();
			String sql = "INSERT INTO account (id, type, name, password, tel) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountVO.getId());
			pstmt.setString(2, type); // 검증된 타입
			pstmt.setString(3, accountVO.getName());
			pstmt.setString(4, accountVO.getPassword());
			pstmt.setString(5, accountVO.getTel());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
		} finally {
			accountDao.closeAll(rs, pstmt, con);
		}

		return result;
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
					logindatasession = new AccountVO();
					logindatasession.setId(id);
					logindatasession.setPassword(dbPassword);
					logindatasession.setType(rs.getString("type"));
					logindatasession.setName(rs.getString("name"));
					logindatasession.setPassword(rs.getString("password"));
					logindatasession.setTel(rs.getString("tel"));
					// System.out.println("로그인 데이터 값 확인 : " + logindatasession); // 테스트용 코드
				}
			}
		} finally {
			accountDao.closeAll(rs, pstmt, con);
		}
		return logindatasession;
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

	// 로그인 사용자 정보 반환
	public AccountVO getLoginDataSessionService() {
		logindatasession = SessionManager.getLoginDataSession();
		return logindatasession;
	}

// 세션 로그인 메서드 (id, password 체크)
//로그인 성공이면 타입에 따라서 판별하여 고객 혹은 멤버 페이지로 이동
	public AccountVO loginSession(String id, String password) throws SQLException {
		AccountVO loginDataSession = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = accountDao.getConnection();
			String sql = "SELECT id, type, name, password, tel FROM account WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				System.out.println("로그인 실패: 해당 ID는 존재하지 않습니다.");
				return null;
			}

			String dbPassword = rs.getString("password");
			if (!dbPassword.equals(password)) {
				System.out.println("로그인 실패: 비밀번호가 일치하지 않습니다.");
				return null;
			}

			// 로그인 성공
			loginDataSession = new AccountVO();
			loginDataSession.setId(rs.getString("id"));
			loginDataSession.setPassword(dbPassword);
			loginDataSession.setType(rs.getString("type"));
			loginDataSession.setName(rs.getString("name"));
			loginDataSession.setTel(rs.getString("tel"));

			// 세션 저장
			SessionManager.login(loginDataSession);
			this.logindatasession = loginDataSession;

			// System.out.println(rs.getString("name")+"님 안녕하세요");
			// System.out.println("로그인 성공! 로그인 정보: " + loginDataSession); // 테스트용 코드

		} catch (SQLException e) {
			System.out.println("DB 오류로 로그인에 실패했습니다.");
			throw e;
		} finally {
			accountDao.closeAll(rs, pstmt, con);
		}

		return loginDataSession;
	}

	// 세션 로그인 데이터 가져오기
	public AccountVO getLoginDataSession() {
		AccountVO loginData = SessionManager.getLoginDataSession();
		return loginData;
	}

	// 세션 로그아웃 처리
	public boolean logoutSession() {
		if (this.logindatasession != null) {
			SessionManager.logout();
			System.out.println(this.logindatasession.getName() + "님, 로그아웃 되었습니다.");
			this.logindatasession = null;
			return true;
		} else {
			System.out.println("현재 로그인된 사용자가 없습니다.");
			return false;
		}
	}

	/**
	 * 로그인한 계정의 타입을 판별해서 그에 맞는 View로 보내는 메서드
	 * 
	 * @throws NotFoundRestaurantException
	 * @throws NotFoundAccountException
	 * @throws AccountNotFoundException
	 **/
	public void checkAccountTypeAndMovePage(AccountVO logindata)
			throws AccountNotFoundException, NotFoundAccountException, NotFoundRestaurantException {
		String accountType = logindata.getType();
		switch (accountType) {
		case "customer":
			System.out.println("고객 메인 페이지 출력");
			UICustomerMain.getInstance().run();
			break;
		case "owner":
			System.out.println("음식점 주인 메인 페이지 출력");
			UIOwnerMain.getInstance().run();
			break;
		case "admin":
			System.out.println("관리자 메인 페이지 출력");
			UIAdminMain.getInstance().run();
			break;
		}
	}

}
