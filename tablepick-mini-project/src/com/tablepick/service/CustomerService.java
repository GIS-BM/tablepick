package com.tablepick.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.CustomerDao;
import com.tablepick.model.ReserveVO;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class CustomerService {
	private static CustomerService instance;
	private CustomerDao customerDao;
	private AccountVO accountId = null;

	private CustomerService() {
		try {
			customerDao = new CustomerDao();
			accountId = CommonService.getInstance().getLoginDataSession();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static synchronized CustomerService getInstance() {
		if (instance == null) {
			instance = new CustomerService();
		}
		return instance;
	}

	// 리뷰 작성
	public ReviewVO writeReview(int reserveIdx, int star, String comment) throws SQLException {
		return customerDao.createReview(reserveIdx, star, comment);
	}

	// 아이디로 내 리뷰 조회
	public ArrayList<ReviewVO> getMyReviews(String accountId) throws SQLException {
		return customerDao.findMyReviewById(accountId);
	}

	// 내 리뷰 수정(아이디랑 리뷰 번호로 변경 데이터 찾기)
	public ReviewVO updateMyReview(String accountId, int reviewIdx, int newStar, String newComment)
			throws SQLException {
		return customerDao.updateMyReviewById(accountId, reviewIdx, newStar, newComment);
	}

	// 리뷰 번호로 리뷰 조회
	public ReviewVO getReviewByIdx(int reviewIdx) throws SQLException {
		return customerDao.findReviewByIdx(reviewIdx);
	}

	// 리뷰 삭제
	public boolean deleteReview(int reviewIdx) throws SQLException {
		return customerDao.deleteReviewById(reviewIdx);
	}

	// 레스토랑 ID로 이름 조회
	public String getRestaurantNameById(int id) throws SQLException, NotFoundRestaurantException {
		return customerDao.findRestaurantNameById(id);
	}

	// 레스토랑 이름으로 ID 조회
	public int getRestaurantIdByName(String name) throws SQLException, NotFoundRestaurantException {
		return customerDao.findRestaurantIdByName(name);
	}

	// 예약 등록
	public boolean registerReserve(ReserveVO reserveVO) throws Exception {
		return customerDao.insertReserve(reserveVO);
	}

	// 특정 식당의 예약 목록
	public List<ReserveVO> getReservesByRestaurantId(int restaurantId) throws SQLException {
		return customerDao.getRestaurantReserves(restaurantId);
	}

	// 특정 계정의 예약 목록
	public List<ReserveVO> getReservesByAccountId(String accountId) throws SQLException {
		return customerDao.getAccountReserves(accountId);
	}

	// 예약 수정
	public boolean modifyReserve(ReserveVO updated) throws SQLException {
		return customerDao.updateReserve(updated);
	}

	// 예약 삭제
	public boolean removeReserve(ReserveVO old) throws SQLException {
		return customerDao.deleteReserve(old);
	}

	// 모든 식당 조회
	public List<RestaurantVO> getAllRestaurants() throws SQLException {
		return customerDao.searchAllRestaurants();
	}

	// 식당 타입별 조회
	public List<RestaurantVO> getRestaurantsByType(String type) throws SQLException {
		return customerDao.searchRestaurantByType(type);
	}

	// 특정 식당 리뷰 조회
	public List<ReviewVO> getReviewsByRestaurantId(int restaurantId) throws SQLException {
		return customerDao.searchRestaurantReviewView(restaurantId);
	}

	// 별점 순 정렬된 식당 목록
	public Map<RestaurantVO, double[]> getRestaurantsByStar() throws SQLException {
		return customerDao.searchRestaurantByStar();
	}

	// 식당 예약
	public void reserveRestaurant(BufferedReader reader) {
		try {
			System.out.println("\n[식당 예약]");
			System.out.print("식당명: ");
			String name = reader.readLine();
			System.out.print("예약 인원: ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("예약 날짜 ( 예)2025-06-12 ) : ");
			String date = reader.readLine();
			System.out.println("예약 시간:");
			int time = Integer.parseInt(reader.readLine());
			int restaurantId = customerDao.findRestaurantIdByName(name);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			if (time < 9 || time > 21) {
				System.out.println("잘못된 입력입니다.");
				return;
			}
			LocalDate registerDate = LocalDate.parse(date, formatter);
			ReserveVO reserveVO = new ReserveVO(accountId.getId(), restaurantId, count, registerDate, time);
			if (customerDao.insertReserve(reserveVO)) {
				System.out.println("예약이 성공하였습니다.");
			} else {
				System.out.println("예약 등록 실패");
			}

		} catch (DateTimeParseException e) {
			System.out.println("잘못된 입력입니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 식당 예약 조회
	public void readReserve(BufferedReader reader) throws IOException {
		try {
			System.out.println("\n[식당 예약 조회]");
			System.out.println("조회할 식당 이름을 입력하세요.");
			String name = reader.readLine();
			int idx = customerDao.findRestaurantIdByName(name);
			ArrayList<ReserveVO> list = customerDao.getRestaurantReserves(idx);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				System.out.println(name + " 식당에 등록된 예약");
				for (ReserveVO vo : list) {
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println("식당명: " + customerDao.findRestaurantNameById(vo.getRestaurantId()) + " 인원 수: "
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

	// 예약 update
	public void reserveUpdate(BufferedReader reader) {
		try {
			System.out.println("\n등록된 예약 목록");
			ArrayList<ReserveVO> list = customerDao.getAccountReserves(accountId.getId());
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
					ReserveVO vo = list.get(i);
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println((i + 1) + ". 식당명: " + customerDao.findRestaurantNameById(vo.getRestaurantId())
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

			System.out.print("식당명 (기존: " + customerDao.findRestaurantNameById(old.getRestaurantId()) + "): ");
			String name = reader.readLine();
			System.out.print("예약 인원 (기존: " + old.getReservePeople() + "): ");
			int count = Integer.parseInt(reader.readLine());
			System.out.print("예약 날짜 ( 예)2025-06-12 ) (기존: " + old.getReserveDate() + "): ");
			String date = reader.readLine();
			System.out.print("예약 시간 (기존: " + old.getReserveTime() + "): ");
			int time = Integer.parseInt(reader.readLine());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate reserveDate = LocalDate.parse(date, formatter);
			int resId = customerDao.findRestaurantIdByName(name);
			ReserveVO updated = new ReserveVO(old.getReserveId(), accountId.getId(),
					name.isEmpty() ? old.getRestaurantId() : resId, count == 0 ? old.getReservePeople() : count,
					date.isEmpty() ? old.getReserveDate() : reserveDate, time == 0 ? old.getReserveTime() : time);

			if (customerDao.updateReserve(updated)) {
				System.out.println("예약이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정이 실패하였습니다.");
			}
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 예약 delete
	public void reserveDelete(BufferedReader reader) {
		try {
			System.out.print("\n등록된 예약 목록");
			ArrayList<ReserveVO> list = customerDao.getAccountReserves(accountId.getId());
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (list.isEmpty()) {
				System.out.println("등록된 예약이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
					ReserveVO vo = list.get(i);
					String formattedRegisterDate = vo.getRegisterDate().format(dateTimeFormatter);
					System.out.println((i + 1) + ". 식당명: " + customerDao.findRestaurantNameById(vo.getRestaurantId())
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
			if (customerDao.deleteReserve(old)) {
				System.out.println("예약이 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("삭제가 실패하였습니다.");
			}
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 모든 식당 조회
	public void searchAllRestaurant() {
		try {
			System.out.println("\n[식당 전체 조회]");
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
			System.out.println("\n조회할 식당 타입을 선택하세요");
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
				return;
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
			System.out.println("\n리뷰를 조회할 식당을 선택하세요 ");
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
			System.out.println("\n[별점 높은순 조회]");
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

	// 리뷰 등록하는 테스트 메서드
	public void createReview(BufferedReader reader) throws IOException {
		try {
			System.out.println("\n[식당 리뷰 등록]");
			System.out.print("식당명: ");
			String name = reader.readLine();
			System.out.print("별점: ");
			int star = Integer.parseInt(reader.readLine());
			System.out.print("코멘트: ");
			String comment = reader.readLine();
			int reserveId = customerDao.findReserveIdxByRestaurantName(name, accountId.getId());
			ReviewVO reviewResult = customerDao.createReview(reserveId, star, comment);
			if(reviewResult!=null)
				System.out.println(accountId.getId()+": "+" 식당: "+customerDao.findRestaurantNameByReserveIdx(reserveId)
				+" 별점: "+star+"점 코멘트: "+comment);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 리뷰찾는 테스트 메서드
	public void findMyReviewById(BufferedReader reader) {
		try {
			System.out.println("\n[내 리뷰 검색]");
			ArrayList<ReviewVO> list = customerDao.findMyReviewById(accountId.getId());
			if (list.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				return;
			}
			for(ReviewVO vo: list) {
				System.out.println("식당: "+customerDao.findRestaurantNameByReserveIdx(vo.getReserveIdx())
				+" 별점: "+vo.getStar()+"점 코멘트: "+vo.getComment());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		}
	}

	// 본인 아이디에 해당되는 리뷰를 수정하는 기능 테스트 메서드
	public void updateReviewById(BufferedReader reader) {

		try {
			System.out.println("\n[내 리뷰 수정]");
			List<ReviewVO> list = customerDao.findMyReviewById(accountId.getId());
			if (list.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				return;
			}
			System.out.println("\n작성한 리뷰 목록");
			for(int i =0; i<list.size(); i++) {
				ReviewVO vo = list.get(i);
				System.out.println((i+1)+ ". 식당: "+customerDao.findRestaurantNameByReserveIdx(vo.getReserveIdx())
				+" 별점: "+vo.getStar()+"점 코멘트: "+vo.getComment());
			}
			System.out.print("\n수정할 리뷰 번호를 입력하세요: ");
			int choice = Integer.parseInt(reader.readLine());
			if (choice < 1 || choice > list.size()) {
				System.out.println("잘못된 선택입니다.");
				return;
			}
			ReviewVO old = list.get(choice - 1);
			System.out.print("새 별점(1~10)을 입력하세요: (기존: " + old.getStar() + "): ");
			int star = Integer.parseInt(reader.readLine());
			if (star < 1 || star > 10) {
				System.out.println("fail : 별점은 1~10 사이의 숫자여야 합니다.");
				return;
			}
			System.out.print("새 코멘트를 입력하세요: (기존: " + old.getComment() + "): ");
			String comment = reader.readLine();
			ReviewVO updated = new ReviewVO(old.getIdx(), old.getReserveIdx(),
					star == 0 ? old.getStar() : star,
					comment.isEmpty() ? old.getComment() : comment);
			ReviewVO updatedReview = customerDao.updateMyReviewById(accountId.getId(), updated.getIdx(), star, comment);
			if (updatedReview!=null) {
				System.out.println("리뷰가 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정이 실패하였습니다.");
			}

		} catch (IOException e) {
			System.out.println("fail : 입력 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("fail : 숫자 입력이 잘못되었습니다.");
		} catch (SQLException e) {
			System.out.println("fail : 데이터베이스 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());
		}
	}

	// 본인 아이디에 해당되는 리뷰 출력 후 하나 선택해서 삭제하는 뷰 테스트 메서드
	public void deleteMyReviewById(BufferedReader reader) {
		try {
			System.out.println("[내 리뷰 삭제]");
			List<ReviewVO> reviews = customerDao.findMyReviewById(accountId.getId());

			if (reviews.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				// excetion 처리 할것
				return;
			}

			System.out.println("\n작성한 리뷰 목록");
			for(int i =0; i< reviews.size(); i++) {
				ReviewVO vo = reviews.get(i);
				System.out.println((i+1)+ ". 식당: "+customerDao.findRestaurantNameByReserveIdx(vo.getReserveIdx())
				+" 별점: "+vo.getStar()+"점 코멘트: "+vo.getComment());
			}
			System.out.print("\n수정할 리뷰 번호를 입력하세요: ");
			int choice = Integer.parseInt(reader.readLine());
			if (choice < 1 || choice > reviews.size()) {
				System.out.println("잘못된 선택입니다.");
				return;
			}
			ReviewVO old = reviews.get(choice - 1);
			if (customerDao.deleteReviewById(old.getIdx()))
				System.out.println("리뷰 삭제가 성공했습니다.");
			else {
				System.out.println("리뷰 삭제가 실패했습니다.");
			}
		} catch (SQLException | NumberFormatException | IOException e) {
			e.printStackTrace();
		} catch (NotFoundRestaurantException e) {
			System.out.println(e.getMessage());;
		}

	}

}
