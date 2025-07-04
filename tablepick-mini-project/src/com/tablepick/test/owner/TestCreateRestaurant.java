package com.tablepick.test.owner;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantVO;
import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;
import com.tablepick.session.SessionManager;

public class TestCreateRestaurant {

	private static TestCreateRestaurant instance = new TestCreateRestaurant();

	private TestCreateRestaurant() {
	}

	public static TestCreateRestaurant getInstance() {
		return instance;
	}

	public void run() {
		/*
		 * 새로운 식당의 정보를 등록을 테스트하는 클래스
		 */

		//세션으로 id 가져오기
		String accountId = SessionManager.getLoginDataSession().getId();

		OwnerService service = new OwnerService();
		
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
		String time;

		boolean typeIsCorrect = true;

		try {

			if (service.existRestaurant(accountId) == true) {
				System.out.println("                          ");
				System.out.println("이미 식당이 존재합니다. 새로 등록하시려면 기존 식당을 삭제하세요.");
				System.out.println("                          ");
			} else {

				System.out.println("                        ***  식당을 등록합니다.  *** ");

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
				
				LocalTime opentime = null;
				   while (true) {
			            try {
			                System.out.println("5. 식당 오픈 시간을 입력하세요. 예시: 09:00");
			                time = sc.nextLine();
			               opentime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
			                break;
			            } catch (DateTimeParseException e) {
			                System.out.println("잘못된 시간 형식입니다. 예시처럼 HH:mm 형식으로 입력해주세요.");
			            }
			        }

				RestaurantVO vo1 = new RestaurantVO(accountId, name, type, address, tel, opentime);

				try {
					service.createRestaurant(vo1);
					System.out.println("식당이 등록되었습니다.");
				} catch (SQLException e) {

					System.out.println(e.getMessage());
				} 
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		new TestCreateRestaurant().run();
	}

}