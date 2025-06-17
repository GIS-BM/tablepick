package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tablepick.model.RestaurantDao;

//식당 주인이 자신의 식당의 리뷰를 조회할 수 있습니다.

public class TestReview {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		RestaurantDao dao = new RestaurantDao();
		OwnerMainDetail omd = new OwnerMainDetail();
		List list = new ArrayList<>();
		
		String console;
		//해당 식당의 id는 하드코딩 한 상태
		int restaurantId = 1;
		
		System.out.println("내 식당의 리뷰를 조회합니다.");
		System.out.println("1. 리뷰 조회하기 2. 뒤로 가기 3. 프로그램 종료하기");
		
		console = sc.nextLine();
		
		switch (console) {
		case "1" :
			System.out.println("내 식당의 리뷰 목록을 조회합니다.");
			try {
				list = dao.checkMyRestaurantReview(restaurantId);
				for(int i = 0; i < list.size();i++) {
					System.out.println(list.get(i));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "2" :
			System.out.println("이전 화면으로 돌아갑니다.");
			omd.main(args);
			break;
		case "3" :
			System.out.println("프로그램을 종료합니다.");
			break;
		default :
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			break;
		}
	}
}
