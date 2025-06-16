package com.tablepick.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tablepick.common.DbConfig;
import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;

public class TablePickSerivceCommon {
	AccountDao accountDao = null;
	public TablePickSerivceCommon() throws ClassNotFoundException {
		accountDao = new AccountDao();
		Class.forName(DbConfig.DRIVER);
	}
	
//회원가입 (이미 insertAccount가 있으니 재활용 가능)
public boolean registerAccount(AccountVO accountVO) throws SQLException {
   return accountDao.insertAccount(accountVO);
}

//로그인 메서드 (id, password 체크)
public boolean login(String id, String password) throws SQLException {
   boolean isLogin = false;
   Connection con = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;
   try {
       con = accountDao.getConnection();
       String sql = "SELECT password FROM account WHERE id = ?";
       pstmt = con.prepareStatement(sql);
       pstmt.setString(1, id);
       rs = pstmt.executeQuery();
       if(rs.next()) {
           String dbPassword = rs.getString("password");
           if(dbPassword.equals(password)) {
               isLogin = true;
           }
       }
   } finally {
  	 accountDao.closeAll(rs, pstmt, con);
   }
   return isLogin;
}
}
