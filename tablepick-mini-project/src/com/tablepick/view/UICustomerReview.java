package com.tablepick.view;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.CustomerService;


public class UICustomerReview {
	private static UICustomerReview instance = new UICustomerReview();

	private UICustomerReview() {
	}

	public static UICustomerReview getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			new UICustomerReview().run(reader);
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
				// 리뷰 등록
				case "1":
					CustomerService.getInstance().createReview();
					break;
				// 리뷰 검색
				case "2":
					CustomerService.getInstance().findMyReviewById();
					break;
				// 리뷰 수정
				case "3":
					CustomerService.getInstance().updateReviewById();
					break;
				// 리뷰 삭제
				case "4":
					CustomerService.getInstance().deleteMyReviewById();;
					break;
				// customer main UI 로 이동 
				case "5":
					System.out.println("이전 화면으로 돌아갑니다.");
					return;
				// 서비스 종료
				case "0":
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
		System.out.println("                                 *** 식당 리뷰 서비스 ***");
		System.out.println(
				"============================================================================================");
		System.out.println("                                    1. 식당 리뷰 등록");
		System.out.println("                                    2. 내 리뷰 검색");
		System.out.println("                                    3. 내 리뷰 수정");
		System.out.println("                                    4: 내 리뷰 삭제");
		System.out.println("                                    5. 뒤로가기");
		System.out.println("                                    0. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}