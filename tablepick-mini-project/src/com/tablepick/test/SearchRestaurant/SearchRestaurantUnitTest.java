package com.tablepick.test.SearchRestaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import com.tablepick.exception.RestaurantNotFoundException;
import com.tablepick.model.AccountDao;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class SearchRestaurantUnitTest {
	private AccountDao accountdao;

	public SearchRestaurantUnitTest() throws ClassNotFoundException {
		accountdao = new AccountDao();
	}

	public static void main(String[] args) throws ClassNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		new SearchRestaurantUnitTest().run(reader);
	}

	public void run(BufferedReader reader) {
		try {
			while (true) {
				printSearchRestaurantMenu();
				String main = reader.readLine().trim();
				switch (main) {
				case "1":
					searchAllRestaurantView();
					break;
				case "2":
					searchRestaurantByTypeView(reader);
					break;
				case "3":
					searchRestaurantReviewView(reader);
					break;
				case "4":
					searchRestaurantByStar(reader);
					break;
				case "5":
					System.out.println("Customer 메인 페이지로 돌아갑니다.");
                	return;
				case "exit":
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

	private void searchAllRestaurantView() {
		try {
			System.out.println("[식당 전체 조회]");
			ArrayList<RestaurantVO> list = accountdao.searchAllRestaurants();
			if (list.isEmpty()) {
				System.out.println("등록된 식당이 없습니다.");
			} else {
				for (RestaurantVO vo : list) {
					System.out.println(vo.getName() + " 주소: " + vo.getAddress() + " 전화번호: " + vo.getTel() + " 오픈 시간: "
							+ vo.getOpenTime());
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
					System.out.println(vo.getName() + " 주소: " + vo.getAddress() + " 전화번호: " + vo.getTel() + " 오픈 시간: "
							+ vo.getOpenTime());
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
				for (int i = 0; i < list.size(); i++) {
					RestaurantVO vo = list.get(i);
					System.out.println(i + 1 + ": " + vo.getName() + " 주소: " + vo.getAddress() + " 전화번호: " + vo.getTel()
							+ " 오픈 시간: " + vo.getOpenTime());
				}
			}
			int choice = Integer.parseInt(reader.readLine());
			RestaurantVO vo = list.get(choice - 1);
			int restaurantId = vo.getRestaurantId();
			ArrayList<ReviewVO> reviewList = accountdao.searchRestaurantReviewView(restaurantId);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (reviewList.isEmpty()) {
				System.out.println("등록된 리뷰가 없습니다.");
			} else {
				for (ReviewVO rvo : reviewList) {
					String formattedRegisterDate = rvo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: "
							+ accountdao.findRestaurantNameById(vo.getRestaurantId())
							+ " 별점: " + rvo.getStar() + "점 " + " 내용: " + rvo.getComment() + " 등록일: "
							+ formattedRegisterDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RestaurantNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void searchRestaurantByStar(BufferedReader reader) {
		try {
			System.out.println("[별점 높은순 조회]");
			Map<RestaurantVO, double[]> map = accountdao.searchRestaurantByStar();
			if (map.isEmpty()) {
				System.out.println("등록된 식당이 없습니다.");
			} else {
				for (RestaurantVO vo : map.keySet()) {
					double[] values = map.get(vo);
					double avgStar = values[0];
					int count = (int)values[1];
					System.out.println(vo.getName() + " 주소: " + vo.getAddress() + " 전화번호: " + vo.getTel() + " 오픈 시간: "
							+ vo.getOpenTime() + " 평균 별점: " + avgStar + " 예약자 수: "+count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void printSearchRestaurantMenu() {
        System.out.println("\n============================================================================================");
        System.out.println("                               *** Customer 조회 서비스 ***");
        System.out.println("============================================================================================");
        System.out.println("                                    1. 식당 전체 조회");
        System.out.println("                                    2. 식당 타입별 조회");
        System.out.println("                                    3. 해당 식당 리뷰 조회");
        System.out.println("                                    4: 평균 별점 높은순 식당 조회");
        System.out.println("                                    5. 뒤로가기");
        System.out.println("                                    6. 서비스 종료하기");
        System.out.println("============================================================================================");
        System.out.print("메뉴를 선택하세요: ");
	}
}
