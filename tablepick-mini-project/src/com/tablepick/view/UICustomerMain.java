package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.CommonService;

public class UICustomerMain {
	private static final UICustomerMain instance = new UICustomerMain();
	private BufferedReader reader;

	// 생성자에서 run() 호출 제거 및 reader 초기화만 수행
	private UICustomerMain() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public static UICustomerMain getInstance() {
		return instance;
	}

	public void run() {
		while (true) {
			try {
				printCustomerMenu();
				String input = reader.readLine().trim();
				handleMenuInput(input);
			} catch (Exception e) {
				System.out.println("오류가 발생했습니다: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private boolean handleMenuInput(String input) throws Exception {
		switch (input) {
		case "1":
			UICustomerFindRestaurant.getInstance().run(reader);
			break;
		case "2":
			UICustomerReserve.getInstance().run(reader);
			break;
		case "3":
			UICustomerReview.getInstance().run();
			break;
		case "4":
			System.out.println("로그아웃합니다.");
			CommonService.getInstance().logoutSession();
			// 메인 화면으로 이동
			UIIndex UIIndex = new UIIndex();
			UIIndex.run();
			return false; // 현재 CustomerMain 종료
		case "0":
			System.out.println("서비스를 종료합니다.");
			System.exit(0);
			break;
		default:
			System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
		}
		return true;
	}

	private void printCustomerMenu() {
		System.out
				.println("\n============================================================================================");
		System.out.println("                             __...--~~~~~-._   _.-~~~~~--...__");
		System.out.println("                           //               `V'               \\\\ ");
		System.out.println("                          //     CUSTOMER    |     PAGE        \\\\ ");
		System.out.println("                         //__...--~~~~~~-._  |  _.-~~~~~~--...__\\\\ ");
		System.out.println("                        //__.....----~~~~._\\ | /_.~~~~----.....__\\\\");
		System.out.println("                       ====================\\\\|//====================");
		System.out.println("                                           `---`");
		System.out.println("============================================================================================");
		System.out.println("                               *** Customer 메인 서비스 ***");
		System.out.println("============================================================================================");
		System.out.println("                                    1. 식당 조회");
		System.out.println("                                    2. 식당 예약");
		System.out.println("                                    3. 식당 리뷰");
		System.out.println("                                    4. 로그아웃");
		System.out.println("                                    0. 서비스 종료하기");
		System.out.println("============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}