package com.tablepick.test.owner;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;
import com.tablepick.service.OwnerService;
import com.tablepick.session.SessionManager;

public class TestInputCustomerSales {

	private static TestInputCustomerSales instance = new TestInputCustomerSales();

	private TestInputCustomerSales() {
	}

	public static TestInputCustomerSales getInstance() {
		return instance;
	}

	public void run() {

		boolean create = false;
		while (!create) {
			
			// 해당 식당의 예약을 조회하고 예약 당 매출액을 입력 가능하게 한다.
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				OwnerService service = new OwnerService();

				AccountVO loginData = null;


//				try {
//					loginData = TablePickSerivceCommon.getInstance().getLoginData();
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				String accountId = loginData.getId();
				//세션으로 id 가져오기
				String accountId = SessionManager.getLoginDataSession().getId();


				String reservationIdxStr = null;
				String sale = null;

				List<Map<String, String>> reservationList = service.findMyRestaurantReservationList(accountId);
				System.out.println("                  ");
				System.out.println("                          *** 식당 예약 자 명단 *** ");
				System.out.println("                  ");
				for (int i = 0; i < reservationList.size(); i++) {
					System.out.println(reservationList.get(i));
				}

				System.out.println("매출액을 입력할 예약 번호를 입력해 주세요.");
		
				reservationIdxStr = br.readLine();
				int reservationIdx = Integer.parseInt(reservationIdxStr);
				System.out.println("                  ");
				// 예약자 조회
				System.out.println("                          *** 선택한 예약자 정보입니다. ***");
				System.out.println("                  ");
				Map<String, String> selectedCustomer = service.findSelectedCustomer(accountId, reservationIdx);

				if (selectedCustomer.isEmpty()) {
					System.out.println("입력하신 예약 번호는 존재하지 않습니다. 다시 입력해 주세요.");
				} else {
					for (Map.Entry<String, String> entry : selectedCustomer.entrySet()) {
						System.out.println(entry.getKey() + " : " + entry.getValue());
					}

					System.out.println("수정할 매출액을 입력해 주세요. ");
					sale = br.readLine();
					int newSale = Integer.parseInt(sale);

					service.updateCustomerSale(accountId, reservationIdx, newSale);
					System.out.println("매출액이 성공적으로 입력되었습니다.");
					create = true;

				}

			} catch (NumberFormatException e) {
				System.out.println("숫자 형식이 올바르지 않습니다.");
			} catch (NoReservationException e) {
				System.out.println("해당하는 예약 번호는 존재하지 않습니다. 다시 입력하세요.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} // while

	}

	public static void main(String[] args) throws NoReservationException {

		new TestInputCustomerSales().run();
	}

}