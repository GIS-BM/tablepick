package com.tablepick.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.AdminDao;
import com.tablepick.model.ReserveVO;


public class AdminService {
	private AdminDao adminDao;

	private static AdminService instance = new AdminService();

	public AdminService() {
		try {
			adminDao = new AdminDao();
		} catch (ClassNotFoundException e) {

		}
	}

	public static AdminService getInstance() {
		return instance;
	}
	// 전체 계정 유효성 검사 (예외 발생 유도)
	public void findAccounts() throws SQLException, NotFoundAccountException {
		adminDao.findAccounts();  // 내부에서 NotFoundAccountException 발생 가능
	}

	// 전체 계정 조회
	public List<AccountVO> getAllAccounts() throws SQLException, NotFoundAccountException {
		findAccounts();
		return adminDao.findAllAccounts();
	}

	// ID로 계정 조회
	public AccountVO getAccountById(String accountId) throws SQLException, NotFoundAccountException {
		return adminDao.findAccount(accountId);
	}

	// 계정 수정
	public boolean updateAccount(AccountVO accountVO) throws SQLException, InfoNotEnoughException {
		return adminDao.updateAccount(accountVO);
	}

	// 계정 삭제
	public boolean deleteAccount(String accountId) throws SQLException, InfoNotEnoughException {
		return adminDao.deleteAccount(accountId);
	}

	// 레스토랑 ID로 이름 조회
	public String getRestaurantNameById(int restaurantId) throws SQLException, NotFoundRestaurantException {
		return adminDao.findRestaurantNameById(restaurantId);
	}

	// 전체 예약 조회
	public List<ReserveVO> getAllReserves() throws SQLException {
		return adminDao.getAllReserves();
	}

	// 예약 최다 계정 조회
	public Map<String, Integer> getTopReserveAccounts() throws SQLException {
		return adminDao.getMostReservesAccount();
	}
	public void searchAllAccount() {
		try {
			System.out.println("\n[전체 계정 조회]");
			ArrayList<AccountVO> list = adminDao.findAllAccounts();
			if (list.isEmpty()) {
				System.out.println("등록된 계정이 없습니다.");
			} else {
				for (AccountVO vo : list) {
					System.out.println("아이디: " + vo.getId() + " 유형: " + vo.getType() + " 이름: " + vo.getName()
							+ " 비밀번호: " + vo.getPassword() + " 전화번호: " + vo.getTel());
				}
			}
		} catch (NotFoundAccountException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void searchAccount(BufferedReader reader) {
		try {
			System.out.println("\n[회원 정보 관리 시스템]");
			System.out.println("1. 회원 조회 2. 뒤로가기");
			String num = reader.readLine().trim();
			if (num.equals("1")) {
				System.out.print("조회할 ID 입력: ");
				String id = reader.readLine();
				AccountVO vo = adminDao.findAccount(id);
				if (vo != null) {
					System.out.println("조회 결과: 아이디: " + vo.getId() + " 유형: " + vo.getType() + " 이름: " + vo.getName()
							+ " 비밀번호: " + vo.getPassword() + " 전화번호: " + vo.getTel());
					while (true) {
						System.out.println("\n해당 아이디를 어떻게 처리하겠습니까?");
						System.out.println("1. 변경 2. 삭제 3. 뒤로가기 4. 관리자 페이지");
						String choice = reader.readLine().trim();
						switch (choice) {
						case "1":
							updateAccount(vo, reader);
							return;
						case "2":
							deleteAccount(vo, reader);
							return;
						case "3":
							System.out.println("회원 정보 관리 시스템으로 돌아갑니다.\n");
							searchAccount(reader);
							break;
						case "4":
							System.out.println("관리자 페이지로 돌아갑니다.");
							return;
						default:
							System.out.println("잘못된 입력입니다.");
						}
					}

				}
			}else if(num.equals("2")) {
				System.out.println("관리자 페이지로 돌아갑니다.");
				return;
			}else {
				System.out.println("잘못된 입력입니다.");
				searchAccount(reader);
			}
		} catch (NotFoundAccountException e) {
			System.out.println(e.getMessage());
			searchAccount(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateAccount(AccountVO vo, BufferedReader reader){
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

			if (adminDao.updateAccount(updated)) {
				System.out.println("\n계정이 성공적으로 수정되었습니다.");
			} else {
				throw new NotFoundAccountException("계정 수정이 실패하였습니다.");
			}
		} catch (InfoNotEnoughException e) {
			System.out.println(e.getMessage());
		}catch (NotFoundAccountException e) {
			System.out.println(e.getMessage());
		}catch (SQLException e) {
			System.out.println("\n계정 수정이 실패하였습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void deleteAccount(AccountVO vo, BufferedReader reader) {
		try {
			while (true) {
				System.out.print(vo.getId() + " 계정을 삭제하시겠습니까?");
				System.out.println("1. 예 2. 아니오 3. 뒤로가기 4. 관리자 페이지");
				String choice = reader.readLine().trim();
				switch (choice) {
				case "1":
					if (adminDao.deleteAccount(vo.getId())) {
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
					searchAccount(reader);
					break;
				case "4":
					System.out.println("관리자 페이지로 돌아갑니다.");
					return;
				default:
					System.out.println("잘못된 입력입니다.");
				}
			}

		} catch (InfoNotEnoughException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void searchAllReserve() {
		try {
			System.out.println("\n[전체 예약 조회]");
			ArrayList<ReserveVO> list = adminDao.getAllReserves();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (ReserveVO vo : list) {
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: " + adminDao.findRestaurantNameById(vo.getRestaurantId()) + " 인원 수: "
							+ vo.getReservePeople() + " 예약 날짜: " + vo.getReserveDate() + " 예약 시간: "
							+ vo.getReserveTime() + "시 예약한 날짜: " + formattedRegisterDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		}
	}

	public void searchMostReserve() {
		try {
			System.out.println("\n[최대 예약자 조회]");
			Map<String, Integer> map = adminDao.getMostReservesAccount();
			if (map.isEmpty()) {
				System.out.println("예약한 사람이 없습니다.");
			} else {
				for (String key : map.keySet()) {
					int value = map.get(key);
					System.out.println("ID: " + key + " 예약 수: " + value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
