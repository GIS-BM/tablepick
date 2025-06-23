package com.tablepick.view;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.CustomerService;

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
					CustomerService.getInstance().reserveRestaurant();
					break;
				// 예약 조회
				case "2":
					CustomerService.getInstance().readReserve();
					break;
				// 예약 수정
				case "3":
					CustomerService.getInstance().reserveUpdate();
					break;
				// 예약 삭제
				case "4":
					CustomerService.getInstance().reserveDelete();
					break;
				// customer main UI 로 이동
				case "5":
					System.out.println("이전 화면으로 돌아갑니다.");
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
				"\n============================================================================================\n");
		System.out.println("                            *** 식당 예약을 할 수 있는 화면입니다. ***");
		System.out.println("                  ");
		System.out.println("                                  1. 식당 예약하기");
		System.out.println("                                  2. 예약 확인하기");
		System.out.println("                                  3. 내 예약 변경하기");
		System.out.println("                                  4: 내 예약 삭제하기");
		System.out.println("                                  5. 이전 화면으로 돌아가기");
		System.out.println("                                  0. 프로그램 종료하기");
		System.out.println(
				"\n============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}
