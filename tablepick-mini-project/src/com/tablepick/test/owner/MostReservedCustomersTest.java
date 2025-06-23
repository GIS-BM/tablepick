package com.tablepick.test.owner;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;

import com.tablepick.session.SessionManager;

import com.tablepick.service.OwnerService;


public class MostReservedCustomersTest {

	private static MostReservedCustomersTest instance = new MostReservedCustomersTest();

	private MostReservedCustomersTest() {
	}

	public static MostReservedCustomersTest getInstance() {
		return instance;
	}

	public void run() {
		/*
		 * 해당 식당의 예약을 가장 많이 한 고객 리스트 조회 클래스
		 * 최다 예약 고객 한 명 출력
		 */
		
		try {
			OwnerService service = new OwnerService();

			String accountId = SessionManager.getLoginDataSession().getId();

			List<Map<String, String>> mostReservationList = service.findMyRestaurantReservationMostList(accountId);

			System.out.println("                  *** 내 식당의 최다 예약자를 확인합니다. ***");
			System.out.println("                  ");
			
			if (mostReservationList.isEmpty()) {
//				System.out.println("예약자가 존재하지 않습니다.");
				throw new NoReservationException("예약이 존재하지 않습니다.");
			} else {
				for (int i = 0; i < mostReservationList.size(); i++) {
					System.out.println(mostReservationList.get(i));
				}
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws NoReservationException {
	}

}
