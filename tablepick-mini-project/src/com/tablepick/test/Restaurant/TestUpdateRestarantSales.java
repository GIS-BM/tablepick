package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.TotalSalesVO;

public class TestUpdateRestarantSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 식당 정보와 총 매출액을 조회한다.
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			
			String accountId = "owner01";
			int restaurantIdx = 1;
			RestaurantVO existingRes = resDao.checkMyRestaurantAndReservation(accountId, restaurantIdx);
			if (existingRes != null) {
				System.out.println("** 현재 식당 정보 **");
				System.out.println(existingRes);
				System.out.print("매출액 입력 (" + existingRes.getTotalSalesVO().getSales() + ") : ");
				String totalSalesStr = br.readLine();
				
				// 필수 정보 입력 확인
				if (totalSalesStr.isBlank()) {
					throw new InfoNotEnoughException("모든 항목을 입력하세요");
				}
				int totalSales = Integer.parseInt(totalSalesStr);
				
				// 변경
				resDao.updateRestaurantSales(accountId, totalSales);
			}
			System.out.println("식당 매출액이 입력되었습니다.");
		} catch (NumberFormatException e) {
			System.err.println("숫자를 올바르게 입력하세요.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}