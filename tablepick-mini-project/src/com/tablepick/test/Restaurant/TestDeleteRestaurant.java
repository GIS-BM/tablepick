package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.model.RestaurantDao;

public class TestDeleteRestaurant {
	// 식당 삭제를 테스트하는 클래스 입니다.

	public static void main(String[] args) {
		RestaurantDao dao = new RestaurantDao();
		Scanner sc = new Scanner(System.in);

		boolean restaurantDelete = true;
		String accountId;
		String password;
		System.out.println("식당을 삭제하려면 본인의 아이디와 비밀번호를 입력해주세요.");
		while (restaurantDelete) {
			try {
				System.out.println("아이디 : ");
				accountId = sc.nextLine();
				System.out.println("비밀번호 : ");
				password = sc.nextLine();
				dao.deleteMyRes(accountId, password);

				System.out.println("삭제 완료");
				restaurantDelete = false;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (NotMatchedPasswordException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}

		}

		sc.close();
	}

}
