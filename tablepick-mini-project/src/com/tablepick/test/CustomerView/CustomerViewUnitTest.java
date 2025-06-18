package com.tablepick.test.CustomerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.test.ReserveCRUDView.ReserveCRUDUnitTest;
import com.tablepick.test.SearchRestaurant.SearchRestaurantUnitTest;

public class CustomerViewUnitTest {
	private SearchRestaurantUnitTest searchRestaurantUnitTest;
	private ReserveCRUDUnitTest reserveCRUDUnitTest;

	public CustomerViewUnitTest() throws ClassNotFoundException {
		searchRestaurantUnitTest = new SearchRestaurantUnitTest();
		reserveCRUDUnitTest = new ReserveCRUDUnitTest();
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
	            System.out.println("\n=== Customer 메인 페이지 ===");
	            System.out.println("1. 식당 조회");
	            System.out.println("2. 식당 예약");
	            System.out.println("3. 식당 리뷰");
	            System.out.println("4. 로그아웃");
	            System.out.println("exit: 종료");
	            System.out.print("입력 : ");

	            String main = reader.readLine().trim();
	            switch (main) {
	                case "1":
	                	searchRestaurantUnitTest.run(reader);
	                    break;
	                case "2":
	                	reserveCRUDUnitTest.run(reader);
	                    break;
	                case "3":
	                	searchRestaurantUnitTest.run(reader);
	                    break;
	                case "4":
	                	System.out.println("로그아웃합니다.");
	                    return;
	                case "exit":
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
}
