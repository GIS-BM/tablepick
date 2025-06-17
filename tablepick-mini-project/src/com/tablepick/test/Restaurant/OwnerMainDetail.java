package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

//식당 상세 정보 화면입니다.
//식당주인 메인 화면에서 넘어옵니다.
public class OwnerMainDetail {

	public void run() {
		Scanner sc = new Scanner(System.in);

		String console;

		System.out.println("                  식당 상세 정보를 관리할 수 있는 화면입니다.");
		System.out.println("                          1. 식당 상세 정보 변경");
		System.out.println("                          2. 식당 메뉴 관리");
		System.out.println("                          3. 내 식당 리뷰 조회하기");
		System.out.println("                          4. 뒤로가기");
		System.out.println("                          5. 프로그램 종료하기");

		console = sc.nextLine();

		switch (console) {
		case "1":
			System.out.println("식당 상세 정보를 변경합니다.");
			break;
		case "2":
			new TestMenu().run();
			break;
		case "3":
			new TestReview().run();
			break;
		case "4":
			System.out.println("이전 화면으로 돌아갑니다.");
			new OwnerMain().run();
			break;
		case "5":
			System.out.println("프로그램을 종료합니다.");
			break;
		default:
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			break;
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new OwnerMainDetail().run();
	}
}
