package com.tablepick.service;

import java.sql.SQLException;
<<<<<<< Updated upstream
import java.util.ArrayList;

import com.tablepick.model.CustomerDao;
import com.tablepick.model.ReviewVO;

public class CustomerService {

    // 싱글톤 패턴 적용
    private static CustomerService instance = new CustomerService();
    private final CustomerDao customerDao;

    // private 생성자
    private CustomerService() {
        this.customerDao = new CustomerDao();
    }

    // public 싱글톤 인스턴스 반환 메서드
    public static CustomerService getInstance() {
        return instance;
    }

    // 리뷰 등록
    public ReviewVO registerReview(int reserveId, int star, String comment) throws SQLException {
        return customerDao.registerReview(reserveId, star, comment);
    }

    // 내 리뷰 전체 조회
    public ArrayList<ReviewVO> findMyReviews(String accountId) throws SQLException {
        return customerDao.findMyReviewById(accountId);
    }

    // 내 리뷰 수정
    public ReviewVO updateMyReview(String accountId, int reviewIdx, int star, String comment) throws SQLException {
        return customerDao.changeMyReviewById(accountId, reviewIdx, star, comment);
    }

    // 리뷰 상세 조회
    public ReviewVO getReviewById(int reviewIdx) throws SQLException {
        return customerDao.getReviewById(reviewIdx);
    }

    // 내 리뷰 삭제
    public boolean deleteReviewById(int reviewIdx) throws SQLException {
        return customerDao.deleteReviewById(reviewIdx);
    }
=======
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.CustomerDao;
import com.tablepick.model.ReserveVO;
import com.tablepick.model.RestaurantVO;
import com.tablepick.model.ReviewVO;

public class CustomerService {
	private CustomerDao customerDao;

	public CustomerService() throws ClassNotFoundException {
		customerDao = new CustomerDao();
	}

	public int getRestaurantIdByName(String name) throws NotFoundRestaurantException {
		try {
			return customerDao.findRestaurantIdByName(name);
		} catch (SQLException e) {
			throw new RuntimeException("식당 ID 조회 실패", e);
		}
	}

	public String getRestaurantNameById(int id) throws NotFoundRestaurantException {
		try {
			return customerDao.findRestaurantNameById(id);
		} catch (SQLException e) {
			throw new RuntimeException("식당 이름 조회 실패", e);
		}
	}

	public List<RestaurantVO> getAllRestaurants() {
		try {
			return customerDao.searchAllRestaurants();
		} catch (SQLException e) {
			throw new RuntimeException("식당 전체 조회 실패", e);
		}
	}

	public List<RestaurantVO> getRestaurantsByType(String type) {
		try {
			return customerDao.searchRestaurantByType(type);
		} catch (SQLException e) {
			throw new RuntimeException("타입별 식당 조회 실패", e);
		}
	}

	public boolean insertReserve(ReserveVO vo) {
		try {
			return customerDao.insertReserve(vo);
		} catch (SQLException e) {
			throw new RuntimeException("예약 등록 실패", e);
		}
	}

	public boolean updateReserve(ReserveVO vo) {
		try {
			return customerDao.updateReserve(vo);
		} catch (SQLException e) {
			throw new RuntimeException("예약 수정 실패", e);
		}
	}

	public boolean deleteReserve(ReserveVO vo) {
		try {
			return customerDao.deleteReserve(vo);
		} catch (SQLException e) {
			throw new RuntimeException("예약 삭제 실패", e);
		}
	}

	public List<ReviewVO> getReviewsByRestaurantId(int id) {
		try {
			return customerDao.searchRestaurantReviewView(id);
		} catch (SQLException e) {
			throw new RuntimeException("리뷰 조회 실패", e);
		}
	}

	public Map<RestaurantVO, double[]> getRestaurantsByStar() {
		try {
			return customerDao.searchRestaurantByStar();
		} catch (SQLException e) {
			throw new RuntimeException("별점 높은 순 조회 실패", e);
		}
	}

	public List<ReserveVO> getRestaurantReserves(int idx) {
		try {
			return customerDao.getRestaurantReserves(idx);
		} catch (SQLException e) {
			throw new RuntimeException("식당 예약 조회 실패", e);
		}
	}

	public List<ReserveVO> getAccountReserves(String id) {
		try {
			return customerDao.getAccountReserves(id);
		} catch (SQLException e) {
			throw new RuntimeException("계정 예약 조회 실패", e);
		}
	}
>>>>>>> Stashed changes
}
