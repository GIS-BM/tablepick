package com.tablepick.test.CustomerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.test.ReserveCRUDView.ReserveCRUDUnitTest;

public class UICustomerReserve {
	private static UICustomerReserve instance = new UICustomerReserve();

	private UICustomerReserve() {
	}

	public static UICustomerReserve getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			UICustomerReserve.getInstance().run(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(BufferedReader reader) {
		try {
			while (true) {
				printReserveMenu();
				String main = reader.readLine().trim();
				switch (main) {
				case "1":
					ReserveCRUDUnitTest.getInstance().reserveRestaurantView(reader);
					break;
				case "2":
					ReserveCRUDUnitTest.getInstance().readReserveView(reader);
					break;
				case "3":
					ReserveCRUDUnitTest.getInstance().reserveUpdateView(reader);
					break;
				case "4":
					ReserveCRUDUnitTest.getInstance().reserveDeleteView(reader);
					break;
				case "5":
					System.out.println("Customer 메인 페이지로 돌아갑니다.");
					return;
				case "6":
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

	private void printReserveMenu() {
		System.out.println(
				"\n============================================================================================");
		System.out.println("                               *** Customer 예약 서비스 ***");
		System.out.println(
				"============================================================================================");
		System.out.println("                                    1. 식당 예약");
		System.out.println("                                    2. 예약 확인");
		System.out.println("                                    3. 예약 변경");
		System.out.println("                                    4: 예약 삭제");
		System.out.println("                                    5. 뒤로가기");
		System.out.println("                                    6. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}
