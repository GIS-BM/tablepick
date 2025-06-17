package com.tablepick.test.Restaurant;

import java.util.ArrayList;
import java.util.Scanner;

import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantDao;
import com.tablepick.service.TablePickSerivceCommon;

//식당 등록을 테스트하는 클래스 입니다.

public class TestCreateRestaurant {
	
	public void run() {

		AccountVO loginData = null;
		try {
			loginData = TablePickSerivceCommon.getInstance().getLoginData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String accountId = loginData.getId();
		RestaurantDao dao = new RestaurantDao();
		ArrayList<String> restaurantType = new ArrayList<String>();
		restaurantType.add("한식");
		restaurantType.add("중식");
		restaurantType.add("일식");
		restaurantType.add("양식");
		restaurantType.add("카페/베이커리");
		restaurantType.add("해산물");
		restaurantType.add("주점");
		restaurantType.add("기타");

		Scanner sc = new Scanner(System.in);

		String id;
		String name;
		String type = null;
		String address;
		String tel;
		String opentime;

		boolean typeIsCorrect = true;

		System.out.println("                  식당을 등록합니다.");

		System.out.println("1. 식당 이름을 입력하세요: ");
		name = sc.nextLine();

		while (typeIsCorrect) {
			System.out.println("2. 식당 타입 ('한식', '중식', '일식', '양식', '카페/베이커리', '해산물', '주점', '기타') 을 골라주세요.");
			type = sc.nextLine();

			if (restaurantType.contains(type)) {
				typeIsCorrect = false;
			} else {
				System.out.println("타입이 틀렸습니다. 다시 입력하세요.");
			}
		}

		System.out.println("3. 식당 위치를 입력하세요: ");
		address = sc.nextLine();
		System.out.println("4. 식당 전화번호를 입력하세요: ");
		tel = sc.nextLine();
		System.out.println("5. 식당 오픈 시간을 입력하세요. 예시: 09:00");
		opentime = sc.nextLine();
		opentime = opentime + ":00";
		
		//RestaurantDao 업데이트시 주석 해제
		//RestaurantVO vo1 = new RestaurantVO(accountId, name, type, address, tel, opentime);

//		try {
			//RestaurantDao 업데이트시 주석 해제
			System.out.println("식당이 등록되었습니다.");
			//System.out.println("id + " + dao.makeRestaurant(vo1));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



		
	}
	
	public static void main(String[] args) {
		
		new TestCreateRestaurant().run();
	}

}
