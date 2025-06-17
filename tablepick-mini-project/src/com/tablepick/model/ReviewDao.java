package com.tablepick.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tablepick.common.DbConfig;

public class ReviewDao {

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
	public ReviewVO registerReview(int reserveId, int star, String comment) throws SQLException {
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
						review.setReserve_idx(selectRs.getInt("reserve_idx"));
						review.setStar(selectRs.getInt("star"));
						review.setComment(selectRs.getString("comment"));
						review.setRegisterdate(selectRs.getTimestamp("registerdate"));
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
			sql.append("SELECT r.* ");
			sql.append("FROM review r ");
			sql.append("JOIN reserve re ON r.reserve_idx = re.idx ");
			sql.append("WHERE re.account_id = ?"); // 파라미터 바인딩

			pstmt = con.prepareStatement(sql.toString());
			// StringBuilder → String
			pstmt.setString(1, accountId);
			// ? 에 accountId 값 바인딩
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewVO review = new ReviewVO(rs.getInt("idx"), rs.getInt("reserve_idx"), rs.getInt("star"),
						rs.getString("comment"), rs.getTimestamp("registerdate"));
				reviewList.add(review);
			}
		} finally {
			closeAll(rs, pstmt, con);
		}

		return reviewList;
	}

	// 내 리뷰 변경
	/*
	UPDATE review r
	JOIN reserve re ON r.reserve_idx = re.idx
	SET r.star = 2,
	    r.comment = '리테 맛좋다!'
	WHERE re.account_id = 'cust05'
	  AND r.idx = 12;
	*/
	
	
	
	
	// 내 리뷰 삭제

}
