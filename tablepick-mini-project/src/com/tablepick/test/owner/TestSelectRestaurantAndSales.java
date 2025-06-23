package com.tablepick.test.owner;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;

import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;


public class TestSelectRestaurantAndSales {
	
	private static TestSelectRestaurantAndSales instance = new TestSelectRestaurantAndSales();
	private TestSelectRestaurantAndSales() {
	}
	public static TestSelectRestaurantAndSales getInstance() {
		return instance;
	}
	
	public void run() {
		/*
		 * 식당 정보와 총 매출액을 조회하는 클래스
		 */
		
		try {
			OwnerService service = new OwnerService();
			String accountId = SessionManager.getLoginDataSession().getId();
			
			int reservationIdx = service.findMyRestaurant(accountId).getRestaurantId();

			List<Map<String, String>> resList = service.findMyRestaurantAndSales(accountId, reservationIdx);
			
			for (int i = 0; i < resList.size(); i++) {
				
				Map<String, String> map = resList.get(i);
				
				String name = map.get("name");
				String type = map.get("type");
				String address = map.get("address");
				String tel = map.get("tel");
				String sales = map.get("sales");
				String time = map.get("opentime");
				String openTime = time.substring(0, 5);
				System.out.println("식당 명 : " + name + ", 타입 : " + type +  ", 주소 : " + address + ", 연락처 : " + tel + ", 오픈 시간 : "+ openTime+", 총 매출액: " + sales);

			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	public static void main(String[] args) throws NotFoundRestaurantException {
		new TestSelectRestaurantAndSales().run();
	}

}