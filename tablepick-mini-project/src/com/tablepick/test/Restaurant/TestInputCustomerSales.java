package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.RestaurantDao;

public class TestInputCustomerSales {
	public static void main(String[] args) throws NoReservationException {
		// 해당 식당의 예약을 조회하고 예약 당 매출액을 입력 가능하게 한다.
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			String accountId = "owner01";
			String reservationIdxStr = null;
			
			List<Map<String, String>> reservationList = resDao.checkMyRestaurantReservationList(accountId);
			System.out.println("** 식당 예약 자 명단 ** ");
			for (int i = 0; i < reservationList.size(); i++) {
				System.out.println(reservationList.get(i));
			}
			
			System.out.println("매출액 입력할 예약 번호 입력");
			System.out.print(">> ");
			reservationIdxStr = br.readLine();
			
			int reservationIdx = Integer.parseInt(br.readLine());

			resDao.createCustomerSale(accountId, reservationIdx);
				// 변경
//				resDao.updateRestaurantSales(accountId, reservationIdx, newSales);
			
		} catch (NumberFormatException  e) {
			System.out.println("숫자 형식이 올바르지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("식당 정보가 성공적으로 변경되었습니다.");
	}

}