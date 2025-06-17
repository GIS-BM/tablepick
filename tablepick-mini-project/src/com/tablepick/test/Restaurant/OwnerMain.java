package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.service.TablePickSerivceCommon;

//식당 주인의 메인 화면 입니다.
//식당 등록 화면, 식당 상세 정보 화면으로 이동할 수 있습니다. 

public class OwnerMain {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		OwnerMainDetail omd = new OwnerMainDetail();
		TestCreateRestaurant tcr = new TestCreateRestaurant();
		TestDeleteRestaurant tdr = new TestDeleteRestaurant();
		// 테스트용 아이디 (추후 삭제)
		TablePickSerivceCommon.getInstance().login("owner01", "pw1234");
		//이 로그인 데이터로 하위 페이지에서 로그인 아이디를 사용할 수 있습니다.
		
		String console;

		System.out.println("*** 어서오세요. 식당을 관리할 수 있는 페이지 입니다. ***");
		System.out.println("1. 내 식당 등록하기 2. 내 식당 상세 정보 확인하기 3. 내 식당 삭제하기 4. 로그아웃 5. 뒤로가기 6. 프로그램 종료하기");

		console = sc.nextLine();

		switch (console) {
		case "1":
			tcr.main(args);
			break;
		case "2":
			System.out.println("식당 상세 정보를 확인합니다.");
			omd.main(args);
			break;
		case "3":
			tdr.main(args);
			break;
		case "4":
			System.out.println("로그아웃 합니다.");
			break;
		case "5":
			System.out.println("이전 화면으로 돌아갑니다. (로직 추가 필요)");
			break;
		case "6":
			System.out.println("프로그램을 종료합니다.");
			break;
		default:
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			break;
		}
	}

}
