package com.tablepick.test.Restaurant;

import java.util.List;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.ReserveVO;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

public class TestSelectRestaurantAndSales {
	public static void main(String[] args) throws RestaurantNotFoundException {
		// 식당 정보와 총 매출액을 조회한다.
		try {
			RestaurantDao resDao = new RestaurantDao();

			String accountId = "owner01";
			int reservationIdx = 1;
			RestaurantVO res = resDao.checkMyRestaurantAndReservation(accountId, reservationIdx);
//			if (res != null) {
//				System.out.println("** 내 식당 조회 **");
//				System.out.println(res);
//			}
			
			List<ReserveVO> reservationList = resDao.checkMyRestaurantReservationList();
			
			for (ReserveVO vo : reservationList) {
				System.out.print("고객아이디 : " + vo.getAccountId() + ", ");
				System.out.print("식당 번호 : " + vo.getRestaurantId() + ", ");
				System.out.print("예약 인원 : " + vo.getReservePeople()+ ", ");
				System.out.print("예약 시간 : " + vo.getReservetime() + "시, ");
				System.out.println("예약 일 : " + vo.getReserveDate());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}