package com.tablepick.test.ReserveCRUDView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.model.ReserveVO;
import com.tablepick.service.TablePickSerivceCommon;

public class ReserveCRUDUnitTest {
	private AccountDao accountdao;
    private final TablePickSerivceCommon tablePickServiceCommon;

	public ReserveCRUDUnitTest() throws ClassNotFoundException {
		try {
			accountdao = new AccountDao();
			this.tablePickServiceCommon = TablePickSerivceCommon.getInstance();	
		}catch (ClassNotFoundException e){
			System.err.println("DB 드라이버 로딩 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("시스템 초기화 실패");
		}
	}

	public static void main(String[] args) {
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        new ReserveCRUDUnitTest().run(reader);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void run(BufferedReader reader) {
	    try {
	        while (true) {
	        	printReserveMenu();

	            String main = reader.readLine().trim();
	            switch (main) {
	                case "1":
	                    reserveRestaurantView(reader);
	                    break;
	                case "2":
	                    readReserveView(reader);
	                    break;
	                case "3":
	                    reserveUpdateView(reader);
	                    break;
	                case "4":
	                    reserveDeleteView(reader);
	                    break;
	                case "5":
	                	System.out.println("Customer 메인 페이지로 돌아갑니다.");
	                	return;
	                case "6":
	                    System.out.println("종료합니다.");
	                    System.exit(0);
	                    break;
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
			
			AccountVO loginData = tablePickServiceCommon.getLoginData();
			ReserveVO reserveVO = new ReserveVO(loginData.getId(), restaurantId, count, registerDate, time);
			if (accountdao.insertReserve(reserveVO)) {
				System.out.println("예약이 성공하였습니다.");
			} else {
				System.out.println("예약 등록 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 식당 예약 조회
	private void readReserveView(BufferedReader reader) throws IOException {
		try {
			System.out.println("[식당 예약 조회]");
			System.out.println("조회할 식당 이름을 입력하세요.");
			String name = reader.readLine();
			int idx = accountdao.findRestaurantIdByName(name);
			ArrayList<ReserveVO> list = accountdao.getRestaurantReserves(idx);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				System.out.println(name+" 식당에 등록된 예약");
				for (ReserveVO vo : list) {
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: " + accountdao.findRestaurantNameById(vo.getRestaurantId()) + " 인원 수: "
							+ vo.getReservePeople() + " 예약 날짜: " + vo.getReserveDate() + " 예약 시간: "
							+ vo.getReserveTime() + "시 예약한 날짜: " + formattedRegisterDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RestaurantNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 예약 update
	private void reserveUpdateView(BufferedReader reader) {
		try {
			System.out.println("등록된 예약 목록");
			AccountVO loginData = tablePickServiceCommon.getLoginData();
			ArrayList<ReserveVO> list = accountdao.getAccountReserves(loginData.getId());
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
			ReserveVO updated = new ReserveVO(old.getReserveId(), loginData.getId(), name.isEmpty() ? old.getRestaurantId() : resId,
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
		try {
			System.out.print("등록된 예약 목록");
			AccountVO loginData = tablePickServiceCommon.getLoginData();
			ArrayList<ReserveVO> list = accountdao.getAccountReserves(loginData.getId());
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
	private void printReserveMenu() {
        System.out.println("\n============================================================================================");
        System.out.println("                               *** Customer 예약 서비스 ***");
        System.out.println("============================================================================================");
        System.out.println("                                    1. 식당 예약");
        System.out.println("                                    2. 예약 확인");
        System.out.println("                                    3. 예약 변경");
        System.out.println("                                    4: 예약 삭제");
        System.out.println("                                    5. 뒤로가기");
        System.out.println("                                    6. 서비스 종료하기");
        System.out.println("============================================================================================");
        System.out.print("메뉴를 선택하세요: ");
	}
}
