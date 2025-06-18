package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

//식당 상세 정보 화면입니다.
//식당주인 메인 화면에서 넘어옵니다.
public class OwnerMainDetail {

	private static OwnerMainDetail instance = new OwnerMainDetail();
	private OwnerMainDetail() {
	}
	public static OwnerMainDetail getInstance() {
		return instance;
	}

	public void run() {

		boolean exit = false;

		while (!exit) {

			Scanner sc = new Scanner(System.in);

			String console;
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.println("                          ");
			System.out.println("                  *** 식당 상세 정보를 관리할 수 있는 화면입니다. *** ");
			System.out.println("                  ");

			System.out.println("                          [등록된 내 식당 정보] ");
			System.out.println("                  ");
			TestSelectRestaurantAndSales.getInstance().run();
			System.out.println("                  ");

			System.out.println("                          1. 식당 상세 정보 변경");
			System.out.println("                          2. 식당 메뉴 관리");
			System.out.println("                          3. 내 식당 예약자 조회하기");
			System.out.println("                          4. 내 식당 리뷰 조회하기");
			System.out.println("                          5. 뒤로가기");
			System.out.println("                          6. 프로그램 종료하기");
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			
			console = sc.nextLine();

			switch (console) {
			case "1":
				TestUpdateRestaurantAndSales.getInstance().run();
				break;
			case "2":
				OwnerMenuDetail.getInstance().run();
				break;
			case "3":
				OwnerReserveCustomerDetail.getInstance().run();
				break;
			case "4":
				OwnerReviewDetail.getInstance().run();
				break;
			case "5":
				OwnerMain.getInstance().run();
				break;
			case "6":
				System.out.println("프로그램을 종료합니다.");
				exit = true;
				System.exit(0); // 시스템 종료
				break;
			default:
				System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
				break;
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new OwnerMainDetail().run();
	}
}
