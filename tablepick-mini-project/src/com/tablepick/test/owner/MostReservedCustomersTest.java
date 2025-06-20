package com.tablepick.test.owner;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;

import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;


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
			OwnerService service = new OwnerService();

			AccountVO loginData = null;


//			try {
//				loginData = TablePickSerivceCommon.getInstance().getLoginData();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			String accountId = loginData.getId();
			//세션으로 id 가져오기
			String accountId = SessionManager.getLoginDataSession().getId();

			
			//System.out.println(accountId);

			List<Map<String, String>> mostReservationList = service.findMyRestaurantReservationMostList(accountId);

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
