package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.SalesVO;

public class TestUpdateSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 예약 별 매출액 수정 가능하게 하는 서비스
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			String accountId = "owner01";
			int reservationIdx = 1;
			
			List<Map<String, String>> existList = resDao.checkMyRestaurantAndSales(accountId, reservationIdx);
			String inputSales = null;
			
			if (existList != null) {
				System.out.println("** 현재 식당 정보 **");
				for (int i = 0; i < existList.size(); i++) {
					Map<String, String> map = existList.get(i);
					
					String name = map.get("name");
					String type = map.get("type");
					String address = map.get("address");
					String tel = map.get("tel");
					String sales = map.get("sales");
					System.out.println("식당 명 : " + name + ", 타입 : " + type +  ", 주소 : " + address + ", 연락처 : " + tel + ", 매출액: " + sales);

					System.out.print("매출액 (" + sales + ") : ");
					inputSales = br.readLine();
				}

				// 매출액 비어 있으면 0 처리
				int newSales = 0;
				if (!inputSales.isBlank()) {
				    try {
				        newSales = Integer.parseInt(inputSales);
				    } catch (NumberFormatException e) {
				        System.out.println("매출액은 숫자로 입력하세요. 기본값 0으로 처리됩니다.");
				    }
				}

				// 변경
				resDao.updateRestaurantSales(accountId, reservationIdx, newSales);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("식당 정보가 성공적으로 변경되었습니다.");
	}

}