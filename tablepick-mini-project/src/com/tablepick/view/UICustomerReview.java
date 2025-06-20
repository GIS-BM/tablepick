package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.CustomerService;

public class UICustomerReview {
	private static final UICustomerReview instance = new UICustomerReview();

	private final BufferedReader reader;
	private final CustomerService customerService;

	private UICustomerReview() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.customerService = CustomerService.getInstance();
	}

	public static UICustomerReview getInstance() {
		return instance;
	}

	public void run() {
		try {
			while (true) {
				printMenu();
				String input = reader.readLine().trim();
				switch (input) {
					case "1":
						customerService.createReview();
						break;
					case "2":
						customerService.findMyReviewById();
						break;
					case "3":
						customerService.updateReviewById();
						break;
					case "4":
						customerService.deleteMyReviewById();
						break;
					case "5":
						System.out.println("이전 화면으로 돌아갑니다.");
						return;
					case "0":
						System.out.println("서비스를 종료합니다.");
						System.exit(0);
					default:
						System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
				}
			}
		} catch (Exception e) {
			System.err.println("오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void printMenu() {
		System.out.println("\n============================================================================================");
		System.out.println("                                 *** 식당 리뷰 서비스 ***");
		System.out.println("============================================================================================");
		System.out.println("                                    1. 식당 리뷰 등록");
		System.out.println("                                    2. 내 리뷰 검색");
		System.out.println("                                    3. 내 리뷰 수정");
		System.out.println("                                    4. 내 리뷰 삭제");
		System.out.println("                                    5. 뒤로가기");
		System.out.println("                                    0. 서비스 종료하기");
		System.out.println("============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}
