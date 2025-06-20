package com.tablepick.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.service.OwnerService;
import com.tablepick.session.SessionManager;


//식당 주인이 자신의 식당의 리뷰를 조회할 수 있습니다.

public class UIOwnerReviewDetail {
	
	private static UIOwnerReviewDetail instance = new UIOwnerReviewDetail();
	private UIOwnerReviewDetail() {
	}
	public static UIOwnerReviewDetail getInstance() {
		return instance;
	}
	
	public void run() throws NotFoundAccountException {
		Scanner sc = new Scanner(System.in);
		OwnerService service = new OwnerService();
		List list = new ArrayList<>();
		boolean exit = false;

		String console;



		//세션으로 id 가져오기
				String accountId = SessionManager.getLoginDataSession().getId();
				
		
		while (!exit) {
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.println("                          ");
			System.out.println("                  *** 내 식당의 리뷰를 조회할 수 있는 화면입니다. ***");
			System.out.println("                  ");
			System.out.println("                          1. 리뷰 조회하기");
			System.out.println("                          2. 뒤로 가기");
			System.out.println("                          3. 프로그램 종료하기");
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
		
			console = sc.nextLine();
			switch (console) {
			case "1":
				System.out.println("내 식당의 리뷰 목록을 조회합니다.");
				System.out.println("                          ");
				try {
					list = service.findMyRestaurantReview(accountId);
					if (list.isEmpty()) {
						System.out.println("등록된 리뷰가 없습니다.");
					}else {
						for (int i = 0; i < list.size(); i++) {
						System.out.println(list.get(i));
					}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "2":
				System.out.println("이전 화면으로 돌아갑니다.");
				exit = true;
				UIOwnerMainDetail.getInstance().run();
				break;
			case "3":
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

	public static void main(String[] args) throws NotFoundAccountException {
		new UIOwnerReviewDetail().run();
	}
}