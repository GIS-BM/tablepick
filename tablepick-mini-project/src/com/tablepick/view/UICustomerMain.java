package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.CommonService;

public class UICustomerMain {
	private static UICustomerMain instance = new UICustomerMain();

	private UICustomerMain() {

	}

	public static UICustomerMain getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			UICustomerMain.getInstance().run(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(BufferedReader reader) {
		try {
			while (true) {
				printCustomerMenu();
				String main = reader.readLine().trim();
				switch (main) {
				// 식당 조회 view 로 이동
				case "1":
					UICustomerFindRestaurant.getInstance().run(reader);
					break;
				// 식당 예약 view 로 이동 
				case "2":
					UICustomerReserve.getInstance().run(reader);
					break;
				// 식당 리뷰 view 로 이동
				case "3":
					UICustomerReview.getInstance().run(reader);
					break;
				// main UI 로 이동
				case "4":
					System.out.println("로그아웃합니다.");
					CommonService.getInstance().logoutSession();
					return;
				// 서비스 종료
				case "5":
					System.out.println("종료합니다.");
					System.exit(0);
					break;
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printCustomerMenu() {
		System.out.println(
				"\n============================================================================================");
		System.out.println("                             __...--~~~~~-._   _.-~~~~~--...__");
		System.out.println("                           //               `V'               \\\\ ");
		System.out.println("                          //     CUSTOMER    |     PAGE        \\\\ ");
		System.out.println("                         //__...--~~~~~~-._  |  _.-~~~~~~--...__\\\\ ");
		System.out.println("                        //__.....----~~~~._\\ | /_.~~~~----.....__\\\\");
		System.out.println("                       ====================\\\\|//====================");
		System.out.println("                                           `---`");
		System.out.println(
				"============================================================================================");
		System.out.println("                               *** Customer 메인 서비스 ***");
		System.out.println(
				"============================================================================================");
		System.out.println("                                    1. 식당 조회");
		System.out.println("                                    2. 식당 예약");
		System.out.println("                                    3. 식당 리뷰");
		System.out.println("                                    4. 로그아웃");
		System.out.println("                                    5. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");

	
	}
}
