package com.tablepick.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tablepick.common.DatabaseUtil;

public class AccountDao {
	
	public boolean existAccountId(String id) throws SQLException {
		boolean existAccount = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT 1 FROM account WHERE id = ?"; 
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			existAccount = rs.next(); // 결과행이 존재하면 true , 없으면 false 
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		return existAccount;
	}
	
	public void createAccount(AccountVO accountVO) throws SQLException {
		/*
		if(existAccountId(accountVO.getId()) == true)
			throw new AccountAlreadyExistsException 발동
		*/
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "insert into account (id, type, name, password, tel) values (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountVO.getId());
			pstmt.setString(2, accountVO.getType());
			pstmt.setString(3, accountVO.getName());
			pstmt.setString(4, accountVO.getPassword());
			pstmt.setString(5, accountVO.getTel());
			pstmt.executeUpdate();
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		
	}

	public String login(String id, String password) throws SQLException {
		/*
		if(existAccountId(receiverAccountNo) == false)
			throw new AccountNotFoundException 발동
		*/
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String type = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "select type,id,password from account where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				/*
				if(!password.equals(rs.getString("password"))
					throw new passwordNotMatchedException 발동
				*/
				type = rs.getString("type");
			}
			
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		return type;
	}
	
	public AccountVO checkAccount(String id1) throws SQLException {
		/*
		if(existAccountId(receiverAccountNo) == false)
			throw new AccountNotFoundException 발동
		 */
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountVO vo = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "select id, type, name, password, tel from account where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id1);
			rs=pstmt.executeQuery();
			if(rs.next())
				vo = new AccountVO(rs.getString("id"), rs.getString("type"), rs.getString("password"), rs.getString("name"),
						rs.getString("tel"));
			
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		return vo;
	}

	public void updateAccount(String updateId, AccountVO updateAccount) throws SQLException {
		/*
		if(existAccountId(receiverAccountNo) == false)
			throw new AccountNotFoundException 발동
		*/
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "update account set type = ?, name = ?, password = ?, tel = ? where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, updateAccount.getType());
			pstmt.setString(2, updateAccount.getName());
			pstmt.setString(3, updateAccount.getPassword());
			pstmt.setString(4, updateAccount.getTel());
			pstmt.setString(5, updateId);
			pstmt.executeUpdate();
			
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
	}

	public void deleteAccount(String deleteId) throws SQLException {
		/*
		if(existAccountId(receiverAccountNo) == false)
			throw new AccountNotFoundException 발동
		*/
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "delete from account where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, deleteId);
			pstmt.executeUpdate();
			
		}finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
	}

}

