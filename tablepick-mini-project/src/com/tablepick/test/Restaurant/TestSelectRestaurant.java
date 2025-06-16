package com.tablepick.test.Restaurant;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

public class TestSelectRestaurant {
	public static void main(String[] args) throws RestaurantNotFoundException {
		
		try {
			RestaurantDao resDao = new RestaurantDao();
//		ArrayList<RestaurantVO> resList = resDao.checkMyRes(accountId);
//		System.out.println("** 내 식당 조회 **");
//		for(RestaurantVO resVO : resList)
//			System.out.println(resVO);
			String accountId = "owner01";
//		String password = "own01";
			RestaurantVO res = resDao.checkMyRes(accountId);
			if (res != null) {
				System.out.println("** 내 식당 조회 **");
				System.out.println(res);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
