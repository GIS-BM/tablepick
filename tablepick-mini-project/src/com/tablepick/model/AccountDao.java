package com.tablepick.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tablepick.common.DbConfig;

public class AccountDao {
	public AccountDao() throws ClassNotFoundException {
		Class.forName(DbConfig.DRIVER);
	}

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

	// 계정 등록
	public boolean insertAccount(AccountVO accountVO) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "INSERT INTO account (id, type, name, password, tel) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountVO.getId());
			pstmt.setString(2, accountVO.getType());
			pstmt.setString(3, accountVO.getName());
			pstmt.setString(4, accountVO.getPassword());
			pstmt.setString(5, accountVO.getTel());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
		} finally {
			closeAll(rs, pstmt, con);
		}
		return result;
	}

	// 전체 계정 목록 출력
	public ArrayList<AccountVO> getAllAccounts() throws SQLException {
		ArrayList<AccountVO> list = new ArrayList<AccountVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT * FROM account";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new AccountVO(rs.getString("id"), rs.getString("type"), rs.getString("name"),
						rs.getString("password"), rs.getString("tel")));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 하나의 계정 출력
	public AccountVO findAccountById(String accountId) throws SQLException {
		AccountVO accountVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT * from account where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();
			if (rs.next())
				accountVO = new AccountVO(rs.getString("id"), rs.getString("type"), rs.getString("name"),
						rs.getString("password"), rs.getString("tel"));
		} finally {
			closeAll(rs, pstmt, con);
		}
		return accountVO;
	}

	// 계정 데이터 수정
	public boolean updateAccount(AccountVO accountVO) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "UPDATE account SET type = ?, name = ?, password = ?, tel = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountVO.getType());
			pstmt.setString(2, accountVO.getName());
			pstmt.setString(3, accountVO.getPassword());
			pstmt.setString(4, accountVO.getTel());
			pstmt.setString(5, accountVO.getId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 계정 삭제
	public boolean deleteAccount(String id) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "DELETE FROM account WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			int rows = pstmt.executeUpdate();
			result = rows > 0;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 레스토랑 idx 찾는 메서드
	public int findRestaurantIdByName(String name) throws SQLException {
		int restaurantId = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx from restaurant where name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next())
				restaurantId = rs.getInt("idx");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return restaurantId;
	}

	// 레스토랑 name 찾는 메서드
	public String findRestaurantNameById(int num) throws SQLException {
		String name = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT name from restaurant where idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next())
				name = rs.getString("name");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return name;
	}

	// 예약 등록
	public boolean insertReserve(ReserveVO reserveVO) throws SQLException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "INSERT INTO reserve (account_id, restaurant_idx, reservepeople,"
					+ " reservedate, reservetime) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reserveVO.getAccountId());
			pstmt.setInt(2, reserveVO.getRestaurantId());
			pstmt.setInt(3, reserveVO.getReservePeople());
			pstmt.setDate(4, java.sql.Date.valueOf(reserveVO.getReserveDate()));
			pstmt.setInt(5, reserveVO.getReserveTime());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
		} finally {
			closeAll(rs, pstmt, con);
		}
		return result;
	}

	// 모든 예약 조회
	public ArrayList<ReserveVO> getAllReserves(String id) throws SQLException {
		ArrayList<ReserveVO> list = new ArrayList<ReserveVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, restaurant_idx, reservepeople ,"
					+ " reservedate, reservetime , registerdate FROM reserve where account_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Date reserveDateTime = rs.getDate("reservedate");
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDate reserveDate = reserveDateTime.toLocalDate();
				LocalDateTime registerDate= registerDateTime.toLocalDateTime();

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
			String sql = "UPDATE reserve SET restaurant_idx = ?, reservepeople = ?, reservedate = ?, reservetime = ? WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, updated.getRestaurantId());
			pstmt.setInt(2, updated.getReservePeople());
			pstmt.setDate(3, java.sql.Date.valueOf(updated.getReserveDate()));
			pstmt.setInt(4, updated.getReserveTime());
			pstmt.setInt(5, updated.getReserveId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
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
			String sql = "DELETE FROM reserve WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, old.getReserveId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
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
			String sql = "SELECT idx, account_id, name, type, address, " + " tel, opentime FROM restaurant";
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
					+ " tel, opentime FROM restaurant where type = ?";
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
			String sql = "SELECT v.idx, v.account_id, v.restaurant_idx, v.star, v.comment , v.registerdate "
					+ "  FROM restaurant r inner join review v on r.idx = v.restaurant_idx where r.idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Timestamp registerDateTime = rs.getTimestamp("registerdate");
				LocalDateTime registerDate = null;
				if (registerDateTime != null) {
					registerDate = registerDateTime.toLocalDateTime();
				}

				list.add(new ReviewVO(rs.getInt("v.idx"), rs.getString("v.account_id"), rs.getInt("v.restaurant_idx"),
						rs.getInt("v.star"), rs.getString("v.comment"), registerDate));
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return list;
	}

	// 별점 높은 순 조회
	public Map<RestaurantVO, Double> searchRestaurantByStar() throws SQLException {
		LinkedHashMap<RestaurantVO, Double> map = new LinkedHashMap<>();
		RestaurantVO vo = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "select r.idx, r.account_id, r.name, r.type, r.address, r.tel, r.opentime, round(avg(v.star), 2), "
					+ " count(v.idx) from restaurant r left join review v on r.idx = v.restaurant_idx "
					+ " group by r.idx, r.account_id,r.name, r.type, r.address, r.tel, r.opentime "
					+ " order by round(avg(v.star), 2) desc;";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LocalTime openTime = rs.getObject("r.opentime", LocalTime.class);

				vo = new RestaurantVO(rs.getInt("r.idx"), rs.getString("r.account_id"), rs.getString("r.name"),
						rs.getString("r.type"), rs.getString("r.address"), rs.getString("r.tel"), openTime);
				double avgStar = rs.getDouble("round(avg(v.star), 2)");
				map.put(vo, avgStar);
			}
		} finally {
			closeAll(rs, pstmt, con);
		}
		return map;
	}
}
