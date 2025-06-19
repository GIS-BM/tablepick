package com.tablepick.Restaurant.UnitTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;
import com.tablepick.service.CommonService;

public class TestUpdateRestaurantAndSales {
	
	private static TestUpdateRestaurantAndSales instance = new TestUpdateRestaurantAndSales();
	private TestUpdateRestaurantAndSales() {
	}
	public static TestUpdateRestaurantAndSales getInstance() {
		return instance;
	}
	
	
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			
			//메뉴를 생성할 시 해당 식당의 id를 받아와야 합니다.
			//1. 따라서 로그인 정보의 accountId를 받아온 후
			//2. 이 정보를 가지고 restaurantId 를 조회합니다.
			AccountVO loginData = null;
		
			try {
				loginData = CommonService.getInstance().getLoginData();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String accountId = loginData.getId();
			
			int reservationIdx = resDao.findMyRestaurant(accountId).getRestaurantId();
			
			//System.out.println(reservationIdx);
			
			List<Map<String, String>> existList = resDao.findMyRestaurantAndSales(accountId, reservationIdx);
			String newName = null;
			String newType = null;
			String newAddress = null;
			String newTel = null;
			String inputSales = null;
			
			if (existList != null) {
				System.out.println("                          *** 식당의 상세 정보를 변경합니다. ***");
				for (int i = 0; i < existList.size(); i++) {
					Map<String, String> map = existList.get(i);
					
					String name = map.get("name");
					String type = map.get("type");
					String address = map.get("address");
					String tel = map.get("tel");
					String sales = map.get("sales");
//					System.out.println("식당 명 : " + name + ", 주소 : " + address + ", 연락처 : " + tel + ", 매출액: " + sales);

					System.out.print("식당 명 (현재 : " + name + ") : ");
					newName = br.readLine();
					System.out.print("타입 명 (현재 : " + type + ") : ");
					newType = br.readLine();
					System.out.print("주소 명 (현재 : " + address + ") : ");
					newAddress = br.readLine();
					System.out.print("전화번호 (현재 : " + tel + ") : ");
					newTel = br.readLine();
					
				}
				
				// 필수 정보 입력 확인
				if (newName.isBlank() || newType.isBlank() || newAddress.isBlank() || newTel.isBlank()) {
					throw new InfoNotEnoughException("모든 항목을 입력하세요");
				}
			
				// 변경
				resDao.updateMyRestaurantInfoAndSales(accountId, reservationIdx, newName, newType, newAddress, newTel);
				System.out.println("식당 정보가 성공적으로 변경되었습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	public static void main(String[] args) {
		new TestUpdateRestaurantAndSales().run();
		
	}
}
