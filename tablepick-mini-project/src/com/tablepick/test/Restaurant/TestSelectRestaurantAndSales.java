package com.tablepick.test.Restaurant;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

public class TestSelectRestaurantAndSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 식당 정보와 총 매출액을 조회한다.
		try {
			RestaurantDao resDao = new RestaurantDao();

			String accountId = "owner01";
//		String password = "own01";
			int reservationIdx = 1;
			RestaurantVO res = resDao.checkMyRestaurantAndReservation(accountId, reservationIdx);
			if (res != null) {
				System.out.println("** 내 식당 조회 **");
				System.out.println(res);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}