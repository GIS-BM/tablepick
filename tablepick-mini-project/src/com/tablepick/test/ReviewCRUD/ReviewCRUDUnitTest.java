package com.tablepick.test.ReviewCRUD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.tablepick.model.ReviewDao;
import com.tablepick.model.ReviewVO;

public class ReviewCRUDUnitTest {
	ReviewDao reviewDao = new ReviewDao();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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

	public void updateReviewByIdTest() {
		System.out.println("아이디를 입력하세요 : ");
		try {
			String accountId = br.readLine();

			List<ReviewVO> reviews = reviewDao.findMyReviewById(accountId);

			if (reviews.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				return;
			}

			System.out.println("\n작성한 리뷰 목록:");
			for (review r : reviews) {
				System.out.println("[리뷰번호: " + r.getIdx() + "] 별점: " + r.getStar() + ", 내용: " + r.getComment());
			}

			System.out.print("\n수정할 리뷰 번호를 입력하세요: ");
			int reviewIdx = Integer.parseInt(br.readLine());

			System.out.print("새 별점(1~10)을 입력하세요: ");
			int newStar = Integer.parseInt(br.readLine());

			if (newStar < 1 || newStar > 10) {
				System.out.println("별점은 1에서 10 사이의 숫자여야 합니다.");
				return;
			}

			System.out.print("새 코멘트를 입력하세요: ");
			String newComment = br.readLine();

			boolean success = reviewDao.updateReview(accountId, reviewIdx, newStar, newComment);

			if (success) {
				System.out.println("리뷰가 성공적으로 수정되었습니다.");
			} else {
				System.out.println("리뷰 수정에 실패했습니다. 리뷰 번호나 아이디를 확인하세요.");
			}

		} catch (IOException e) {
			System.out.println("입력 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("숫자 입력이 잘못되었습니다.");
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