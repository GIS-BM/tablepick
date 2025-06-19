package com.tablepick.test.CustomerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.tablepick.test.SearchRestaurant.SearchRestaurantUnitTest;

public class UICustomerSearch {
	private static UICustomerSearch instance = new UICustomerSearch();

	private UICustomerSearch() {
	}

	public static UICustomerSearch getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			new UICustomerSearch().run(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(BufferedReader reader) {
		try {
			while (true) {
				printSearchRestaurantMenu();
				String main = reader.readLine().trim();
				switch (main) {
				case "1":
					SearchRestaurantUnitTest.getInstance().searchAllRestaurant();
					break;
				case "2":
					SearchRestaurantUnitTest.getInstance().searchRestaurantByType(reader);
					break;
				case "3":
					SearchRestaurantUnitTest.getInstance().searchRestaurantReview(reader);
					break;
				case "4":
					SearchRestaurantUnitTest.getInstance().searchRestaurantByStar(reader);
					break;
				case "5":
					System.out.println("Customer 메인 페이지로 돌아갑니다.");
					return;
				case "exit":
					System.out.println("종료합니다.");
					System.exit(0);
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printSearchRestaurantMenu() {
		System.out.println(
				"\n============================================================================================");
		System.out.println("                               *** Customer 조회 서비스 ***");
		System.out.println(
				"============================================================================================");
		System.out.println("                                    1. 식당 전체 조회");
		System.out.println("                                    2. 식당 타입별 조회");
		System.out.println("                                    3. 해당 식당 리뷰 조회");
		System.out.println("                                    4: 평균 별점 높은순 식당 조회");
		System.out.println("                                    5. 뒤로가기");
		System.out.println("                                    6. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}
