package com.tablepick.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.CustomerDao;
import com.tablepick.model.ReserveVO;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class CustomerService {
	private static CustomerService instance;
	private CustomerDao customerDao;

	private CustomerService() {
		customerDao = new CustomerDao();
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
	public ReviewVO updateMyReview(String accountId, int reviewIdx, int newStar, String newComment) throws SQLException {
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
}

