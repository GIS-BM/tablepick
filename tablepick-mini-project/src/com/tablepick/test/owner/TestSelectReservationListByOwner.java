package com.tablepick.test.owner;

import java.util.List;

import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;
import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;

public class TestSelectReservationListByOwner {

	private static TestSelectReservationListByOwner instance = new TestSelectReservationListByOwner();

	private TestSelectReservationListByOwner() {
	}

	public static TestSelectReservationListByOwner getInstance() {
		return instance;
	}

	public void run() {
		/*
		 * 식당 주인이 본인의 예약자 명단 리스트를 조회하는 클래스
		 */
		try {
			OwnerService service = new OwnerService();
	
			String accountId = SessionManager.getLoginDataSession().getId();

			List<Map<String, String>> reservationList = service.findMyRestaurantReservationList(accountId);
			
			System.out.println("                        ");
			System.out.println("                        *** 내 식당 예약자 명단 *** ");
			System.out.println("                  ");
			if (reservationList.isEmpty()) {
				throw new NoReservationException("예약이 존재하지 않습니다.");
			} else {
				for (int i = 0; i < reservationList.size(); i++) {
					System.out.println(reservationList.get(i));
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws NoReservationException {
		new TestSelectReservationListByOwner().run();
	}
}
