package com.tablepick.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
			if(rs.next())
				accountVO = new AccountVO(
				    rs.getString("id"),
				    rs.getString("type"),
				    rs.getString("name"),
				    rs.getString("password"),
				    rs.getString("tel")
				);		} finally {
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
}

