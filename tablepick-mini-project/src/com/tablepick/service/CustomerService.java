package com.tablepick.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tablepick.model.CustomerDao;
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
}

