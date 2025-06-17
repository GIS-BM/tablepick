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

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
			pstmt.setString(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
//				while (rs.next()) {
					res = new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"),
							rs.getString("type"), rs.getString("address"), rs.getString("tel"));
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
	public void changeMyRes(String accountId, String name, String type, String address, String tel)
			throws RestaurantNotFoundException, SQLException {
		if (existRes(accountId) == false) {
			throw new RestaurantNotFoundException(accountId + "님의 식당이 존재하지 않습니다.");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE restaurant ");
		sql.append("SET name = ?, type = ?, address = ?, tel = ? ");
		sql.append("WHERE account_id = ?");

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
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
	 * 내 식당이 존재하는지 먼저 확인 후 쿼리문을 실행한다.
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 * @throws RestaurantNotFoundException 
	 */
	public RestaurantVO checkMyRestaurantAndReservation(String accountId, int restaurantIdx) throws SQLException, RestaurantNotFoundException {
		if (existRes(accountId) == false) {
			throw new RestaurantNotFoundException(accountId + "님의 식당이 존재하지 않습니다.");
		}
		RestaurantVO resVO = null;
		TotalSalesVO ttlVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT r.idx, r.account_id, r.name, r.type, r.address, r.tel, t.sales as totalsales ");
		sql.append("FROM restaurant r ");
		sql.append("INNER JOIN total_sales t ON r.idx = t.restaurant_idx ");
		sql.append("WHERE r.idx = ? AND r.account_id = ?");
		
		try(Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());
				){
			pstmt.setInt(1, restaurantIdx);
			pstmt.setString(2, accountId);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					resVO = new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"), rs.getString("type"), rs.getString("address"), rs.getString("tel"));
					ttlVO = new TotalSalesVO(rs.getInt("idx"), restaurantIdx, rs.getInt("totalsales"));
					resVO.setTotalSalesVO(ttlVO);
				}
			} 
		}

		return resVO;
	}

	/**
	 * 식당의 매출액을 입력시 업데이트 해주는 메서드 (디폴트 값 0이기때문에 update 쿼리를 이용)
	 * 
	 * @param accountId
	 * @param totalSales
	 * @throws SQLException 
	 */
	public void updateRestaurantSales(String accountId, int totalSales) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE total_sales ts ");
		sql.append("JOIN restaurant r ON ts.restaurant_idx = r.idx ");
		sql.append("SET ts.sales = ? ");
		sql.append("WHERE ts.idx > 0 AND r.account_id = ?");
		
		try(Connection con = DatabaseUtil.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql.toString());
				){
			pstmt.setInt(1, totalSales);
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
	public void createMenu(String accountId, String name, int price) throws SQLException {
		RestaurantVO restaurantVo = checkMyRes(accountId);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int menuId = 0;
		

		try {
			con = DatabaseUtil.getConnection();
			String sql = "INSERT INTO menu(restaurant_idx, name, price) VALUES(?,?,?);";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			pstmt.setString(2, name);
			pstmt.setInt(3, price);

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
	 * accountId를 통해 해당 계정의 restaurantId 정보를 알아냅니다.
	 * "내 식당을 조회하는 메소드" 를 사용하여 식당 정보 객체를 받아온 후, id만 뺍니다.
	 * @param restaurantId
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public List<Map<String, String>> checkMenu(String accountId) throws SQLException {
		
		RestaurantVO restaurantVo = checkMyRes(accountId);
		
		List<Map<String, String>> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
	

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
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
	
	public boolean findMenu(String accountId, String name)
			throws NotFoundMenuException, SQLException, AccountNotFoundException {
		RestaurantVO restaurantVo = checkMyRes(accountId);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean menuExist = true;


		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx =? AND name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
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

	public void UpdateMenu(String accountId, String name, int price) throws SQLException, NotFoundMenuException, AccountNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		RestaurantVO restaurantVo = checkMyRes(accountId);
		//메뉴가 존재하는지 확인
		findMenu(accountId, name);
	
		try {
			con = DatabaseUtil.getConnection();
			String sql = "UPDATE menu SET price = ? WHERE name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			pstmt.setString(2, restaurantVo.getName());
		
			result = pstmt.executeUpdate();
			
		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}
		
	}
	
	/**
	 * 메뉴를 삭제하는 메소드 입니다.
	 * @param restaurantId
	 * @param name
	 * @throws SQLException
	 * @throws NotFoundMenuException
	 * @throws AccountNotFoundException
	 */

	public void deleteMenu(String accountId, String name) throws SQLException, NotFoundMenuException, AccountNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;

		//메뉴가 존재하는지 확인
		findMenu(accountId, name);

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
	public List checkMyRestaurantReview(String accountId) throws SQLException {
		
		List<Map<String, String>> list = new ArrayList<>();
		//식당 정보 가져오기
		RestaurantVO restaurantVo = checkMyRes(accountId);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT account_id, star, comment, registerdate FROM review WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
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
