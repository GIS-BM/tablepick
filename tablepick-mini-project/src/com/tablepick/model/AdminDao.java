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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tablepick.common.DatabaseUtil;
import com.tablepick.common.DbConfig;
import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;

public class AdminDao {
	public AdminDao() throws ClassNotFoundException {
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

	// 계정 조회
	public void findAccounts() throws NotFoundAccountException, SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT * FROM account;";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next() == false) {
				throw new NotFoundAccountException("조회된 계정이 없습니다.");
			}
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
	}

	// 전체 계정 출력
	public ArrayList<AccountVO> findAllAccounts() throws SQLException, NotFoundAccountException {
		ArrayList<AccountVO> list = new ArrayList<AccountVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		findAccounts();
		try {
			con = getConnection();
			String sql = "SELECT * FROM account;";
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
	public AccountVO findAccount(String accountId) throws SQLException, NotFoundAccountException {
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
			if (!rs.next())
				throw new NotFoundAccountException(accountId + " 해당 ID의 계정이 존재하지 않습니다.");
			else {
				accountVO = new AccountVO(rs.getString("id"), rs.getString("type"), rs.getString("name"),
						rs.getString("password"), rs.getString("tel"));
			}
		} finally {
			closeAll(rs, pstmt, con);
		}
		return accountVO;
	}

	// 계정 수정
	public boolean updateAccount(AccountVO accountVO) throws SQLException, InfoNotEnoughException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			String sql = "UPDATE account SET type = ?, name = ?, password = ?, tel = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountVO.getType());
			pstmt.setString(2, accountVO.getName());
			pstmt.setString(3, accountVO.getPassword());
			pstmt.setString(4, accountVO.getTel());
			pstmt.setString(5, accountVO.getId());
			int rows = pstmt.executeUpdate();
			result = rows > 0;
			if (result)
				con.commit();
			else
				throw new InfoNotEnoughException("정보가 충분하지 않습니다.");
		} catch (InfoNotEnoughException e) {
			con.rollback();
			throw e;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 계정 삭제
	public boolean deleteAccount(String id) throws SQLException, InfoNotEnoughException {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			String sql = "DELETE FROM account WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			int rows = pstmt.executeUpdate();
			if (result = rows > 0)
				con.commit();
			else
				throw new InfoNotEnoughException("정보가 충분하지 않습니다.");
		} catch (InfoNotEnoughException e) {
			con.rollback();
			throw e;
		} finally {
			closeAll(pstmt, con);
		}
		return result;
	}

	// 레스토랑 name 찾는 메서드
	public String findRestaurantNameById(int num) throws SQLException, NotFoundRestaurantException {
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
			if (!rs.next())
				throw new NotFoundRestaurantException("조회된 식당이 없습니다.");
			else
				name = rs.getString("name");
		} finally {
			closeAll(rs, pstmt, con);
		}
		return name;
	}

	// 모든 예약 조회
	public ArrayList<ReserveVO> getAllReserves() throws SQLException {
		ArrayList<ReserveVO> list = new ArrayList<ReserveVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "SELECT idx, account_id, restaurant_idx, reservepeople ,"
					+ " reservedate, reservetime , registerdate FROM reserve";
			pstmt = con.prepareStatement(sql);
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

	public Map<String, Integer> getMostReservesAccount() throws SQLException {
		LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "select account_id,count(idx) from reserve group by account_id "
					+ " having count(idx) = (select max(reserve_count) from (select count(idx) "
					+ " as reserve_count from reserve group by account_id) as counts);";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String person = rs.getString("account_id");
				int max = rs.getInt("count(idx)");
				map.put(person, max);
			}

		} finally {
			closeAll(rs, pstmt, con);
		}
		return map;
	}

}
