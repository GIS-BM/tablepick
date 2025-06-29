package com.tablepick.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.service.AdminService;
import com.tablepick.service.CommonService;

public class UIAdminMain {
    private static final UIAdminMain instance = new UIAdminMain();
    private final BufferedReader reader;

    // 생성자에서는 reader 초기화만 수행
    private UIAdminMain() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static UIAdminMain getInstance() {
        return instance;
    }

    public void run() {
        while (true) {
            try {
                printAdminMenu();
                String input = reader.readLine().trim();
                if (!handleMenuInput(input)) {
                    break; // 로그아웃 시 루프 종료
                }
            } catch (Exception e) {
                System.out.println("오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean handleMenuInput(String input) throws Exception {
        switch (input) {
            case "1":
                AdminService.getInstance().searchAllAccount();
                break;
            case "2":
                AdminService.getInstance().searchAccount(reader);
                break;
            case "3":
                AdminService.getInstance().searchAllReserve();
                break;
            case "4":
                AdminService.getInstance().searchMostReserve();
                break;
            case "5":
            	System.out.println("로그아웃이 완료되었으므로 프로그램 홈으로 돌아갑니다.\n");
    			CommonService.getInstance().logoutSession();
    			// 메인 화면으로 이동
    			UIIndex UIIndex = new UIIndex();
    			UIIndex.run();
    			return false; // 현재 CustomerMain 종료
            case "6":
            	System.out.println("이전 화면으로 돌아갑니다.\n");
            	CommonService.getInstance().logoutSession();
            	// 메인 화면으로 이동
            	UIIndex UIIndex1 = new UIIndex();
            	UIIndex1.run();
            	return false; // 현재 AdminMain 종료
            case "0":
                System.out.println("서비스를 종료합니다.");
                System.exit(0);
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
        }
        return true; // 계속 실행
    }

    private void printAdminMenu() {
		System.out.println("                          ");
        System.out.println(
            "============================================================================================");
		System.out.println("                          ");
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
		System.out.println("                          ");

        System.out.println(
            "============================================================================================");
		System.out.println("                          ");
        System.out.println("                           *** 어서오세요. 관리자 전용 페이지입니다. ***");
		System.out.println("                          ");
        System.out.println("                                1. 전체 회원 조회하기");
        System.out.println("                                2. 회원 정보 검색하기");
        System.out.println("                                3. 전체 예약 목록 조회하기");
        System.out.println("                                4. 최대 예약자 조회하기");
        System.out.println("                                5. 로그아웃");
		System.out.println("                                6. 이전 화면으로 돌아가기");
		System.out.println("                                0. 프로그램 종료하기");
		System.out.println("                          ");		
        System.out.println(
            "============================================================================================");
        System.out.print("메뉴를 선택하세요: ");
    }
}