package com.tablepick.test.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.tablepick.model.CustomerDao;
import com.tablepick.model.ReviewVO;

public class ReviewCRUDUnitTest {
	CustomerDao customerDao = new CustomerDao();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {
		ReviewCRUDUnitTest test = new ReviewCRUDUnitTest();
		test.registerReviewTest();
		test.findMyReviewByIdTest();
		// test.updateReviewByIdTest();
		test.deleteMyReviewByIdTest();
	}

	// 리뷰 등록하는 테스트 메서드
	public void registerReviewTest() {
		try {
			System.out.println("registerReviewTest 테스트");
			ReviewVO reviewresult = customerDao.registerReview(5, 3, "리뷰테스트");
			System.out.println(reviewresult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 리뷰찾는 테스트 메서드
	public void findMyReviewByIdTest() {
		try {
			System.out.println("findMyReviewByIdTest 테스트");
			System.out.println(customerDao.findMyReviewById("cust01"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 본인 아이디에 해당되는 리뷰를 수정하는 기능 테스트 메서드
	public void updateReviewByIdTest() {
		System.out.print("아이디를 입력하세요: ");

		try {
			String accountId = br.readLine();

			List<ReviewVO> reviews = customerDao.findMyReviewById(accountId);

			if (reviews.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				return;
			}

			System.out.println("\n[작성한 리뷰 목록]");
			for (ReviewVO r : reviews) {
				System.out.printf("[리뷰번호: %d] 별점: %d점, 내용: %s%n", r.getIdx(), r.getStar(), r.getComment());
			}

			System.out.print("\n수정할 리뷰 번호를 입력하세요: ");
			int reviewIdx = Integer.parseInt(br.readLine());

			System.out.print("새 별점(1~10)을 입력하세요: ");
			int newStar = Integer.parseInt(br.readLine());

			if (newStar < 1 || newStar > 10) {
				System.out.println("fail : 별점은 1~10 사이의 숫자여야 합니다.");
				return;
			}

			System.out.print("새 코멘트를 입력하세요: ");
			String newComment = br.readLine();

			// 리뷰 수정 시도
			ReviewVO updatedReview = customerDao.changeMyReviewById(accountId, reviewIdx, newStar, newComment);

			if (updatedReview != null) {
				System.out.println("\n 리뷰가 성공적으로 수정되었습니다.");
				System.out.printf(" 수정된 리뷰 → [리뷰번호: %d] 별점: %d점, 내용: %s%n", updatedReview.getIdx(), updatedReview.getStar(),
						updatedReview.getComment());
			} else {
				System.out.println("fail : 리뷰 수정에 실패했습니다. 리뷰 번호나 아이디를 확인하세요.");
			}

		} catch (IOException e) {
			System.out.println("fail : 입력 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("fail : 숫자 입력이 잘못되었습니다.");
		} catch (SQLException e) {
			System.out.println("fail : 데이터베이스 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}
	
	// 본인 아이디에 해당되는 리뷰 출력 후 하나 선택해서 삭제하는 뷰 테스트 메서드
	public void deleteMyReviewByIdTest() {
		System.out.println("아이디를 입력하세요: ");
		
		try {
			String accountId = br.readLine();
			
			List<ReviewVO> reviews = customerDao.findMyReviewById(accountId);
			
			if (reviews.isEmpty()) {
				System.out.println("작성한 리뷰가 없습니다.");
				// excetion 처리 할것
				return;
			}
			
			// [] 해당 유저가 작성한 리뷰 목록이 출력
			System.out.println("\n[작성한 리뷰 목록]");
			for (ReviewVO r : reviews) {
				System.out.printf("[리뷰번호: %d] 별점: %d점, 내용: %s%n", r.getIdx(), r.getStar(), r.getComment());
			}
			
			System.out.print("\n삭제할 리뷰 번호를 입력해 주세요: ");
			int reviewIdx = Integer.parseInt(br.readLine());
			
			// reviewIdx가 존재하지 않을 경우 메시지 추가
			
			if(customerDao.deleteReviewById(reviewIdx))
				System.out.println("리뷰 삭제가 성공했습니다.");
			else {
				System.out.println("리뷰 삭제가 실패했습니다.");
			}
		} catch (SQLException | NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
		
	}

}