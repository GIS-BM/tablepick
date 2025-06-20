package com.tablepick.service;

import java.sql.SQLException;
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
}