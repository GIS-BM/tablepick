package com.tablepick.view;

import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.test.owner.MostReservedCustomersTest;
import com.tablepick.test.owner.TestInputCustomerSales;
import com.tablepick.test.owner.TestSelectReservationListByOwner;

//식당을 예약한 예약자를 확인할수 있는 페이지 입니다.
//여기서 예약을 확인한 다음, 예약의 매출액을 적을 수 있습니다.
//매출액은 이후 OwnerMainDetail 페이지의 총 매출액으로 합산된 것을 확인할 수 있습니다.

public class UIOwnerReserveCustomerDetail {
	private static UIOwnerReserveCustomerDetail instance = new UIOwnerReserveCustomerDetail();

	private UIOwnerReserveCustomerDetail() {
	}

	public static UIOwnerReserveCustomerDetail getInstance() {
		return instance;
	}

	public void run() throws NotFoundAccountException, AccountNotFoundException, NotFoundRestaurantException {
		boolean exit = false;

		while (!exit) {

			Scanner sc = new Scanner(System.in);

			String console;
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.println("                          ");
			System.out.println("                  *** 내 식당의 예약자를 조회할 수 있습니다. *** ");
			System.out.println("                  ");
			System.out.println("                          1. 예약자 전체 조회하기");
			System.out.println("                          2. 가장 많이 예약한 사람 조회하기");
			System.out.println("                          3. 예약 당 매출액 입력 및 수정하기");
			System.out.println("                          4. 이전 화면으로 돌아가기");
			System.out.println("                          0. 프로그램 종료하기");
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.print("메뉴를 선택하세요: ");
			console = sc.nextLine();

			switch (console) {
			case "1":
				TestSelectReservationListByOwner.getInstance().run();
				break;
			case "2":
				MostReservedCustomersTest.getInstance().run();
				break;
			case "3":
				TestInputCustomerSales.getInstance().run();
				break;
			case "4":
				System.out.println("이전 화면으로 돌아갑니다.");
				System.out.println("                          ");
				UIOwnerMainDetail.getInstance().run();
				break;
			case "0":
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

	public static void main(String[] args) throws NotFoundAccountException, AccountNotFoundException, NotFoundRestaurantException {
		new UIOwnerReserveCustomerDetail().run();
	}
}
