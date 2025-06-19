package com.tablepick.test.AdminUnitTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.AdminDao;
import com.tablepick.model.ReserveVO;

public class AdminCRUDUnitTest {
	private AdminDao admindao;

	public AdminCRUDUnitTest() throws ClassNotFoundException {
		admindao = new AdminDao();
	}

	public static void main(String[] args) throws ClassNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		new AdminCRUDUnitTest().run(reader);	
		}
	public void run(BufferedReader reader) {
		try {
			while (true) {
				printAdminMenu();

				String main = reader.readLine().trim();
				switch (main) {
				case "1":
					searchAllAccountView();
					break;
				case "2":
					searchAccountView(reader);
					break;
				case "3":
					searchAllReserveView();
					break;
				case "4":
					searchMostReserveView();
					break;
				case "5":
                	System.out.println("로그아웃합니다.");
                    return;
				case "6":
					System.out.println("종료합니다.");
					System.exit(0);
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void searchAllAccountView() {
		try {
			System.out.println("[전체 계정 조회]");
			ArrayList<AccountVO> list = admindao.getAllAccounts();
			if (list.isEmpty()) {
				System.out.println("등록된 계정이 없습니다.");
			} else {
				for (AccountVO vo : list) {
					System.out.println("아이디: " + vo.getId() + " 유형: " + vo.getType() + " 이름: " + vo.getName()
							+ " 비밀번호: " + vo.getPassword() + " 전화번호: " + vo.getTel());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchAccountView(BufferedReader reader) {
		try {
			System.out.println("[회원 정보 관리 시스템]");
			System.out.print("조회할 ID 입력: ");
			String id = reader.readLine();
			AccountVO vo = admindao.findAccountById(id);
			if (vo != null) {
				System.out.println("조회 결과: 아이디: " + vo.getId() + " 유형: " + vo.getType() + " 이름: " + vo.getName()
						+ " 비밀번호: " + vo.getPassword() + " 전화번호: " + vo.getTel());

				while (true) {
					System.out.println("\n해당 아이디를 어떻게 처리하겠습니까?");
					System.out.println("1. 변경 2. 삭제 3. 뒤로가기 4. 관리자 페이지");
					String choice = reader.readLine().trim();
					switch (choice) {
					case "1":
						updateAccountView(vo, reader);
						return;
					case "2":
						deleteAccountView(vo, reader);
						return;
					case "3":
						System.out.println("회원 정보 관리 시스템으로 돌아갑니다.\n");
						searchAccountView(reader);
						break;
					case "4":
						System.out.println("관리자 페이지로 돌아갑니다.");
						return;
					default:
						System.out.println("잘못된 입력입니다.");
					}
				}
			} else {
				System.out.println("해당 ID의 계정이 존재하지 않습니다.");
			}
		}catch (AccountNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateAccountView(AccountVO vo, BufferedReader reader) {
		try {
			AccountVO old = vo;
			if (old == null) {
				System.out.println("해당 ID의 계정이 존재하지 않습니다.");
				return;
			}

			System.out.print("새 유형 (기존: " + old.getType() + "): ");
			String type = reader.readLine();
			System.out.print("새 이름 (기존: " + old.getName() + "): ");
			String name = reader.readLine();
			System.out.print("새 비밀번호 (기존: " + old.getPassword() + "): ");
			String password = reader.readLine();
			System.out.print("새 전화번호 (기존: " + old.getTel() + "): ");
			String tel = reader.readLine();

			AccountVO updated = new AccountVO(vo.getId(), type.isEmpty() ? old.getType() : type,
					name.isEmpty() ? old.getName() : name, password.isEmpty() ? old.getPassword() : password,
					tel.isEmpty() ? old.getTel() : tel);

			if (admindao.updateAccount(updated)) {
				System.out.println("\n계정이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteAccountView(AccountVO vo, BufferedReader reader) {
		try {
			while (true) {
				System.out.print(vo.getId() + " 계정을 삭제하시겠습니까?");
				System.out.println("1. 예 2. 아니오 3. 뒤로가기 4. 관리자 페이지");
				String choice = reader.readLine().trim();
				switch (choice) {
				case "1":
					if (admindao.deleteAccount(vo.getId())) {
						System.out.println("계정이 성공적으로 삭제되었습니다.");
					} else {
						System.out.println("삭제 실패 또는 존재하지 않는 ID");
					}
					return;
				case "2":
					System.out.println("계정을 삭제하지 않았습니다.");
					return;
				case "3":
					System.out.println("회원 정보 관리 시스템으로 돌아갑니다.\n");
					searchAccountView(reader);
					break;
				case "4":
					System.out.println("관리자 페이지로 돌아갑니다.");
					return;
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchAllReserveView() {
		try {
			System.out.println("[전체 예약 조회]");
			ArrayList<ReserveVO> list = admindao.getAllReserves();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (ReserveVO vo : list) {
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: " + admindao.findRestaurantNameById(vo.getRestaurantId()) + " 인원 수: "
							+ vo.getReservePeople() + " 예약 날짜: " + vo.getReserveDate() + " 예약 시간: "
							+ vo.getReserveTime() + "시 예약한 날짜: " + formattedRegisterDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchMostReserveView() {
		try {
			System.out.println("[최대 예약자 조회]");
			Map<String, Integer> map = admindao.getMostReservesAccount();
			if (map.isEmpty()) {
				System.out.println("예약한 사람이 없습니다.");
			} else {
				for(String key : map.keySet()) {
					int value = map.get(key);
					System.out.println("ID: "+key+" 예약 수: "+value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void printAdminMenu() {
        System.out.println("\n============================================================================================");
        System.out.println("                    /$$$$$$  /$$$$$$$  /$$      /$$ /$$$$$$ /$$   /$$");
        System.out.println("                   /$$__  $$| $$__  $$| $$$    /$$$|_  $$_/| $$$ | $$");
        System.out.println("                  | $$  \\ $$| $$  \\ $$| $$$$  /$$$$  | $$  | $$$$| $$");
        System.out.println("                  | $$$$$$$$| $$  | $$| $$ $$/$$ $$  | $$  | $$ $$ $$");
        System.out.println("                  | $$__  $$| $$  | $$| $$  $$$| $$  | $$  | $$  $$$$");
        System.out.println("                  | $$  | $$| $$  | $$| $$\\  $ | $$  | $$  | $$\\  $$$");
        System.out.println("                  | $$  | $$| $$$$$$$/| $$ \\/  | $$ /$$$$$$| $$ \\  $$");
        System.out.println("                  |__/  |__/|_______/ |__/     |__/|______/|__/  \\__/");
        System.out.println("============================================================================================");
        System.out.println("                             *** Admin 메인 서비스 ***");
        System.out.println("============================================================================================");
        System.out.println("                                1. 전체 회원 조회");
        System.out.println("                                2. 회원 정보 검색");
        System.out.println("                                3. 전체 예약 목록");
        System.out.println("                                4. 최대 예약자 조회");
        System.out.println("                                5. 로그아웃");
        System.out.println("                                6. 서비스 종료하기");
        System.out.println("============================================================================================");
        System.out.print("메뉴를 선택하세요: ");
	}
}
