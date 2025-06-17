package com.tablepick.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tablepick.common.DatabaseUtil;
import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.exception.RestaurantNotFoundException;

//레스토랑 Dao 입니다. 

public class RestaurantDao {

	/**
	 * 식당 삭제 시 본인의 아이디와 비밀번호를 다시 입력합니다. <br>
	 * 이것이 일치해야 식당 삭제가 가능합니다. <br>
	 * 
	 * @param accountId
	 * @param password
	 * @return
	 * @throws NotMatchedPasswordException
	 * @throws AccountNotFoundException
	 * @throws SQLException
	 */
	public boolean findAccount(String accountId, String password)
			throws NotMatchedPasswordException, AccountNotFoundException, SQLException {

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
				// 아이디가 존재하지 않을 때
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
	public int makeRestaurant(RestaurantVO restaurantVO) throws SQLException {
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
	 * 
	 * @param accountId
	 * @param password
	 * @throws SQLException
	 * @throws AccountNotFoundException
	 * @throws NotMatchedPasswordException
	 */
	public void deleteMyRestaurant(String accountId, String password)
			throws SQLException, NotMatchedPasswordException, AccountNotFoundException {
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

	/**
	 * 해당 아이디로 식당이 존재하는지 확인하는 메서드
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
	 * 식당 정보 조회 및 식당 별 매출액 조회하는 메서드
	 * 내 식당이 존재하는지 먼저 확인 후 쿼리문을 실행한다.
	 * 내 식당 조회 시 정보, 매출액 출력
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 * @throws RestaurantNotFoundException 
	 */
	public List<Map<String, String>> checkMyRestaurantAndSales(String accountId, int restaurantIdx) throws SQLException, RestaurantNotFoundException {
		if (existRes(accountId) == false) {
			throw new RestaurantNotFoundException(accountId + "님의 식당이 존재하지 않습니다.");
		}
		
		List<Map<String, String>> listM = new ArrayList<Map<String,String>>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT r.name AS name, r.type AS type, r.address AS address, r.tel AS tel, COALESCE(SUM(s.sales), 0) AS sales ");
		sql.append("FROM restaurant r ");
		sql.append("LEFT JOIN reserve rv ON r.idx = rv.restaurant_idx ");
		sql.append("LEFT JOIN sales s ON rv.idx = s.reserve_idx ");
		sql.append("WHERE r.account_id =? AND r.idx = ? ");
		sql.append("GROUP BY r.idx, r.name, r.type, r.address, r.tel;");

		try(Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());
				){
			pstmt.setString(1, accountId);
			pstmt.setInt(2, restaurantIdx);
			try(ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", rs.getString("name"));
					map.put("type", rs.getString("type"));
					map.put("address", rs.getString("address"));
					map.put("tel", rs.getString("tel"));
					map.put("sales", String.valueOf(rs.getLong("sales")));
					
					listM.add(map);
				}
			} 
		}

		return listM;
	}

	/**
	 * 식당 정보 및 매출액 업데이트
	 * 
	 * @param accountId
	 * @param reservationIdx 
	 * @param newName
	 * @param newType
	 * @param newAddress
	 * @param newTel
	 * @param newSales
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public void changeMyRestaurantInfoAndSales(String accountId, int reservationIdx, String newName, String newType, String newAddress,
			String newTel, int newSales) throws SQLException, RestaurantNotFoundException {
		if (existRes(accountId) == false) {
			throw new RestaurantNotFoundException(accountId + "님의 식당이 존재하지 않습니다.");
		}
		
		Connection con = DatabaseUtil.getConnection();
		PreparedStatement pstmt = null;
		try {
			
			con.setAutoCommit(false); // 트랜젝션 처리를 위한 자동 커밋 모드 해제
			
			String updateRestaurantInfoSql = "UPDATE restaurant SET name = ?, type = ?, address = ?, tel = ? WHERE account_id = ?";
			String updateSalesSql = "UPDATE sales SET sales = ? WHERE reserve_idx = ?";
			
			pstmt = con.prepareStatement(updateRestaurantInfoSql);
			pstmt.setString(1, newName);
			pstmt.setString(2, newType);
			pstmt.setString(3, newAddress);
			pstmt.setString(4, newTel);
			pstmt.setString(5, accountId);
			
			int newInfoResult = pstmt.executeUpdate();
			if (newInfoResult > 0) {
				System.out.println("레스토랑 업데이트 완료. 코드 : " + newInfoResult);
			}
			pstmt.close();
			
			pstmt = con.prepareStatement(updateSalesSql);
			pstmt.setInt(1, newSales);
			pstmt.setInt(2, reservationIdx);
			
			int newSalesResult = pstmt.executeUpdate();
			if (newSalesResult > 0) {
				System.out.println("매출액 업데이트 완료. 코드 : " + newSalesResult);
			}
			
			con.commit(); // 정보 업데이트 내의 모든 세부 작업 정상 처리
			System.out.println("모두 업데이트 완. commit");
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			DatabaseUtil.closeAll(pstmt, con);
		}
		
		
	}

	
	/**
	 * 식당의 매출액을 입력시 업데이트 해주는 메서드 (디폴트 값 0이기때문에 update 쿼리를 이용)
	 * 
	 * @param accountId
	 * @param totalSales
	 * @param newSales 
	 * @throws SQLException 
	 */
	public void updateRestaurantSales(String accountId, int reservationIdx, int newSales) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE sales s  ");
		sql.append("JOIN reserve rv ON s.reserve_idx = rv.idx ");
		sql.append("JOIN restaurant r ON rv.restaurant_idx = r.idx ");
		sql.append("SET s.sales = ? ");
		sql.append("WHERE s.idx > 0 AND r.account_id = ?");

		
		try(Connection con = DatabaseUtil.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
				){
			pstmt.setInt(1, newSales);
			pstmt.setString(2, accountId);
			
			pstmt.executeUpdate();
		}
		
		
	}

	/**
	 * 메뉴를 생성하는 메소드 입니다. 메뉴 데이터 뿐만 아니라 레스토랑 ID도 필요합니다.
	 * 
	 * @param menuVO
	 * @throws SQLException
	 */
	public void createMenu(MenuVO menuVO) throws SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int menuId = 0;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "INSERT INTO menu(restaurant_idx, name, price) VALUES(?,?,?);";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, menuVO.getRestaurantId());
			pstmt.setString(2, menuVO.getName());
			pstmt.setInt(3, menuVO.getPrice());

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys(); // 발급된 메뉴 id를 반환받는다.
			if (rs.next())
				menuId = rs.getInt(1);
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
	}

	/**
	 * 메뉴를 조회하는 메소드 입니다.
	 * 
	 * @param restaurantId
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public List<Map<String, String>> checkMenu(int restaurantId) throws SQLException {
		List<Map<String, String>> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();

			
				while (rs.next()) {
					// 계속 맵을 만들어줘야 함
					// 한 객체의 주솟값을 계속 받아버리면 이전의 데이터는 사라짐 (덮어쓰기가 되므로)
					Map<String, String> map = new HashMap<String, String>();
					map.put("메뉴", rs.getString("name"));
					map.put("가격", rs.getString("price"));
					list.add(map);
				}

		
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return list;
	}
	
	
	/**
	 * 메뉴를 찾는 메소드 입니다. 가격 수정 및 삭제 시 이 메소드가 먼저 호출됩니다.
	 * @throws SQLException 
	 * @throws AccountNotFoundException 
	 */
	
	public boolean findMenu(int restaurantId, String name)
			throws NotFoundMenuException, SQLException, AccountNotFoundException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean menuExist = true;


		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx =? AND name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();

			if (rs.next() == false) {
				// 메뉴가 존재하지 않을 때
				menuExist = false;
				throw new AccountNotFoundException("메뉴가 존재하지 않습니다. 다시 입력하세요.");

			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return menuExist;
	}
	
	/**
	 * 메뉴의 가격을 수정하는 메소드 입니다. <br>
	 * @param menuVO
	 * @throws SQLException
	 * @throws AccountNotFoundException 
	 * @throws NotFoundMenuException 
	 */

	public void UpdateMenu(MenuVO menuVO) throws SQLException, NotFoundMenuException, AccountNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		findMenu(menuVO.getRestaurantId(), menuVO.getName());
	
		try {
			con = DatabaseUtil.getConnection();
			String sql = "UPDATE menu SET price = ? WHERE name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, menuVO.getPrice());
			pstmt.setString(2, menuVO.getName());
		
			result = pstmt.executeUpdate();
			
		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}
		
	}

	public void deleteMenu(int restaurantId, String name) throws SQLException, NotFoundMenuException, AccountNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;

		findMenu(restaurantId, name);

		try {
			con = DatabaseUtil.getConnection();
			String sql = "DELETE FROM menu WHERE name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.executeUpdate();

		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}

		
	}
	
	/**
	 * 내 식당의 리뷰를 조회하는 메소드 입니다. 해당 식당의 id가 필요합니다.
	 * @param restaurantId
	 * @throws SQLException 
	 */
	public List checkMyRestaurantReview(int restaurantId) throws SQLException {
		
		List<Map<String, String>> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT account_id, star, comment, registerdate FROM review WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();
			
				while (rs.next()) {
					// 계속 맵을 만들어줘야 함
					// 한 객체의 주솟값을 계속 받아버리면 이전의 데이터는 사라짐 (덮어쓰기가 되므로)
					Map<String, String> map = new HashMap<String, String>();
					map.put("작성자", rs.getString("account_id"));
					map.put("별점", rs.getString("star"));
					map.put("내용", rs.getString("comment"));
					map.put("작성일자", rs.getString("registerdate"));
					list.add(map);
				}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return list;

	}


}
