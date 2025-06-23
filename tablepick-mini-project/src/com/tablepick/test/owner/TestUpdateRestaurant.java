package com.tablepick.test.owner;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.OwnerDao;
import com.tablepick.model.RestaurantVO;

import com.tablepick.session.SessionManager;

import com.tablepick.service.CommonService;
import com.tablepick.service.OwnerService;

public class TestUpdateRestaurant {

	private static TestUpdateRestaurant instance = new TestUpdateRestaurant();

	private TestUpdateRestaurant() {
	}

	public static TestUpdateRestaurant getInstance() {
		return instance;
	}

	public void run() {
		/*
		 * 식당의 정보를 변경하는 클래스
		 */
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			OwnerService service = new OwnerService();

			String accountId = SessionManager.getLoginDataSession().getId();

			int reservationIdx = service.findMyRestaurant(accountId).getRestaurantId();

			List<Map<String, String>> existList = service.findMyRestaurantAndSales(accountId, reservationIdx);
			String newName = null;
			String newType = null;
			String newAddress = null;
			String newTel = null;
			String newTime = null;
			LocalTime time = null;

			if (existList != null) {
				System.out.println("                          *** 식당의 상세 정보를 변경합니다. ***");
				for (int i = 0; i < existList.size(); i++) {
					Map<String, String> map = existList.get(i);

					String name = map.get("name");
					String type = map.get("type");
					String address = map.get("address");
					String tel = map.get("tel");
					String sales = map.get("sales");
					String beforetime = map.get("opentime");

					System.out.print("식당 명 (현재 : " + name + ") : ");
					newName = br.readLine();
					System.out.print("타입 명 (현재 : " + type + ", 가능한 식당 타입 : 한식, 중식, 일식, 양식, 카페/베이커리, 해산물, 주점, 기타) : ");

					newType = br.readLine();
					System.out.print("주소 명 (현재 : " + address + ") : ");
					newAddress = br.readLine();
					System.out.print("전화번호 (현재 : " + tel + ") : ");
					newTel = br.readLine();

					while (true) {
						try {
							beforetime = beforetime.substring(0, 5);
							System.out.print("오픈 시간 (현재 : " + beforetime + ") : ");
							newTime = br.readLine();
							time = LocalTime.parse(newTime, DateTimeFormatter.ofPattern("HH:mm"));

							break;
						} catch (DateTimeParseException e) {
							System.out.println("잘못된 시간 형식입니다. 예시처럼 HH:mm 형식으로 입력해주세요.");
						}
					}

				}

				// 필수 정보 입력 확인
				if (newName.isBlank() || newType.isBlank() || newAddress.isBlank() || newTel.isBlank() || newTime.isBlank()) {
					throw new InfoNotEnoughException("모든 항목을 입력하세요");
				}

				// 변경
				service.updateMyRestaurantInfo(accountId, reservationIdx, newName, newType, newAddress, newTel, time);
				System.out.println("식당 정보가 성공적으로 변경되었습니다.");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		new TestUpdateRestaurant().run();

	}
}
