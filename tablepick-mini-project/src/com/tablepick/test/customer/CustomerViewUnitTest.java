package com.tablepick.test.customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.test.ReserveCRUDView.ReserveCRUDUnitTest;
import com.tablepick.test.SearchRestaurant.SearchRestaurantUnitTest;

public class CustomerViewUnitTest {
	public CustomerViewUnitTest() throws ClassNotFoundException {
	}

	public static void main(String[] args) {
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        new CustomerViewUnitTest().run(reader);
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
	                case "1":
	                	UICustomerFindRestaurant.getInstance().run(reader);
	                    break;
	                case "2":
	                	UICustomerReserve.getInstance().run(reader);
	                    break;
	                case "3":
	                	//searchRestaurantUnitTest.run(reader);
	                    break;
	                case "4":
	                	System.out.println("로그아웃합니다.");
	                    return;
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
        System.out.println("\n============================================================================================");
		System.out.println("      /$$$$$$  /$$   /$$  /$$$$$$  /$$$$$$$$ /$$$$$$  /$$      /$$ /$$$$$$$$ /$$$$$$$ ");
        System.out.println("     /$$__  $$| $$  | $$ /$$__  $$|__  $$__//$$__  $$| $$$    /$$$| $$_____/| $$__  $$");
        System.out.println("    | $$  \\__/| $$  | $$| $$  \\__/   | $$  | $$  \\ $$| $$$$  /$$$$| $$      | $$  \\ $$");
        System.out.println("    | $$      | $$  | $$|  $$$$$$    | $$  | $$  | $$| $$ $$/$$ $$| $$$$$   | $$$$$$$/");
        System.out.println("    | $$      | $$  | $$ \\____  $$   | $$  | $$  | $$| $$  $$$| $$| $$__/   | $$__  $$");
        System.out.println("    | $$    $$| $$  | $$ /$$  \\ $$   | $$  | $$  | $$| $$\\  $ | $$| $$      | $$  \\ $$");
        System.out.println("    |  $$$$$$/|  $$$$$$/|  $$$$$$/   | $$  |  $$$$$$/| $$ \\/  | $$| $$$$$$$$| $$  | $$");
        System.out.println("     \\______/  \\______/  \\______/    |__/   \\______/ |__/     |__/|________/|__/  |__/");
        System.out.println("============================================================================================");
        System.out.println("                               *** Customer 메인 서비스 ***");
        System.out.println("============================================================================================");
        System.out.println("                                    1. 식당 조회");
        System.out.println("                                    2. 식당 예약");
        System.out.println("                                    3. 식당 리뷰");
        System.out.println("                                    4. 로그아웃");
        System.out.println("                                    5. 서비스 종료하기");
        System.out.println("============================================================================================");
        System.out.print("메뉴를 선택하세요: ");
    } 
}
