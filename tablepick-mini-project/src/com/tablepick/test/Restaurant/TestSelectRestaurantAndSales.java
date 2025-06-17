package com.tablepick.test.Restaurant;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;

public class TestSelectRestaurantAndSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 식당 정보와 총 매출액을 조회한다.
		try {
			RestaurantDao resDao = new RestaurantDao();

			String accountId = "owner01";
			int reservationIdx = 1;
			
			List<Map<String, String>> resList = resDao.checkMyRestaurantAndSales(accountId, reservationIdx);
			
			for (int i = 0; i < resList.size(); i++) {
				Map<String, String> map = resList.get(i);
				
				String name = map.get("name");
				String type = map.get("type");
				String address = map.get("address");
				String tel = map.get("tel");
				String sales = map.get("sales");
				System.out.println("식당 명 : " + name + ", 타입 : " + type +  ", 주소 : " + address + ", 연락처 : " + tel + ", 매출액: " + sales);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}