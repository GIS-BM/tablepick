package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

//식당 등록을 테스트하는 클래스 입니다.

public class TestCreateRestaurant {
	public static void main(String[] args) {
		
		RestaurantDao dao = new RestaurantDao();
		
		Scanner sc = new Scanner(System.in);
		
		String id;
		String name;
		String type = null;
		String address;
		String tel;
		
		boolean typeIsCorrect = true;
		
	
		System.out.println("식당을 등록합니다.");
		System.out.println("1. 본인의 아이디를 입력하세요: ");
		id = sc.nextLine();
		System.out.println("2. 식당 이름을 입력하세요 : ");
		name = sc.nextLine();
		
		while (typeIsCorrect) {
			System.out.println("3. 식당 타입 (한식, 일식, 중식, 양식) 을 골라주세요.");
		type = sc.nextLine();
		
		if	(type.equals("한식") |type.equals("일식") || type.equals("중식") || type.equals("양식") ) {
			typeIsCorrect = false;
		}else {
			System.out.println("타입이 틀렸습니다. 다시 입력하세요.");
		}
		}
		
		System.out.println("4. 식당 위치를 입력하세요");
		address = sc.nextLine();
		System.out.println("5. 식당 전화번호를 입력하세요");
		tel = sc.nextLine();
		
		
		RestaurantVO vo1 = new RestaurantVO(id, name, type, address, tel );
		
		
		try {
			System.out.println("레스토랑이 등록되었습니다. id:"+dao.makeRestaurant(vo1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.close();
	}

}
