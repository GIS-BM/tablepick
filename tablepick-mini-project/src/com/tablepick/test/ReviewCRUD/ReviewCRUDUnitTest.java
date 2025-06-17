package com.tablepick.test.ReviewCRUD;

import java.sql.SQLException;

import com.tablepick.model.ReviewDao;
import com.tablepick.model.ReviewVO;

public class ReviewCRUDUnitTest {
    ReviewDao reviewDao = new ReviewDao();

    public static void main(String[] args) {
        ReviewCRUDUnitTest test = new ReviewCRUDUnitTest();
        test.registerReviewTest();
        test.findMyReviewByIdTest();
    }
    
    public void registerReviewTest() {
    	try {
    		System.out.println("registerReviewTest 테스트");
				ReviewVO reviewresult = reviewDao.registerReview(5, 3, "리뷰테스트");
				System.out.println(reviewresult);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }
    
    
    
    public void findMyReviewByIdTest() {
    	try {
    		System.out.println("findMyReviewByIdTest 테스트");
				System.out.println(reviewDao.findMyReviewById("cust01"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }
}