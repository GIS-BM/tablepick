package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

//식당 상세 정보 화면입니다.
//식당주인 메인 화면에서 넘어옵니다.
public class OwnerMainDetail {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		OwnerMain om = new OwnerMain();
		TestMenu tm = new TestMenu();
		TestReview tr = new TestReview();
		
		String console;
		
		System.out.println("식당 상세 정보를 관리할 수 있는 화면입니다.");
		System.out.println("1. 식당 상세 정보 변경 2. 식당 메뉴 관리 3. 내 식당 리뷰 조회하기 4. 뒤로가기 5. 프로그램 종료하기");
		
		console = sc.nextLine();
		
		switch (console) {
		case "1" :
			System.out.println("식당 상세 정보를 변경합니다.");
			break;
		case "2" :
			tm.main(args);
			break;
		case "3" :
			tr.main(args);
			break;
		case "4" :
			System.out.println("이전 화면으로 돌아갑니다.");
			om.main(args);
			break;
		case "5" :
			System.out.println("프로그램을 종료합니다.");
			break;
		default :
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			break;
		}
	}
}
