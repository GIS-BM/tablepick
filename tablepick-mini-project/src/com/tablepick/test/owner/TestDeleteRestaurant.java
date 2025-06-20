package com.tablepick.test.owner;

import java.sql.SQLException;
import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.service.OwnerService;
import com.tablepick.session.SessionManager;

public class TestDeleteRestaurant {
	/* 식당 삭제를 테스트하는 클래스 입니다.
	 * 
	 */
	private static TestDeleteRestaurant instance = new TestDeleteRestaurant();

	private TestDeleteRestaurant() {
	}

	public static TestDeleteRestaurant getInstance() {
		return instance;
	}

	public void run() throws AccountNotFoundException {
		OwnerService service = new OwnerService();
		Scanner sc = new Scanner(System.in);

		// 세션으로 id 가져오기
		String accountId = SessionManager.getLoginDataSession().getId();

		String writeAccountId;
		boolean restaurantDelete = false;
		String password;
		boolean idCorrect = false;

		while (!restaurantDelete) {

			try {
				if (service.existRestaurant(accountId) == false) {
					System.out.println("                          ");
					System.out.println("등록된 식당이 없기 때문에 삭제할 수 없습니다.");
					restaurantDelete = true;
				} else {
					System.out.println("                  식당을 삭제하려면 본인의 아이디와 비밀번호를 입력해주세요.");
					try {

						while (!idCorrect) {
							System.out.println("아이디 : ");
							writeAccountId = sc.nextLine();
							if (!writeAccountId.equals(accountId)) {
								System.out.println("로그인 한 아이디와 다릅니다. 다시 입력하세요.");
							} else {
								idCorrect = true;
							}
						}
						System.out.println("비밀번호 : ");
						password = sc.nextLine();
						service.deleteMyRestaurant(accountId, password);

						System.out.println("등록된 식당을 삭제했습니다.");
						restaurantDelete = true;
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					} catch (NotFoundAccountException e) {
						System.out.println(e.getMessage());
					} catch (NotMatchedPasswordException e) {
						System.out.println(e.getMessage());
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public static void main(String[] args) {
		try {
			new TestDeleteRestaurant().run();
		} catch (AccountNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}
