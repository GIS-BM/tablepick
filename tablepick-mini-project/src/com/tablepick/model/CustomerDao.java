package com.tablepick.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tablepick.common.DbConfig;

public class CustomerDao {
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASS);
	}

	public void closeAll(PreparedStatement pstmt, Connection con) throws SQLException {
		if (pstmt != null)
			pstmt.close();
		if (con != null)
			con.close();
	}

	public void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) throws SQLException {
		if (rs != null)
			rs.close();
		closeAll(pstmt, con);
	}

	// 리뷰 등록 메서드
	// 해당 예약에 대한 리뷰를 등록해야한다.
	// 예약 번호를 매개변수로 입력받아 리뷰 객체를 반환한다.
	public ReviewVO createReview(int reserveId, int star, String comment) throws SQLException {
		// 메서드에서 사용하는 메서드 지역 변수 선언
		ReviewVO review = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			// String sql = "create star, comment, FROM Review where id =?";
			String sql = "INSERT INTO review(reserve_idx, star, comment) VALUES (?, ?, ?)";
			// reserve_idx는 예약 테이블 idx의 외래키이다.
			// 외래키 : 다른 테이블의 리본키를 참조하는 열 "외래어처럼 다른 테이블에서 와서 사용되는 키"
			// 참조키 : 외래키가 참조하는 대상 컬럼, 일반적으로 다른 테이블의 기본키 "참조되는 키"
			// 나머지 컬럼은 외래키거나 자동 증가이다.
			// account_id, restaurant_idx는 예약 테이블에서 가져온다.

			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// ?? auto_increment된 키를 얻기 위해 두 번째 인자로 옵션 추가

			pstmt.setInt(1, reserveId);
			pstmt.setInt(2, star);
			pstmt.setString(3, comment);

			int rows = pstmt.executeUpdate();
			// INSERT, UPDATE, DELETE 같은 DML(Data Manipulation Language) 문은 executeUpdate()
			// 를 써야 한다.

			if (rows > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int generatedIdx = rs.getInt(1);

					// 생성된 review 정보 조회 (registerdate 포함)
					String selectSql = "SELECT * FROM review WHERE idx = ?";
					PreparedStatement selectPstmt = con.prepareStatement(selectSql);

					selectPstmt.setInt(1, generatedIdx);
					// sql문에 generatedIdx 값 출력되서 해당되는 리뷰의 값 가져오게한다.
					ResultSet selectRs = selectPstmt.executeQuery();
					// 쿼리문 DB에 입력
					if (selectRs.next()) {
						review = new ReviewVO();
						review.setIdx(selectRs.getInt("idx"));
						review.setReserveIdx(selectRs.getInt("reserve_idx"));
						review.setStar(selectRs.getInt("star"));
						review.setComment(selectRs.getString("comment"));
						review.setRegisterDate(selectRs.getTimestamp("registerdate").toLocalDateTime());
					}
				}
			}
		} finally {
			closeAll(rs, pstmt, con);
		}
		return review;
	}

//내 리뷰 조회
	public ArrayList<ReviewVO> findMyReviewById(String accountId) throws SQLException {
		ArrayList<ReviewVO> reviewList = new ArrayList<>(); // NullPointer 방지 위해 초기화
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("select r.* ");
			sql.append("from review r ");
			sql.append("JOIN reserve rs ON r.reserve_idx = rs.idx ");
			// idx를 기준으로 예약 테이블과 리뷰 테이블을 조인
			// 조인하면 두 테이블이 합쳐지므로 account_id으로 해당하는 review 테이블의 데이터를 찾을 수 있다.
			sql.append("WHERE rs.account_id = ? ");

			pstmt = con.prepareStatement(sql.toString());
			// StringBuilder → String
			pstmt.setString(1, accountId);
			// ? 에 accountId 값 바인딩
			rs = pstmt.executeQuery();
			// sql문이 입력되고 값 바인딩도 끝난 prepareStatement 객체 executeQuery() 메서드 사용해서
			// DB에서 sql문 실행 반환값 ResultSet 객체에 저장

			while (rs.next()) {
				ReviewVO review = new ReviewVO(rs.getInt("idx"), rs.getInt("reserve_idx"), rs.getInt("star"),
						rs.getString("comment"), rs.getTimestamp("registerdate").toLocalDateTime());
				reviewList.add(review);
			}
		} finally {
			closeAll(rs, pstmt, con);
		}

		return reviewList;
	}

	/**
	 * changeMyReviewById 메서드 내 리뷰 변경 기능을 수행한다. 매개변수로 아이디, 별점, 댓글 3가지를 받는다. 받은 값을
	 * 이용해 데이터베이스에 해당 아이디에 해당하는 리뷰 데이터를 찾는다. 리뷰 목록이 나오면 리뷰중 하나를 선택해서 update 해서 변경한다.
	 * update해서 변경한다. 반환값으로 변경된 리뷰 값이나 결과값을 boolean 으로 출력
	 */
	/*
	 * UPDATE review r JOIN reserve rs ON r.reserve_idx = rs.idx SET r.star = 3,
	 * r.comment = '가격에 비해서 너무 음식이 질이 낮습니다.' WHERE rs.account_id = 'cust05' AND
	 * r.idx = 5;
	 */
	public ReviewVO updateMyReviewById(String accountId, int reviewIdx, int star, String comment) throws SQLException {
		ReviewVO updatedReview = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE review r ");
			sql.append("JOIN reserve rs ON r.reserve_idx = rs.idx ");
			sql.append("SET r.star = ?, r.comment = ? ");
			sql.append("WHERE rs.account_id = ? AND r.idx = ? ");

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, star);
			pstmt.setString(2, comment);
			pstmt.setString(3, accountId);
			pstmt.setInt(4, reviewIdx);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				updatedReview = findReviewByIdx(reviewIdx);
			}
		} finally {
			closeAll(pstmt, con);
		}
		return updatedReview;
	}

	// reviewIdx로 리뷰 데이터 가져오는 메서드
	public ReviewVO findReviewByIdx(int reviewIdx) throws SQLException {
		ReviewVO review = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			String sql = "SELECT * FROM review WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reviewIdx);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				review = new ReviewVO(rs.getInt("idx"), rs.getInt("reserve_idx"), rs.getInt("star"), rs.getString("comment"),
						rs.getTimestamp("registerdate").toLocalDateTime());
			}
		} finally {
			closeAll(rs, pstmt, con);
		}

		return review;
	}

	/**
	 * 내 리뷰 삭제 내 리뷰 목록 출력 후 삭제하고 싶은 리뷰 idx 입력해서 삭제
	 */

	public boolean deleteReviewById(int reviewIdx) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			String sql = "delete FROM review WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reviewIdx);
			int affectedRows = pstmt.executeUpdate();
			result = (affectedRows > 0); // 하나 이상의 행이 삭제되면 ture 입력

		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}
}