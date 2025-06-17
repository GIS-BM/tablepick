package com.tablepick.test.accountLogout;

import java.sql.SQLException;

import com.tablepick.exception.LogoutFailException;
import com.tablepick.model.AccountVO;
import com.tablepick.service.TablePickSerivceCommon;

public class AccountLogoutUnitTest {
// 로그인 데이터 다른 클래스에서 유지되는지 테스트
// 테스트 성공
	
	public static void main(String[] args) {
		AccountVO loginData = null;
		try {
			if(TablePickSerivceCommon.getInstance().login("owner01", "pw1234")!=null) {
				System.out.println("로그인 성공");
			}
			loginData = TablePickSerivceCommon.getInstance().getLoginData();
			System.out.println("로그인 데이터 값 출력 : " + loginData);
			TablePickSerivceCommon.getInstance().logout(loginData);
			if(loginData!=null)
				throw new LogoutFailException("로그아웃이 실패하였습니다. 다시 시도해주세요");
			System.out.println("로그아웃이 성공");
		} catch (ClassNotFoundException | SQLException | LogoutFailException e) {
			e.printStackTrace();
		}
	}
}