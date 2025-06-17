package com.tablepick.test.ReserveCRUDView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.model.ReserveVO;

public class ReserveCRUDUnitTest {
	private AccountDao accountdao;

	public ReserveCRUDUnitTest() throws ClassNotFoundException {
		accountdao = new AccountDao();
	}

	public static void main(String[] args) {
		try {
			ReserveCRUDUnitTest crud = new ReserveCRUDUnitTest();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println("\n=== Reserve 예약 메뉴 ===");
				System.out.println("c: 식당 예약");
				System.out.println("r: 예약 확인");
				System.out.println("u: 예약 변경");
				System.out.println("d: 예약 삭제");
				System.out.println("exit: 종료");
				System.out.print("입력 : ");
				
				String main = reader.readLine().trim();
				switch (main) {
					case "c":
						crud.reserveRestaurantView(reader);
						break;
					case "r":
						crud.readReserveView(reader);
						break;
					case "u":
						crud.reserveUpdateView(reader);
						break;
					case "d":
						crud.reserveDeleteView(reader);
						break;
					case "exit":
						System.out.println("종료합니다.");
						return;
					default:
						System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reserveRestaurantView(BufferedReader reader) {
		String id = "user01";
		try {
			System.out.println("[식당 예약]");
			System.out.print("식당명: ");
			String name = reader.readLine();
			System.out.print("예약 인원: ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("예약 날짜 ( 예)2025-06-12 12:40 ): ");
			String date = reader.readLine();
			Long sale = 0L;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime registerDate = LocalDateTime.parse(date, formatter);
			
			int restaurantId = accountdao.findRestaurantIdByName(name);
			
	        if (restaurantId == -1) {
	            System.out.println("해당 식당이 존재하지 않습니다.");
	            return;
	        }
			ReserveVO reserveVO = new ReserveVO(id, restaurantId, count, registerDate, sale);
			if (accountdao.insertReserve(reserveVO)) {
				System.out.println("예약이 성공적으로 등록되었습니다.");
			} else {
				System.out.println("예약 등록 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readReserveView(BufferedReader reader) {
		String id = "user01";
		try {
			System.out.println("[전체 예약 조회]");
			ArrayList<ReserveVO> list = accountdao.getAllReserves(id);
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (ReserveVO vo : list) {
					System.out.println(vo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void reserveUpdateView(BufferedReader reader) {
		String id = "user01";
		try {
			System.out.print("등록된 예약 목록");
			ArrayList<ReserveVO> list = accountdao.getAllReserves(id);
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
			        System.out.println((i + 1) + ". " + list.get(i)); 
			    }
			}
			System.out.print("수정할 예약: ");
			int choice = Integer.parseInt(reader.readLine());
			if (choice < 1 || choice > list.size()) {
		        System.out.println("잘못된 선택입니다.");
		        return;
		    }
			ReserveVO old = list.get(choice - 1);
			
			System.out.print("새 Restaurant (기존: " + accountdao.findRestaurantNameById(old.getRestaurantId()) + "): ");
			String name = reader.readLine();
			System.out.print("새 ReserveCount (기존: " + old.getReserveCount() + "): ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("새 ReserveDate ( 예)2025-06-12 12:40 ) (기존: " + old.getReserveDate() + "): ");
			String date = reader.readLine();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime reserveDate = LocalDateTime.parse(date, formatter);
			int resId= accountdao.findRestaurantIdByName(name);
			Long sale = 0L;
			ReserveVO updated = new ReserveVO(old.getReserveId(), id,
					name.isEmpty() ? old.getRestaurantId() : resId,
					count==0 ? old.getReserveCount() : count,
					date.isEmpty() ? old.getReserveDate() : reserveDate, sale);

			if (accountdao.updateReserve(updated)) {
				System.out.println("예약이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reserveDeleteView(BufferedReader reader) {
		String id = "user01";
		try {
			System.out.print("등록된 예약 목록");
			ArrayList<ReserveVO> list = accountdao.getAllReserves(id);
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
			        System.out.println((i + 1) + ". " + list.get(i)); 
			    }
			}
			System.out.print("삭제할 예약: ");
			int choice = Integer.parseInt(reader.readLine());
			if (choice < 1 || choice > list.size()) {
		        System.out.println("잘못된 선택입니다.");
		        return;
		    }
			ReserveVO old = list.get(choice - 1);
			if (accountdao.deleteReserve(old)) {
				System.out.println("예약이 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("삭제 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
