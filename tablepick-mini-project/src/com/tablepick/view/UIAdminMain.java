package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.AdminService;

public class UIAdminMain {
	private static UIAdminMain instance = new UIAdminMain();

	private UIAdminMain() {
	}

	public static UIAdminMain getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			new UIAdminMain().run(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(BufferedReader reader) {
		try {
			while (true) {
				printAdminMenu();
				String main = reader.readLine().trim();
				switch (main) {
				// 전체 계정 조회
				case "1":
					AdminService.getInstance().searchAllAccount();
					break;
				// 계정 조회
				case "2":
					AdminService.getInstance().searchAccount(reader);
					break;
				// 전체 예약 조회
				case "3":
					AdminService.getInstance().searchAllReserve();
					break;
				// 최대 예약자 조회
				case "4":
					AdminService.getInstance().searchMostReserve();
					break;
				// main UI 로 이동
				case "5":
					System.out.println("로그아웃합니다.");
					return;
				// 서비스 종료
				case "6":
					System.out.println("종료합니다.");
					System.exit(0);
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printAdminMenu() {
		System.out.println(
				"\n============================================================================================");
		System.out.println("                                  ._________________.");
        System.out.println("                                  |.---------------.|");
        System.out.println("                                  ||               ||");
        System.out.println("                                  ||     ADMIN     ||");
        System.out.println("                                  ||               ||");
        System.out.println("                                  ||     PAGE      ||");
        System.out.println("                                  ||               ||");
        System.out.println("                                  ||_______________||");
        System.out.println("                                  /.-.-.-.-.-.-.-.-.\\");
        System.out.println("                                 /.-.-.-.-.-.-.-.-.-.\\");
        System.out.println("                                /.-.-.-.-.-.-.-.-.-.-.\\");
        System.out.println("                               /______/__________\\___o_\\ ");
        System.out.println("                               \\_______________________/");	      
        System.out.println("                                                                                  ");
		System.out.println(
				"============================================================================================");
		System.out.println("                                *** Admin 메인 서비스 ***");
		System.out.println(
				"============================================================================================");
		System.out.println("                                   1. 전체 회원 조회");
		System.out.println("                                   2. 회원 정보 검색");
		System.out.println("                                   3. 전체 예약 목록");
		System.out.println("                                   4. 최대 예약자 조회");
		System.out.println("                                   5. 로그아웃");
		System.out.println("                                   6. 서비스 종료하기");
		System.out.println(
				"============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
		 
	}
}
