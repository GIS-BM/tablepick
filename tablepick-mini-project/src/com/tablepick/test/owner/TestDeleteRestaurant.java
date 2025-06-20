package com.tablepick.test.owner;

import java.sql.SQLException;

import java.util.Scanner;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;

import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;


public class TestDeleteRestaurant {
	// 식당 삭제를 테스트하는 클래스 입니다.
	private static TestDeleteRestaurant instance = new TestDeleteRestaurant();
	private TestDeleteRestaurant() {
	}
	public static TestDeleteRestaurant getInstance() {
		return instance;
	}

	public void run() {
		OwnerService service = new OwnerService();
		Scanner sc = new Scanner(System.in);
		AccountVO loginData = null;

//		try {
//			loginData = TablePickSerivceCommon.getInstance().getLoginData();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String accountId = loginData.getId();
		//세션으로 id 가져오기
		String accountId = SessionManager.getLoginDataSession().getId();
				

		String writeAccountId;
		boolean restaurantDelete = false;
		String password;
		boolean idCorrect = false;
	
		while (!restaurantDelete) {
			System.out.println("                  식당을 삭제하려면 본인의 아이디와 비밀번호를 입력해주세요.");
			try {
				
				while(!idCorrect) {
				System.out.println("아이디 : ");
				writeAccountId = sc.nextLine();
				if (!writeAccountId.equals(accountId)) {
					System.out.println("로그인 한 아이디와 다릅니다. 다시 입력하세요.");
				}else {
					idCorrect = true;
				}
				}
				System.out.println("비밀번호 : ");
				password = sc.nextLine();
				service.deleteMyRestaurant(accountId, password);

				System.out.println("등록된 식당을 삭제했습니다.");
				restaurantDelete=true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (AccountNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (NotMatchedPasswordException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		new TestDeleteRestaurant().run();
	}

}
