package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

public class TestUpdateRestaurant {
	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			RestaurantDao resDao = new RestaurantDao();
			String accountId = "owner01";
			RestaurantVO existingRes =  resDao.checkMyRes(accountId);
			if (existingRes != null) {
				System.out.println("** 현재 식당 정보 **");
				System.out.print("식당 명 (" + existingRes.getName() + ") : ");
				String name = br.readLine();
				System.out.print("타입 명 (" + existingRes.getType() + ") : ");
				String type = br.readLine();
				System.out.print("주소 명 (" + existingRes.getAddress() + ") : ");
				String address = br.readLine();
				System.out.print("전화번호 (" + existingRes.getTel() + ") : ");
				String tel = br.readLine();
				
				// 필수 정보 입력 확인
				if (name.isBlank() || type.isBlank() || address.isBlank() || tel.isBlank()) {
					throw new InfoNotEnoughException("모든 항목을 입력하세요");
				}
				// 변경
				resDao.changeMyRes(accountId, name, type, address, tel);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("식당 정보가 성공적으로 변경되었습니다.");
		
	}
}
