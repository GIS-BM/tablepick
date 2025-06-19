package com.tablepick.Restaurant.UnitTest;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantDao;
import com.tablepick.service.CommonService;

public class MostReservedCustomersTest {

	private static MostReservedCustomersTest instance = new MostReservedCustomersTest();

	private MostReservedCustomersTest() {
	}

	public static MostReservedCustomersTest getInstance() {
		return instance;
	}

	public void run() {
		// 해당 식당의 예약을 가장 많이 한 고객 리스트
		// 한 명만 출력할지 리스트로 최다 예약자 순으로 뽑을지 선택 필요
		try {
			RestaurantDao restaurantDao = new RestaurantDao();

			AccountVO loginData = null;

			try {
				loginData = CommonService.getInstance().getLoginData();
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
//				e.printStackTrace();
			}

			String accountId = loginData.getId();
			
			//System.out.println(accountId);

			List<Map<String, String>> mostReservationList = restaurantDao.findMyRestaurantReservationMostList(accountId);

			System.out.println("                  *** 내 식당의 최다 예약자를 확인합니다. ***");
			System.out.println("                  ");
			for (int i = 0; i < mostReservationList.size(); i++) {
				System.out.println(mostReservationList.get(i));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) throws NoReservationException {
	}

}
