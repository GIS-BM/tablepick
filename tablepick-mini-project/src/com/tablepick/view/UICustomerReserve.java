package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.test.customer.CustomerUnit;

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
				// 식당 예약
				case "1":
					CustomerUnit.getInstance().reserveRestaurant(reader);
					break;
				// 예약 조회
				case "2":
					CustomerUnit.getInstance().readReserve(reader);
					break;
				// 예약 수정
				case "3":
					CustomerUnit.getInstance().reserveUpdate(reader);
					break;
				// 예약 삭제
				case "4":
					CustomerUnit.getInstance().reserveDelete(reader);
					break;
				// customer main UI 로 이동
				case "5":
					System.out.println("Customer 메인 페이지로 돌아갑니다.");
					return;
				// 서비스 종료
				case "0":
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
		System.out.println("                                    0. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}
