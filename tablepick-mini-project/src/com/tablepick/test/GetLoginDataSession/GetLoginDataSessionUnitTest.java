package com.tablepick.test.GetLoginDataSession;

import java.sql.SQLException;

import com.tablepick.model.AccountVO;
import com.tablepick.service.CommonService;
import com.tablepick.session.SessionManager;

public class GetLoginDataSessionUnitTest {
// 로그인 데이터 다른 클래스에서 유지되는지 테스트
// 테스트 성공
	
	public static void main(String[] args) {
		AccountVO loginData = null;
		try {
			if(CommonService.getInstance().loginSessionManager("owner02", "pw1234")!=null) {
				System.out.println("로그인 성공");
			}
			System.out.println("로그인된 데이터 가져오기 : "+SessionManager.getLoginDataSession());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
