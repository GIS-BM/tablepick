package com.tablepick.Restaurant.UnitTest;

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
		// 식당 주인이 조회하는 예약 리스트
		try {
			OwnerService service = new OwnerService();
			AccountVO loginData = null;

//					try {
//						loginData = TablePickSerivceCommon.getInstance().getLoginData();
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					
//					String accountId = loginData.getId();
			// 세션으로 id 가져오기
			String accountId = SessionManager.getLoginDataSession().getId();

			List<Map<String, String>> reservationList = service.findMyRestaurantReservationList(accountId);

			System.out.println("                        *** 내 식당 예약자 명단 *** ");
			if (reservationList.isEmpty()) {
				System.out.println("                        예약자가 없습니다. ");
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
