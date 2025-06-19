package com.tablepick.test.common;

import java.sql.SQLException;

import com.tablepick.model.AccountVO;
import com.tablepick.service.CommonService;

public class GetLoginDataUnit {
// 로그인 데이터 다른 클래스에서 유지되는지 테스트
// 테스트 성공
	
	public static void main(String[] args) {
		AccountVO loginData = null;
		try {
			if(CommonService.getInstance().login("owner04", "pw1234")!=null) {
				System.out.println("로그인 성공");
			}
			loginData = CommonService.getInstance().getLoginData();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println("로그인 데이터 값 출력 : " + loginData);
	}
}
