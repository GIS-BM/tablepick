package com.tablepick.test.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.CustomerDao;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class SearchRestaurantUnitTest {
	private CustomerDao customerDao;
	private static SearchRestaurantUnitTest instance = new SearchRestaurantUnitTest();

	private SearchRestaurantUnitTest() {
		try {
			customerDao = new CustomerDao();
		} catch (Exception e) {
			
		}
	}

	public static SearchRestaurantUnitTest getInstance() {
		return instance;
	}

	// 모든 식당 조회
		public void searchAllRestaurant() {
			try {
				System.out.println("[식당 전체 조회]");
				ArrayList<RestaurantVO> list = customerDao.searchAllRestaurants();
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

		// 타입별 식당 조회
		public void searchRestaurantByType(BufferedReader reader) {
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
				ArrayList<RestaurantVO> list = customerDao.searchRestaurantByType(type);
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

		// 식당 리뷰 조회
		public void searchRestaurantReview(BufferedReader reader) throws IOException {
			try {
				System.out.println("리뷰를 조회할 식당을 선택하세요 ");
				ArrayList<RestaurantVO> list = customerDao.searchAllRestaurants();
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
				ArrayList<ReviewVO> reviewList = customerDao.searchRestaurantReviewView(restaurantId);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				if (reviewList.isEmpty()) {
					System.out.println("등록된 리뷰가 없습니다.");
				} else {
					for (ReviewVO rvo : reviewList) {
						String formattedRegisterDate = rvo.getRegisterDate().format(dateTimeFormatter);
						System.out.println("식당명: " + customerDao.findRestaurantNameById(vo.getRestaurantId()) + " 별점: "
								+ rvo.getStar() + "점 " + " 내용: " + rvo.getComment() + " 등록일: " + formattedRegisterDate);
					}
				}
			} catch (NotFoundRestaurantException e) {
				System.out.println(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 별점 높은순 식당 조회
		public void searchRestaurantByStar(BufferedReader reader) {
			try {
				System.out.println("[별점 높은순 조회]");
				Map<RestaurantVO, double[]> map = customerDao.searchRestaurantByStar();
				if (map.isEmpty()) {
					System.out.println("등록된 식당이 없습니다.");
				} else {
					for (RestaurantVO vo : map.keySet()) {
						double[] values = map.get(vo);
						double avgStar = values[0];
						int count = (int) values[1];
						System.out.println(vo.getName() + " 주소: " + vo.getAddress() + " 전화번호: " + vo.getTel() + " 오픈 시간: "
								+ vo.getOpenTime() + " 평균 별점: " + avgStar + " 예약자 수: " + count);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
