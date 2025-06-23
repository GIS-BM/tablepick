package com.tablepick.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tablepick.common.DbConfig;
import com.tablepick.exception.AlreadyReservedException;
import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.NotFoundRestaurantException;

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

	// restaurant 이름으로 reserve idx 찾는 메서드
	public int findReserveIdxByRestaurantName(String name, String accountId)
			throws SQLException, NotFoundRestaurantException {
		int reserveId = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT r.idx FROM reserve r JOIN restaurant res ON r.restaurant_idx = res.idx "
					+ " LEFT JOIN review rv ON r.idx = rv.reserve_idx  WHERE res.name = ? AND r.account_id = ?; ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, accountId);
			rs = pstmt.executeQuery();
			if (rs.next())
				reserveId = rs.getInt("r.idx");
			if (reserveId == 0)
				throw new NotFoundRestaurantException(name + " 식당이 존재하지 않습니다.");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return reserveId;
	}
	// restaurant 번호로 reserve idx 찾는 메서드
	public int findReserveIdxByRestaurantIdx(int idx, String accountId)
			throws SQLException, NotFoundRestaurantException {
		int reserveId = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT r.idx,res.name FROM reserve r JOIN restaurant res ON r.restaurant_idx = res.idx "
					+ " LEFT JOIN review rv ON r.idx = rv.reserve_idx WHERE res.idx = ? AND r.account_id = ?; ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.setString(2, accountId);
			rs = pstmt.executeQuery();
			if (rs.next())
				reserveId = rs.getInt("r.idx");
			else
				throw new NotFoundRestaurantException("식당이 존재하지 않습니다.");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return reserveId;
	}

	// reserve idx 로 restaurant 이름 찾는 메서드
	public String findRestaurantNameByReserveIdx(int idx) throws SQLException, NotFoundRestaurantException {
		String restaurantName = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT res.name FROM reserve r JOIN restaurant res ON r.restaurant_idx = res.idx "
					+ " LEFT JOIN review rv ON r.idx = rv.reserve_idx WHERE r.idx = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if (rs.next())
				restaurantName = rs.getString("name");
			if (restaurantName == null)
				throw new NotFoundRestaurantException("식당이 존재하지 않습니다.");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return restaurantName;
	}

	// 리뷰 중복 확인 메서드
	public ArrayList<ReserveVO> findReservedReview(String id) throws SQLException {
		ArrayList<ReserveVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT v.* FROM reserve v	LEFT JOIN review rv "
					+ " ON v.idx = rv.reserve_idx "
					+ " WHERE v.account_id = ? AND (rv.idx IS NULL OR rv.idx = 0);";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Date reserveDateTime = rs.getDate("reservedate");
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDate reserveDate = reserveDateTime.toLocalDate();
				LocalDateTime registerDate = registerDateTime.toLocalDateTime();

				list.add(new ReserveVO(rs.getInt("idx"), rs.getString("account_id"), rs.getInt("restaurant_idx"),
						rs.getInt("reservepeople"), reserveDate, rs.getInt("reservetime"), registerDate));
			}
		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 리뷰 등록 메서드
	// 해당 예약에 대한 리뷰를 등록해야한다.
	// 예약 번호를 매개변수로 입력받아 리뷰 객체를 반환한다.
	public boolean createReview(int reserveId, int star, String comment) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			String sql = "INSERT INTO review(reserve_idx, star, comment) VALUES (?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reserveId);
			pstmt.setInt(2, star);
			pstmt.setString(3, comment);

			int rows = pstmt.executeUpdate();
			return rows > 0; // 등록 성공 시 true, 실패 시 false
		} finally {
			closeAll(null, pstmt, con);
		}
	}

//아이디로 리뷰 조회
	public ArrayList<ReviewVO> findMyReviewById(String accountId) throws SQLException {
		ArrayList<ReviewVO> reviewList = new ArrayList<>(); // NullPointer 방지 위해 초기화
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			String sql = "SELECT r.idx, r.reserve_idx, r.star, r.comment, r.registerdate " + " FROM review r "
					+ " JOIN reserve rs " + " ON r.reserve_idx = rs.idx " + " WHERE rs.account_id = ?";
			pstmt = con.prepareStatement(sql);
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
		try {
			con = getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE review r ");
			sql.append(" JOIN reserve rs ON r.reserve_idx = rs.idx ");
			sql.append(" SET r.star = ?, r.comment = ? ");
			sql.append(" WHERE rs.account_id = ? AND r.idx = ? ");

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
				review = new ReviewVO(rs.getInt("idx"), rs.getInt("reserve_idx"), rs.getInt("star"),
						rs.getString("comment"), rs.getTimestamp("registerdate").toLocalDateTime());
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

	// 레스토랑 idx 찾는 메서드
	public int findRestaurantIdByName(String name) throws SQLException, NotFoundRestaurantException {
		int restaurantId = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx FROM restaurant WHERE name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next())
				restaurantId = rs.getInt("idx");
			if (restaurantId == 0)
				throw new NotFoundRestaurantException(name + " 식당이 존재하지 않습니다.");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return restaurantId;
	}

	// 레스토랑 name 찾는 메서드
	public String findRestaurantNameById(int num) throws SQLException, NotFoundRestaurantException {
		String name = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT name FROM restaurant WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next())
				name = rs.getString("name");
			if (name == null)
				throw new NotFoundRestaurantException("식당이 존재하지 않습니다.");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return name;
	}

	// 예약 등록
	public boolean insertReserve(ReserveVO reserveVO, String id) throws Exception {
		// 같은 식당, 같은 시간, 같은 날짜에 예약 중복을 막는다.
		ArrayList<ReserveVO> myReserve = getAccountReserves(id);
		for (ReserveVO vo : myReserve) {
			if (vo.getRestaurantId() == reserveVO.getRestaurantId()
					&& vo.getReserveDate().equals(reserveVO.getReserveDate())
					&& vo.getReserveTime() == reserveVO.getReserveTime())
				throw new AlreadyReservedException("중복된 예약입니다.");
		}
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int reserveId = 0;
		try {

			con = getConnection();
			con.setAutoCommit(false);

			String sql = "INSERT INTO reserve (account_id, restaurant_idx, reservepeople,"
					+ " reservedate, reservetime) VALUES (?, ?, ?, ?, ?)";

			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, reserveVO.getAccountId());
			pstmt.setInt(2, reserveVO.getRestaurantId());
			pstmt.setInt(3, reserveVO.getReservePeople());
			pstmt.setDate(4, java.sql.Date.valueOf(reserveVO.getReserveDate()));
			pstmt.setInt(5, reserveVO.getReserveTime());
			int rows = pstmt.executeUpdate();
			if (result = rows < 0)
				throw new InfoNotEnoughException("데이터가 충분하지 않습니다.");
			else
				result = rows > 0;

			rs = pstmt.getGeneratedKeys(); // 발급된 reserve id를 반환받는다.
			if (rs.next()) {
				reserveId = rs.getInt(1); // 첫 번째 컬럼에서 reserve_idx 얻기
			}

			rs.close(); // 먼저 닫기
			pstmt.close();

			String salesSql = "INSERT INTO sales(reserve_idx, sales) VALUES (?,?)";
			pstmt = con.prepareStatement(salesSql);
			pstmt.setInt(1, reserveId);
			pstmt.setInt(2, 0);
			pstmt.executeUpdate();

			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeAll(rs, pstmt, con);
		}
		return result;
	}

	// 식당 예약 조회
	public ArrayList<ReserveVO> getRestaurantReserves(int idx) throws SQLException {
		ArrayList<ReserveVO> list = new ArrayList<ReserveVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, restaurant_idx, reservepeople ,"
					+ " reservedate, reservetime , registerdate FROM reserve WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Date reserveDateTime = rs.getDate("reservedate");
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDate reserveDate = reserveDateTime.toLocalDate();
				LocalDateTime registerDate = registerDateTime.toLocalDateTime();

				list.add(new ReserveVO(rs.getInt("idx"), rs.getString("account_id"), rs.getInt("restaurant_idx"),
						rs.getInt("reservepeople"), reserveDate, rs.getInt("reservetime"), registerDate));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 계정 예약 조회
	public ArrayList<ReserveVO> getAccountReserves(String id) throws SQLException {
		ArrayList<ReserveVO> list = new ArrayList<ReserveVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, restaurant_idx, reservepeople ,"
					+ " reservedate, reservetime , registerdate FROM reserve WHERE account_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Date reserveDateTime = rs.getDate("reservedate");
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDate reserveDate = reserveDateTime.toLocalDate();
				LocalDateTime registerDate = registerDateTime.toLocalDateTime();

				list.add(new ReserveVO(rs.getInt("idx"), rs.getString("account_id"), rs.getInt("restaurant_idx"),
						rs.getInt("reservepeople"), reserveDate, rs.getInt("reservetime"), registerDate));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 예약 수정
	public boolean updateReserve(ReserveVO updated) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			String sql = "UPDATE reserve SET restaurant_idx = ?, reservepeople = ?, reservedate = ?, reservetime = ? WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, updated.getRestaurantId());
			pstmt.setInt(2, updated.getReservePeople());
			pstmt.setDate(3, java.sql.Date.valueOf(updated.getReserveDate()));
			pstmt.setInt(4, updated.getReserveTime());
			pstmt.setInt(5, updated.getReserveId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 예약 삭제
	public boolean deleteReserve(ReserveVO old) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			String sql = "DELETE FROM reserve WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, old.getReserveId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 전체 식당 조회
	public ArrayList<RestaurantVO> searchAllRestaurants() throws SQLException {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, name, type, address, tel, opentime FROM restaurant";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LocalTime openTime = rs.getObject("opentime", LocalTime.class);

				list.add(new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"),
						rs.getString("type"), rs.getString("address"), rs.getString("tel"), openTime));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 타입별 식당 조회
	public ArrayList<RestaurantVO> searchRestaurantByType(String type) throws SQLException {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, name, type, address, "
					+ " tel, opentime FROM restaurant WHERE type = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, type);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LocalTime openTime = rs.getObject("opentime", LocalTime.class);

				list.add(new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"),
						rs.getString("type"), rs.getString("address"), rs.getString("tel"), openTime));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 해당 식당의 리뷰 조회
	public ArrayList<ReviewVO> searchRestaurantReviewView(int restaurantId) throws SQLException {
		ArrayList<ReviewVO> list = new ArrayList<ReviewVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT v.idx, v.reserve_idx, v.star, v.comment , v.registerdate, r.restaurant_idx "
					+ "FROM reserve r INNER JOIN review v ON r.idx = v.reserve_idx WHERE r.restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDateTime registerDate = null;
				if (registerDateTime != null) {
					registerDate = registerDateTime.toLocalDateTime();
				}

				list.add(new ReviewVO(rs.getInt("v.idx"), rs.getInt("v.reserve_idx"), rs.getInt("v.star"),
						rs.getString("v.comment"), registerDate));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 별점 높은 순 조회
	public Map<RestaurantVO, double[]> searchRestaurantByStar() throws SQLException {
		LinkedHashMap<RestaurantVO, double[]> map = new LinkedHashMap<>();
		RestaurantVO vo = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT r.idx, r.account_id, r.name, r.type, r.address, r.tel, r.opentime, round(avg(rv.star), 2), "
					+ "count(rv.idx) FROM restaurant r LEFT JOIN reserve rs on r.idx = rs.restaurant_idx "
					+ " LEFT JOIN review rv ON rs.idx = rv.reserve_idx GROUP BY r.idx, r.account_id, r.name, "
					+ " r.type, r.address, r.tel, r.opentime ORDER BY round(avg(rv.star), 2) DESC;";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LocalTime openTime = rs.getObject("r.opentime", LocalTime.class);

				vo = new RestaurantVO(rs.getInt("r.idx"), rs.getString("r.account_id"), rs.getString("r.name"),
						rs.getString("r.type"), rs.getString("r.address"), rs.getString("r.tel"), openTime);
				double avgStar = rs.getDouble("round(avg(rv.star), 2)");
				int count = rs.getInt("count(rv.idx)");
				map.put(vo, new double[] { avgStar, count });
			}
		} finally {
			closeAll(rs, pstmt, con);
		}
		return map;
	}
}