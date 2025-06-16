package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

public class TestUpdateRestarantSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 식당 정보와 총 매출액을 조회한다.
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			
			String accountId = "owner01";
//		String password = "own01";
			int reservationIdx = 1;
			RestaurantVO existingRes = resDao.checkMyRestaurantAndReservation(accountId, reservationIdx);
			if (existingRes != null) {
				System.out.println("** 현재 식당 정보 **");
				System.out.print(existingRes);
				System.out.print("전화번호 (" + existingRes.getTotal() + ") : ");
				String total = br.readLine();
				
				// 필수 정보 입력 확인
				if (total.isBlank()) {
					throw new InfoNotEnoughException("모든 항목을 입력하세요");
				}
				// 변경
				resDao.updateRestaurantSales(total);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
