package com.tablepick.test.common;

import java.sql.SQLException;

import com.tablepick.model.AccountVO;
import com.tablepick.service.CommonService;
import com.tablepick.session.SessionManager;


public class AccountLogoutSessionUnitTest {
// 세션 로그인, 계정 데이터 가져오기, 세션 로그아웃 테스트

	public static void main(String[] args) {
		AccountVO loginData = null;
		try {
			if (CommonService.getInstance().loginSession("owner02", "pw1234") != null) {
			// 세션 로그인 하기
				System.out.println("로그인 성공");
			}
			loginData = CommonService.getInstance().getLoginDataSession();
			// 로그인한 계정 데이터 가져오기
			System.out.println("로그인 데이터 값 출력 : " + loginData);
			// CommonService.getInstance().checkAccountTypeAndMovePage(loginData);
			// 계정 타입 확인하고 타입별로 페이지 이동하는 메서드
			
			CommonService.getInstance().logoutSession();
			// 세션 로그아웃 하기
			
			// [] 재 로그인 기능 테스트
			if (CommonService.getInstance().loginSession("owner04", "pw1234") != null) {
			// 세션 로그인 하기
				System.out.println("로그인 성공");
			}
			
			loginData = CommonService.getInstance().getLoginDataSession();
			// 로그인한 계정 데이터 가져오기
			System.out.println("로그인 데이터 값 출력 : " + loginData);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}