package com.tablepick.test.GetLoginDataSession;

import java.sql.SQLException;

import com.tablepick.model.AccountVO;
import com.tablepick.service.TablePickSerivceCommon;

public class AccountLogoutSessionUnitTest {
// 로그인 데이터 다른 클래스에서 유지되는지 테스트
// 테스트 성공

	public static void main(String[] args) {
		AccountVO loginData = null;
		try {
			if (TablePickSerivceCommon.getInstance().loginSessionManager("owner02", "pw1234") != null) {
				System.out.println("로그인 성공");
			}
			loginData = TablePickSerivceCommon.getInstance().getLoginData();
			System.out.println("로그인 데이터 값 출력 : " + loginData);

			TablePickSerivceCommon.getInstance().logoutSession();
			System.out.println();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}