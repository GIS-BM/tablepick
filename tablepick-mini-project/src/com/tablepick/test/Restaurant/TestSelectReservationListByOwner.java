package com.tablepick.test.Restaurant;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.RestaurantDao;

public class TestSelectReservationListByOwner {
	
	private static TestSelectReservationListByOwner instance = new TestSelectReservationListByOwner();
	private TestSelectReservationListByOwner() {
	}
	public static TestSelectReservationListByOwner getInstance() {
		return instance;
	}
	
	public void run() {
		// 식당 주인이 조회하는 예약 리스트
				try {
					RestaurantDao restaurantDao = new RestaurantDao();
					String accountId = "owner01";
					
					List<Map<String, String>> reservationList = restaurantDao.checkMyRestaurantReservationList(accountId);
					
					System.out.println("                  *** 내 식당 예약자 명단 *** ");
					for (int i = 0; i < reservationList.size(); i++) {
						System.out.println(reservationList.get(i));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	
	public static void main(String[] args) throws NoReservationException {
		new TestSelectReservationListByOwner().run();
	}
}
