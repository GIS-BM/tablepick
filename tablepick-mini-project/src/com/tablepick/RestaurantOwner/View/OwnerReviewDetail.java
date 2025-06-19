package com.tablepick.RestaurantOwner.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantDao;
import com.tablepick.service.TablePickSerivceCommon;

//식당 주인이 자신의 식당의 리뷰를 조회할 수 있습니다.

public class OwnerReviewDetail {
	
	private static OwnerReviewDetail instance = new OwnerReviewDetail();
	private OwnerReviewDetail() {
	}
	public static OwnerReviewDetail getInstance() {
		return instance;
	}
	
	public void run() {
		Scanner sc = new Scanner(System.in);
		RestaurantDao dao = new RestaurantDao();
		List list = new ArrayList<>();
		boolean exit = false;

		String console;
		// 메뉴를 생성할 시 해당 식당의 id를 받아와야 합니다.
		// 1. 따라서 로그인 정보의 accountId를 받아온 후
		// 2. 이 정보를 가지고 restaurantId 를 조회합니다.
		AccountVO loginData = null;
		try {
			loginData = TablePickSerivceCommon.getInstance().getLoginData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String accountId = loginData.getId();

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
					list = dao.findMyRestaurantReview(accountId);
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
				OwnerMainDetail.getInstance().run();
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

	public static void main(String[] args) {
		new OwnerReviewDetail().run();
	}
}
