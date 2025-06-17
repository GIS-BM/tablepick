package com.tablepick.test.SearchRestaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class SearchRestaurantUnitTest {
	private AccountDao accountdao;

	public SearchRestaurantUnitTest() throws ClassNotFoundException {
		accountdao = new AccountDao();
	}

	public static void main(String[] args) {
		try {
			SearchRestaurantUnitTest search = new SearchRestaurantUnitTest();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println("\n=== Restaurant 조회 서비스 ===");
				System.out.println("1. 식당 전체 조회");
				System.out.println("2. 식당 타입별 조회");
				System.out.println("3. 해당 식당 리뷰 조회");
				System.out.println("4. 식당 예약 시간 조회");
				System.out.println("5. 평균 별점 높은순 식당 조회");
				System.out.println("exit: 종료");
				System.out.print("입력 : ");
				
				String main = reader.readLine().trim();
				switch (main) {
					case "1":
						search.searchAllRestaurantView();
						break;
					case "2":
						search.searchRestaurantByTypeView(reader);
						break;
					case "3":
						search.searchRestaurantReviewView(reader);
						break;
					case "4":
						search.searchRestaurantReserveTimeView(reader);
						break;
					case "5":
						search.searchRestaurantByStar(reader);
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

	private void searchAllRestaurantView() {
		try {
			System.out.println("[식당 전체 조회]");
			ArrayList<RestaurantVO> list = accountdao.searchAllRestaurants();
			if (list.isEmpty()) {
				System.out.println("등록된 식당이 없습니다.");
			} else {
				for (RestaurantVO vo : list) {
					System.out.println(vo.getName()+ " 주소: "+vo.getAddress()+" 전화번호: "+vo.getTel()+" 오픈 시간: "+vo.getOpenTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void searchRestaurantByTypeView(BufferedReader reader) {
		try {
			System.out.println("조회할 식당 타입을 선택하세요");
			System.out.println(" 1. 한식 2. 중식 3. 일식 4. 양식");
			int choice = Integer.parseInt(reader.readLine());
			String type = null;
			switch (choice) {
				case 1:
					type = "한식";
					break;
				case 2:
					type = "중식";
					break;
				case 3:
					type = "일식";
					break;
				case 4:
					type = "양식";
					break;
				default:
					System.out.println("잘못된 입력입니다.");		
			}
			ArrayList<RestaurantVO> list = accountdao.searchRestaurantByType(type);
			if (list.isEmpty()) {
				System.out.println("선택한 타입의 식당이 없습니다.");
			} else {
				for (RestaurantVO vo : list) {
					System.out.println(vo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchRestaurantReviewView(BufferedReader reader) throws IOException {
		try {
			System.out.println("리뷰를 조회할 식당을 선택하세요 ");
			ArrayList<RestaurantVO> list = accountdao.searchAllRestaurants();
			if (list.isEmpty()) {
				System.out.println("등록된 식당이 없습니다.");
			} else {
				for (int i=0;i<list.size();i++) {
					RestaurantVO vo = list.get(i);
					System.out.println(i+1+": "+vo.getName()+ " 주소: "+vo.getAddress()+" 전화번호: "+vo.getTel()+" 오픈 시간: "+vo.getOpenTime());
				}
			}
			int choice = Integer.parseInt(reader.readLine());
			RestaurantVO vo = list.get(choice-1);
			int restaurantId = vo.getRestaurantId();
			ArrayList<ReviewVO> reviewList = accountdao.searchRestaurantReviewView(restaurantId);
			if (reviewList.isEmpty()) {
				System.out.println("등록된 리뷰가 없습니다.");
			} else {
				for (ReviewVO rvo : reviewList) {
					System.out.println("식당명: "+accountdao.findRestaurantNameById(rvo.getRestaurantId())+" 별점: "+rvo.getStar()+"점 "
							+" 내용: "+rvo.getComment()+" 등록일: "+rvo.getRegisterDate());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchRestaurantReserveTimeView(BufferedReader reader) {
		try {
			System.out.print("수정할 계정 ID: ");
			String id = reader.readLine();
			AccountVO old = accountdao.findAccountById(id);
			if (old == null) {
				System.out.println("해당 ID의 계정이 존재하지 않습니다.");
				return;
			}

			System.out.print("새 Type (기존: " + old.getType() + "): ");
			String type = reader.readLine();
			System.out.print("새 Name (기존: " + old.getName() + "): ");
			String name = reader.readLine();
			System.out.print("새 Password (기존: " + old.getPassword() + "): ");
			String password = reader.readLine();
			System.out.print("새 Tel (기존: " + old.getTel() + "): ");
			String tel = reader.readLine();

			AccountVO updated = new AccountVO(id,
					type.isEmpty() ? old.getType() : type,
					name.isEmpty() ? old.getName() : name,
					password.isEmpty() ? old.getPassword() : password,
					tel.isEmpty() ? old.getTel() : tel);

			if (accountdao.updateAccount(updated)) {
				System.out.println("계정이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchRestaurantByStar(BufferedReader reader) {
		try {
			System.out.println("[별점 높은순 조회]");
			ArrayList<RestaurantVO> list = accountdao.searchRestaurantByStar();
			if (list.isEmpty()) {
				System.out.println("등록된 식당이 없습니다.");
			} else {
				for (RestaurantVO vo : list) {
					System.out.println(vo.getName()+ " 주소: "+vo.getAddress()+" 전화번호: "+vo.getTel()+" 오픈 시간: "+vo.getOpenTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
