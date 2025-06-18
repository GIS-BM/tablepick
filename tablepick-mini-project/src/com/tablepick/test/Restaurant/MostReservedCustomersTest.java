package com.tablepick.test.Restaurant;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.RestaurantDao;

public class MostReservedCustomersTest {
	public static void main(String[] args) throws NoReservationException {
		// 해당 식당의 예약을 가장 많이 한 고객 리스트
		// 한 명만 출력할지 리스트로 최다 예약자 순으로 뽑을지 선택 필요
		try {
			RestaurantDao restaurantDao = new RestaurantDao();
			String accountId = "owner01";
			
			List<Map<String, String>> mostReservationList = restaurantDao.checkMostReservedCustomersTest(accountId);
//			List<Map<String, String>> mostReservationList = restaurantDao.checkMyRestaurantReservationList(accountId);
			
			System.out.println("** 우리 식당 최다 예약자 ** ");
			for (int i = 0; i < mostReservationList.size(); i++) {
				System.out.println(mostReservationList.get(i));
			}
//			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
