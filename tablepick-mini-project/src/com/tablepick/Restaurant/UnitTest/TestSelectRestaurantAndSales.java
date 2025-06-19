package com.tablepick.Restaurant.UnitTest;

import java.util.List;

import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantDao;

import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;


public class TestSelectRestaurantAndSales {
	
	private static TestSelectRestaurantAndSales instance = new TestSelectRestaurantAndSales();
	private TestSelectRestaurantAndSales() {
	}
	public static TestSelectRestaurantAndSales getInstance() {
		return instance;
	}
	
	public void run() {
		
		// 식당 정보와 총 매출액을 조회한다.
				try {
					RestaurantDao resDao = new RestaurantDao();
					//메뉴를 생성할 시 해당 식당의 id를 받아와야 합니다.
					//1. 따라서 로그인 정보의 accountId를 받아온 후
					//2. 이 정보를 가지고 restaurantId 를 조회합니다.
					AccountVO loginData = null;

//				
//					try {
//						loginData = TablePickSerivceCommon.getInstance().getLoginData();
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					String accountId = loginData.getId();
					//세션으로 id 가져오기
					String accountId = SessionManager.getLoginDataSession().getId();

					
					
					int reservationIdx = resDao.findMyRestaurant(accountId).getRestaurantId();
		
					
					List<Map<String, String>> resList = resDao.findMyRestaurantAndSales(accountId, reservationIdx);
					
					for (int i = 0; i < resList.size(); i++) {
						Map<String, String> map = resList.get(i);
						
						String name = map.get("name");
						String type = map.get("type");
						String address = map.get("address");
						String tel = map.get("tel");
						String sales = map.get("sales");
						System.out.println("식당 명 : " + name + ", 타입 : " + type +  ", 주소 : " + address + ", 연락처 : " + tel + ", 매출액: " + sales);

					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		
	}
	public static void main(String[] args) throws NotFoundRestaurantException {
		new TestSelectRestaurantAndSales().run();
	}

}