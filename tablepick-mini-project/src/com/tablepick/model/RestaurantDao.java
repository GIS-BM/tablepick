package com.tablepick.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tablepick.common.DatabaseUtil;
import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.exception.RestaurantNotFoundException;

//레스토랑 Dao 입니다. 


public class RestaurantDao {
	
	/**
	 * 식당 삭제 시 본인의 아이디와 비밀번호를 다시 입력합니다. <br>
	 * 이것이 일치해야 식당 삭제가 가능합니다. <br>
	 * @param accountId
	 * @param password
	 * @return
	 * @throws NotMatchedPasswordException
	 * @throws AccountNotFoundException
	 * @throws SQLException
	 */
	public boolean findAccount(String accountId, String password) throws NotMatchedPasswordException, AccountNotFoundException, SQLException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean loginSuccess = true;
		
		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT password FROM account WHERE id = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();

			if (rs.next() == false) {
				//아이디가 존재하지 않을 때
				loginSuccess = false; 
				throw new AccountNotFoundException("아이디가 존재하지 않습니다. 다시 입력하세요.");

			} else {// 아이디가 존재하면 비밀번호 동일 여부 확인

				if (password.equals(rs.getString("password")) == false) {
					loginSuccess = false; 
					throw new NotMatchedPasswordException("비밀번호가 일치하지 않습니다. 다시 입력하세요.");
					
				}
				
			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return loginSuccess;
	}
	

	/**
	 * 식당을 등록하는 메소드 입니다.<br>
	 * 
	 * @param restaurantVO
	 * @return
	 * @throws SQLException
	 */
	public int makeRes(RestaurantVO restaurantVO) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int restaurantId = 0;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "INSERT INTO restaurant(account_id, name, type, address, tel) VALUES(?,?,?,?,?);";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, restaurantVO.getAccountId());
			pstmt.setString(2, restaurantVO.getName());
			pstmt.setString(3, restaurantVO.getType());
			pstmt.setString(4, restaurantVO.getAddress());
			pstmt.setString(5, restaurantVO.getTel());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys(); // 발급된 레스토랑 id를 반환받는다.
			if (rs.next())
				restaurantId = rs.getInt(1);
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return restaurantId;
	}// makeRes

	/**
	 * //등록된 식당을 삭제하는 메소드 입니다. <br>
	 * 비밀번호가 다르면 NotMatchedPasswordException 발생시키고 전파 <br>
	 * @param accountId
	 * @param password
	 * @throws SQLException
	 * @throws AccountNotFoundException 
	 * @throws NotMatchedPasswordException 
	 */
	public void deleteMyRes(String accountId, String password) throws SQLException, NotMatchedPasswordException, AccountNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		findAccount(accountId, password);

		try {
			con = DatabaseUtil.getConnection();
			String sql = "DELETE FROM restaurant WHERE account_id =?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			pstmt.executeUpdate();

		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}

	}
	
	/** 해당 아이디로 식당이 존재하는지 확인하는 메서드
	 * 
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public boolean existRes(String accountId) throws SQLException {
		boolean existRes = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT 1 FROM restaurant WHERE account_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();
			existRes = rs.next();
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		return existRes;
	}

	/**
	 * 내 식당을 조회하는 메서드 <br>
	 * 
	 * @param accountId
	 * @return res
	 * @throws SQLException
	 */
	public RestaurantVO checkMyRes(String accountId) throws SQLException {
//	public ArrayList<RestaurantVO> checkMyRes(String accountId) throws SQLException {
		RestaurantVO res = null;
//		ArrayList<RestaurantVO> resList = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT idx, account_id, name, type, address, tel ");
		sql.append("FROM restaurant ");
		sql.append("WHERE account_id = ?");

		try(Connection con = DatabaseUtil.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString())
		) {
			pstmt.setString(1, accountId);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
//				while (rs.next()) {
					res = new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"), rs.getString("type"), rs.getString("address"), rs.getString("tel"));
//					res.setRestaurantId(rs.getInt("idx"));
//					res.setAccountId(rs.getString("account_id"));
//					res.setName(rs.getString("name"));
//					res.setType(rs.getString("type"));
//					res.setAddress(rs.getString("address"));
//					res.setTel(rs.getString("tel"));
//					resList.add(new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"), rs.getString("type"), rs.getString("address"), rs.getString("tel")));
				}
			}
		}
		
		return res;
//		return resList;
	}

	/**
	 * 식당 정보 업데이트 메서드
	 * 
	 * @param accountId
	 * @param name
	 * @param type
	 * @param address
	 * @param tel
	 * @throws RestaurantNotFoundException
	 * @throws SQLException
	 */
	public void changeMyRes(String accountId, String name, String type, String address, String tel) throws RestaurantNotFoundException, SQLException {
		if (existRes(accountId) == false) {
			throw new RestaurantNotFoundException(accountId + "님의 식당이 존재하지 않습니다.");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE restaurant ");
		sql.append("SET name = ?, type = ?, address = ?, tel = ? ");
		sql.append("WHERE account_id = ?");
				
		try(Connection con = DatabaseUtil.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
		) {
			pstmt.setString(1, name);
			pstmt.setString(2, type);
			pstmt.setString(3, address);
			pstmt.setString(4, tel);
			pstmt.setString(5, accountId);
			
			pstmt.executeUpdate();
		}
	}

	/**
	 * 식당 정보 조회 및 식당 별 매출액 조회하는 메서드
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 */
	public RestaurantVO checkMyRestaurantAndReservation(String accountId, int reservationIdx) throws SQLException {
		RestaurantVO resVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rt.idx, rt.account_id, rt.name, rt.type, rt.address, rt.tel, SUM(rs.sale) ");
		sql.append("FROM restaurant rt ");
		sql.append("INNER JOIN reserve rs ON rt.idx = rs.restaurant_idx ");
		sql.append("WHERE rt.idx = ?");
		
		try(Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());
				){
			pstmt.setInt(1, reservationIdx);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					resVO = new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"), rs.getString("type"), rs.getString("address"), rs.getString("tel"), rs.getInt(7));
				}
			} 
		}

		return resVO;
	}


	public void updateRestaurantSales(String total) {
		
	}

	

}
