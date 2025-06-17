package com.tablepick.test.ReserveCRUDView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.tablepick.model.AccountDao;
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

	// 식당 예약
	private void reserveRestaurantView(BufferedReader reader) {
		String id = "cust01";
		try {
			System.out.println("[식당 예약]");
			System.out.print("식당명: ");
			String name = reader.readLine();
			System.out.print("예약 인원: ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("예약 날짜 ( 예)2025-06-12 ) : ");
			String date = reader.readLine();
			System.out.println("예약 시간:");
			int time = Integer.parseInt(reader.readLine());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate registerDate = LocalDate.parse(date, formatter);

			int restaurantId = accountdao.findRestaurantIdByName(name);

			if (restaurantId == -1) {
				System.out.println("해당 식당이 존재하지 않습니다.");
				return;
			}
			ReserveVO reserveVO = new ReserveVO(id, restaurantId, count, registerDate, time);
			if (accountdao.insertReserve(reserveVO)) {
				System.out.println("예약이 성공적으로 등록되었습니다.");
			} else {
				System.out.println("예약 등록 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 모든 예약 조회
	private void readReserveView(BufferedReader reader) {
		String id = "cust01";
		try {
			System.out.println("[전체 예약 조회]");
			ArrayList<ReserveVO> list = accountdao.getAllReserves(id);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (ReserveVO vo : list) {
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: " + accountdao.findRestaurantNameById(vo.getRestaurantId()) + " 인원 수: "
							+ vo.getReservePeople() + " 예약 날짜: " + vo.getReserveDate() + " 예약 시간: "
							+ vo.getReserveTime() + "시 예약한 날짜: " + formattedRegisterDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 예약 update
	private void reserveUpdateView(BufferedReader reader) {
		String id = "cust01";
		try {
			System.out.println("등록된 예약 목록");
			ArrayList<ReserveVO> list = accountdao.getAllReserves(id);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
					ReserveVO vo = list.get(i);
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println((i + 1) + ". 식당명: " + accountdao.findRestaurantNameById(vo.getRestaurantId())
							+ " 인원 수: " + vo.getReservePeople() + " 예약 날짜: " + vo.getReserveDate() + " 예약 시간: "
							+ vo.getReserveTime() + "시 예약한 날짜: " + formattedRegisterDate);
				}
			}
			System.out.print("수정할 예약: ");
			int choice = Integer.parseInt(reader.readLine());
			if (choice < 1 || choice > list.size()) {
				System.out.println("잘못된 선택입니다.");
				return;
			}
			ReserveVO old = list.get(choice - 1);

			System.out.print("식당명 (기존: " + accountdao.findRestaurantNameById(old.getRestaurantId()) + "): ");
			String name = reader.readLine();
			System.out.print("예약 인원 (기존: " + old.getReservePeople() + "): ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("예약 날짜 ( 예)2025-06-12 ) (기존: " + old.getReserveDate() + "): ");
			String date = reader.readLine();
			System.out.print("예약 시간 (기존: " + old.getReserveTime() + "): ");
			int time = Integer.parseInt(reader.readLine());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate reserveDate = LocalDate.parse(date, formatter);
			int resId = accountdao.findRestaurantIdByName(name);
			ReserveVO updated = new ReserveVO(old.getReserveId(), id, name.isEmpty() ? old.getRestaurantId() : resId,
					count == 0 ? old.getReservePeople() : count, date.isEmpty() ? old.getReserveDate() : reserveDate,
					time == 0 ? old.getReserveTime() : time);

			if (accountdao.updateReserve(updated)) {
				System.out.println("예약이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 예약 delete
	private void reserveDeleteView(BufferedReader reader) {
		String id = "cust01";
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
